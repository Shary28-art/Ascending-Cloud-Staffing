package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Company;
import com.ascendingcloud.staffing.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(
            @RequestBody CompanyRequest request) {

        return ResponseEntity.ok(
                companyService.createCompany(
                        request.name(),
                        request.description(),
                        request.website(),
                        request.location()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(
            @PathVariable Integer id) {

        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(
            @PathVariable Integer id,
            @RequestBody CompanyRequest request) {

        return ResponseEntity.ok(
                companyService.updateCompany(
                        id,
                        request.name(),
                        request.description(),
                        request.website(),
                        request.location()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable Integer id) {

        companyService.deleteCompany(id);

        return ResponseEntity.noContent().build();
    }

    public record CompanyRequest(
            String name,
            String description,
            String website,
            String location
    ) {
    }
}