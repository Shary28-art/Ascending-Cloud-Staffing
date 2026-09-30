package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.Permission;
import com.ascendingcloud.staffing.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@CrossOrigin(origins = "*")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(
            @RequestBody Permission permission) {
        return ResponseEntity.ok(permissionService.createPermission(permission));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(
            @PathVariable Integer id,
            @RequestBody Permission permission) {
        return ResponseEntity.ok(
                permissionService.updatePermission(id, permission)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(
            @PathVariable Integer id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}