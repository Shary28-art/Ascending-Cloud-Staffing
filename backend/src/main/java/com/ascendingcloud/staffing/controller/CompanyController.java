package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.CompanyDTO;
import com.ascendingcloud.staffing.security.CurrentUserService;
import com.ascendingcloud.staffing.service.CompanyService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final CurrentUserService currentUserService;

    public CompanyController(
            CompanyService companyService,
            CurrentUserService currentUserService) {
        this.companyService = companyService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompany(@PathVariable Integer id) {
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO request) {
        return ResponseEntity.status(201).body(
                companyService.createCompany(currentUserService.getUser(), request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<CompanyDTO> updateCompany(
            @PathVariable Integer id,
            @RequestBody CompanyDTO request) {
        return ResponseEntity.ok(
                companyService.updateCompany(currentUserService.getUser(), id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    /** multipart/form-data with a "file" part. PNG, JPG, JPEG or WebP, max 2 MB. */
    @PostMapping(value = "/{id}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('EMPLOYER','ADMIN')")
    public ResponseEntity<CompanyDTO> uploadLogo(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(
                companyService.uploadLogo(currentUserService.getUser(), id, file));
    }

    /** Public: logos appear on company profiles and job postings. */
    @GetMapping("/{id}/logo")
    public ResponseEntity<InputStreamResource> getLogo(@PathVariable Integer id) {
        CompanyService.LogoFile logo = companyService.loadLogo(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(logo.contentType()))
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .body(new InputStreamResource(logo.stream()));
    }
}
