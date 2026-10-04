package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.EmployerProfile;
import com.ascendingcloud.staffing.service.EmployerProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employer-profiles")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class EmployerProfileController {

    private final EmployerProfileService employerProfileService;

    public EmployerProfileController(
            EmployerProfileService employerProfileService) {

        this.employerProfileService = employerProfileService;
    }

    @PostMapping
    public ResponseEntity<EmployerProfile> createProfile(
            @RequestParam String email,
            @RequestBody EmployerProfileRequest request) {

        return ResponseEntity.ok(
                employerProfileService.createProfile(
                        email,
                        request.phone(),
                        request.designation(),
                        request.companyId()
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<EmployerProfile> getProfile(
            @RequestParam String email) {

        return ResponseEntity.ok(
                employerProfileService.getProfile(email)
        );
    }

    @PutMapping("/me")
    public ResponseEntity<EmployerProfile> updateProfile(
            @RequestParam String email,
            @RequestBody EmployerProfileRequest request) {

        return ResponseEntity.ok(
                employerProfileService.updateProfile(
                        email,
                        request.phone(),
                        request.designation(),
                        request.companyId()
                )
        );
    }

    public record EmployerProfileRequest(
            String phone,
            String designation,
            Integer companyId
    ) {
    }
}