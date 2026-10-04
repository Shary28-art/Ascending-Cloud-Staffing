package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.AdminRole;
import com.ascendingcloud.staffing.service.AdminRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-roles")
public class AdminRoleController {

    private final AdminRoleService service;

    public AdminRoleController(AdminRoleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AdminRole>> getAllAdminRoles() {
        return ResponseEntity.ok(service.getAllAdminRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminRole> getAdminRoleById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getAdminRoleById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminRole> createAdminRole(@RequestBody AdminRole body) {
        return ResponseEntity.status(201).body(service.createAdminRole(body));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminRole> updateAdminRole(
            @PathVariable Integer id,
            @RequestBody AdminRole body) {
        return ResponseEntity.ok(service.updateAdminRole(id, body));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAdminRole(@PathVariable Integer id) {
        service.deleteAdminRole(id);
        return ResponseEntity.noContent().build();
    }
}
