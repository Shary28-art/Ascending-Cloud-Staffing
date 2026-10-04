package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.AdminRolePermissionDTO;
import com.ascendingcloud.staffing.service.AdminRolePermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Admin only (enforced in SecurityConfig). */
@RestController
@RequestMapping("/api/admin-role-permissions")
public class AdminRolePermissionController {

    private final AdminRolePermissionService service;

    public AdminRolePermissionController(AdminRolePermissionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AdminRolePermissionDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<AdminRolePermissionDTO>> getByRole(@PathVariable Integer roleId) {
        return ResponseEntity.ok(service.getByRole(roleId));
    }

    /** Body: {"adminRoleId": 1, "permissionId": 4}. */
    @PostMapping
    public ResponseEntity<AdminRolePermissionDTO> create(
            @RequestBody AdminRolePermissionDTO request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @DeleteMapping("/{roleId}/{permissionId}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer roleId,
            @PathVariable Integer permissionId) {
        service.delete(roleId, permissionId);
        return ResponseEntity.noContent().build();
    }
}
