package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.AdminRolePermission;
import com.ascendingcloud.staffing.entity.AdminRolePermissionId;
import com.ascendingcloud.staffing.service.AdminRolePermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-role-permissions")
@CrossOrigin(origins = "*")
public class AdminRolePermissionController {

    private final AdminRolePermissionService service;

    public AdminRolePermissionController(
            AdminRolePermissionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AdminRolePermission>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{roleId}/{permissionId}")
    public ResponseEntity<AdminRolePermission> getById(
            @PathVariable Integer roleId,
            @PathVariable Integer permissionId) {

        AdminRolePermissionId id =
                new AdminRolePermissionId(roleId, permissionId);

        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<AdminRolePermission> create(
            @RequestBody AdminRolePermission rolePermission) {
        return ResponseEntity.ok(service.create(rolePermission));
    }

    @PutMapping("/{roleId}/{permissionId}")
    public ResponseEntity<AdminRolePermission> update(
            @PathVariable Integer roleId,
            @PathVariable Integer permissionId,
            @RequestBody AdminRolePermission rolePermission) {

        AdminRolePermissionId id =
                new AdminRolePermissionId(roleId, permissionId);

        return ResponseEntity.ok(service.update(id, rolePermission));
    }

    @DeleteMapping("/{roleId}/{permissionId}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer roleId,
            @PathVariable Integer permissionId) {

        AdminRolePermissionId id =
                new AdminRolePermissionId(roleId, permissionId);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}