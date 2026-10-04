package com.ascendingcloud.staffing.controller;

import com.ascendingcloud.staffing.dto.UserAdminRoleDTO;
import com.ascendingcloud.staffing.service.UserAdminRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Admin only (enforced in SecurityConfig). */
@RestController
@RequestMapping("/api/user-admin-roles")
public class UserAdminRoleController {

    private final UserAdminRoleService service;

    public UserAdminRoleController(UserAdminRoleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserAdminRoleDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAdminRoleDTO>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getByUser(userId));
    }

    /** Body: {"userId": 1, "adminRoleId": 2}. */
    @PostMapping
    public ResponseEntity<UserAdminRoleDTO> assign(@RequestBody UserAdminRoleDTO request) {
        return ResponseEntity.status(201).body(service.assign(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Integer id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }
}
