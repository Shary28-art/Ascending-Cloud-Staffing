package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.entity.UserAdminRole;
import com.ascendingcloud.staffing.service.UserAdminRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-admin-roles")
@CrossOrigin(origins = "*")
public class UserAdminRoleController {

    private final UserAdminRoleService userAdminRoleService;

    public UserAdminRoleController(UserAdminRoleService userAdminRoleService) {
        this.userAdminRoleService = userAdminRoleService;
    }

    @GetMapping
    public ResponseEntity<List<UserAdminRole>> getAllUserAdminRoles() {
        return ResponseEntity.ok(userAdminRoleService.getAllUserAdminRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAdminRole> getUserAdminRoleById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                userAdminRoleService.getUserAdminRoleById(id)
        );
    }

    @PostMapping
    public ResponseEntity<UserAdminRole> createUserAdminRole(
            @RequestBody UserAdminRole userAdminRole) {
        return ResponseEntity.ok(
                userAdminRoleService.createUserAdminRole(userAdminRole)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAdminRole> updateUserAdminRole(
            @PathVariable Integer id,
            @RequestBody UserAdminRole userAdminRole) {
        return ResponseEntity.ok(
                userAdminRoleService.updateUserAdminRole(id, userAdminRole)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAdminRole(
            @PathVariable Integer id) {
        userAdminRoleService.deleteUserAdminRole(id);
        return ResponseEntity.noContent().build();
    }
}