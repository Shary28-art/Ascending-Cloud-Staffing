package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.AuthResponse;
import com.ascendingcloud.staffing.dto.LoginRequest;
import com.ascendingcloud.staffing.dto.RegisterRequest;
import com.ascendingcloud.staffing.entity.CandidateProfile;
import com.ascendingcloud.staffing.entity.Company;
import com.ascendingcloud.staffing.entity.EmployerProfile;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.repository.CandidateProfileRepository;
import com.ascendingcloud.staffing.repository.CompanyRepository;
import com.ascendingcloud.staffing.repository.EmployerProfileRepository;
import com.ascendingcloud.staffing.repository.UserRepository;
import com.ascendingcloud.staffing.security.CustomUserDetailsService;
import com.ascendingcloud.staffing.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Account creation and login, following the "Information Needed to Create
 * an Account" lists in the requirements document.
 */
@Service
public class AuthService {

    private static final Pattern EMAIL =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            CandidateProfileRepository candidateProfileRepository,
            EmployerProfileRepository employerProfileRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder,
            CustomUserDetailsService userDetailsService,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.employerProfileRepository = employerProfileRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {

        if (req == null) {
            throw new BadRequestException("Request body is required");
        }

        String role = req.role() == null
                ? ""
                : req.role().trim().toUpperCase(Locale.ROOT);

        // Admin accounts are never self-registered.
        if (!role.equals("CANDIDATE") && !role.equals("EMPLOYER")) {
            throw new BadRequestException("Role must be CANDIDATE or EMPLOYER");
        }

        String firstName = trimToNull(req.firstName());
        String lastName = trimToNull(req.lastName());

        // Legacy support: a single "name" field
        if (firstName == null && trimToNull(req.name()) != null) {
            String[] parts = req.name().trim().split("\\s+", 2);
            firstName = parts[0];
            lastName = parts.length > 1 ? parts[1] : null;
        }

        require(firstName, "First name");
        require(lastName, "Last name");

        String email = trimToNull(req.email());
        require(email, "Email address");
        email = email.toLowerCase(Locale.ROOT);

        if (!EMAIL.matcher(email).matches()) {
            throw new BadRequestException("Email address is not valid");
        }

        require(trimToNull(req.phone()), "Phone number");
        validatePassword(req.password(), req.confirmPassword());

        if (!Boolean.TRUE.equals(req.termsAccepted())) {
            throw new BadRequestException(
                    "You must agree to the Privacy Policy and Terms and Conditions");
        }

        if (role.equals("CANDIDATE")) {
            require(trimToNull(req.city()), "City");
            require(trimToNull(req.state()), "State");
            require(trimToNull(req.country()), "Country");
            require(trimToNull(req.preferredJobTitle()), "Preferred job title");
            require(trimToNull(req.workAuthorizationStatus()), "Work authorization status");

            if (!Boolean.TRUE.equals(req.recruitmentConsent())) {
                throw new BadRequestException(
                        "You must consent to receive recruitment communications");
            }
        } else {
            require(trimToNull(req.companyName()), "Company name");
            require(trimToNull(req.jobTitle()), "Job title or position");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already registered");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setName(firstName + " " + lastName);
        user.setEmail(email);
        user.setPhone(req.phone().trim());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setRole(role);
        user.setTermsAccepted(true);
        user.setTermsAcceptedAt(LocalDateTime.now());
        user.setRecruitmentConsent(Boolean.TRUE.equals(req.recruitmentConsent()));

        user = userRepository.save(user);

        if (role.equals("CANDIDATE")) {
            createCandidateProfile(user, req);
        } else {
            createEmployerProfile(user, req);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return new AuthResponse(
                jwtService.generateToken(userDetails),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {

        if (req == null
                || trimToNull(req.email()) == null
                || req.password() == null) {
            throw new BadRequestException("Email and password are required");
        }

        User user = userRepository.findByEmail(req.email().trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        return new AuthResponse(
                jwtService.generateToken(userDetails),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }

    private void createCandidateProfile(User user, RegisterRequest req) {
        CandidateProfile profile = new CandidateProfile();

        profile.setUser(user);
        profile.setPhone(req.phone().trim());
        profile.setCity(req.city().trim());
        profile.setState(req.state().trim());
        profile.setCountry(req.country().trim());
        profile.setLocation(
                req.city().trim() + ", " + req.state().trim() + ", " + req.country().trim());
        profile.setDesiredJobTitle(req.preferredJobTitle().trim());
        profile.setWorkAuthorizationStatus(req.workAuthorizationStatus().trim());

        candidateProfileRepository.save(profile);
    }

    private void createEmployerProfile(User user, RegisterRequest req) {
        Company company = new Company();

        company.setName(req.companyName().trim());
        company.setWebsite(trimToNull(req.companyWebsite()));

        company = companyRepository.save(company);

        EmployerProfile profile = new EmployerProfile();

        profile.setUser(user);
        profile.setPhone(req.phone().trim());
        profile.setDesignation(req.jobTitle().trim());
        profile.setCompany(company);
        profile.setPrimaryContactName(user.getName());
        profile.setContactEmail(user.getEmail());

        employerProfileRepository.save(profile);
    }

    private void validatePassword(String password, String confirm) {
        if (password == null || password.length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters long");
        }

        if (password.length() > 72) {
            throw new BadRequestException("Password must be at most 72 characters long");
        }

        if (confirm == null || !password.equals(confirm)) {
            throw new BadRequestException("Password and confirm password do not match");
        }
    }

    private void require(String value, String label) {
        if (value == null) {
            throw new BadRequestException(label + " is required");
        }
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty() ? null : trimmed;
    }
}
