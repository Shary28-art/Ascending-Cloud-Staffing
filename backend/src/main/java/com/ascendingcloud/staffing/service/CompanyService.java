package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.CompanyDTO;
import com.ascendingcloud.staffing.entity.Company;
import com.ascendingcloud.staffing.entity.EmployerProfile;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.mapper.CompanyMapper;
import com.ascendingcloud.staffing.repository.CompanyRepository;
import com.ascendingcloud.staffing.repository.EmployerProfileRepository;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.storage.FileValidator;
import com.ascendingcloud.staffing.storage.StorageService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class CompanyService {

    /** Logos larger than this (in px) are scaled down and re-encoded. */
    private static final int MAX_LOGO_DIMENSION = 1000;

    public record LogoFile(InputStream stream, String contentType) {
    }

    private final CompanyRepository companyRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final CurrentUserService currentUserService;
    private final StorageService storageService;

    public CompanyService(
            CompanyRepository companyRepository,
            EmployerProfileRepository employerProfileRepository,
            CurrentUserService currentUserService,
            StorageService storageService) {
        this.companyRepository = companyRepository;
        this.employerProfileRepository = employerProfileRepository;
        this.currentUserService = currentUserService;
        this.storageService = storageService;
    }

    @Transactional(readOnly = true)
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream().map(CompanyMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public CompanyDTO getCompany(Integer id) {
        return CompanyMapper.toDTO(find(id));
    }

    /** Admins can create any company; an employer only if they do not have one yet. */
    @Transactional
    public CompanyDTO createCompany(User user, CompanyDTO dto) {

        EmployerProfile employerProfile = null;

        if (currentUserService.isEmployer(user)) {
            employerProfile = employerProfileRepository.findByUserId(user.getId()).orElse(null);

            if (employerProfile != null && employerProfile.getCompany() != null) {
                throw new BadRequestException("Your account is already linked to a company");
            }
        } else if (!currentUserService.isAdmin(user)) {
            throw new AccessDeniedException("Only employers or admins can create companies");
        }

        Company company = new Company();
        apply(company, dto);
        company = companyRepository.save(company);

        if (employerProfile != null) {
            employerProfile.setCompany(company);
            employerProfileRepository.save(employerProfile);
        }

        return CompanyMapper.toDTO(company);
    }

    @Transactional
    public CompanyDTO updateCompany(User user, Integer id, CompanyDTO dto) {
        Company company = find(id);
        currentUserService.requireCompanyAccess(user, id);

        apply(company, dto);

        return CompanyMapper.toDTO(companyRepository.save(company));
    }

    @Transactional
    public void deleteCompany(Integer id) {
        Company company = find(id);

        if (company.getLogoKey() != null) {
            storageService.delete(company.getLogoKey());
        }

        companyRepository.delete(company);
    }

    @Transactional
    public CompanyDTO uploadLogo(User user, Integer id, MultipartFile file) {
        Company company = find(id);
        currentUserService.requireCompanyAccess(user, id);

        String ext = FileValidator.validateLogo(file);

        byte[] bytes;

        try {
            bytes = optimise(file.getBytes(), ext);
        } catch (IOException e) {
            throw new BadRequestException("Could not process the logo image");
        }

        String key = FileValidator.secureKey("logos", ext.equals("jpeg") ? "jpg" : ext);

        storageService.store(key, new ByteArrayInputStream(bytes), bytes.length);

        String oldKey = company.getLogoKey();

        company.setLogoKey(key);
        company.setLogoContentType(FileValidator.logoContentType(ext));
        company.setLogoFileSize((long) bytes.length);

        Company saved = companyRepository.save(company);

        if (oldKey != null) {
            storageService.delete(oldKey);
        }

        return CompanyMapper.toDTO(saved);
    }

    /** Logos are public, so no permission check here. */
    @Transactional(readOnly = true)
    public LogoFile loadLogo(Integer id) {
        Company company = find(id);

        if (company.getLogoKey() == null) {
            throw new ResourceNotFoundException("This company has no logo");
        }

        return new LogoFile(
                storageService.load(company.getLogoKey()),
                company.getLogoContentType());
    }

    private Company find(Integer id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

    private void apply(Company company, CompanyDTO dto) {
        if (dto == null || dto.name() == null || dto.name().isBlank()) {
            throw new BadRequestException("Company name is required");
        }

        company.setName(dto.name().trim());
        company.setDescription(dto.description());
        company.setWebsite(dto.website());
        company.setIndustry(dto.industry());
        company.setCompanySize(dto.companySize());
        company.setLocation(dto.location());
    }

    /**
     * Scales big PNG/JPEG logos down and re-encodes them (this also strips
     * metadata). WebP is stored unchanged because the JDK cannot write it.
     */
    private byte[] optimise(byte[] original, String ext) throws IOException {
        if (ext.equals("webp")) {
            return original;
        }

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(original));

        if (image == null) {
            throw new BadRequestException("File is not a valid image");
        }

        int width = image.getWidth();
        int height = image.getHeight();
        int longest = Math.max(width, height);

        boolean png = ext.equals("png");

        if (longest > MAX_LOGO_DIMENSION) {
            double scale = (double) MAX_LOGO_DIMENSION / longest;
            int newW = Math.max(1, (int) Math.round(width * scale));
            int newH = Math.max(1, (int) Math.round(height * scale));

            BufferedImage resized = new BufferedImage(
                    newW, newH,
                    png ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);

            Graphics2D g = resized.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, 0, 0, newW, newH, null);
            g.dispose();

            image = resized;
        } else if (!png && image.getType() != BufferedImage.TYPE_INT_RGB) {
            // JPEG cannot be written from images with an alpha channel
            BufferedImage rgb = new BufferedImage(
                    width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = rgb.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            image = rgb;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (!ImageIO.write(image, png ? "png" : "jpg", out)) {
            throw new BadRequestException("Could not encode the logo image");
        }

        return out.toByteArray();
    }
}
