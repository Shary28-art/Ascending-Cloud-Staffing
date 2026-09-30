package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.AdminRolePermission;
import com.ascendingcloud.staffing.entity.AdminRolePermissionId;
import com.ascendingcloud.staffing.repository.AdminRolePermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminRolePermissionService {

    private final AdminRolePermissionRepository repository;

    public AdminRolePermissionService(
            AdminRolePermissionRepository repository) {
        this.repository = repository;
    }

    public List<AdminRolePermission> getAll() {
        return repository.findAll();
    }

    public AdminRolePermission getById(AdminRolePermissionId id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Admin role permission not found"));
    }

    public AdminRolePermission create(
            AdminRolePermission rolePermission) {
        return repository.save(rolePermission);
    }

    public AdminRolePermission update(
            AdminRolePermissionId id,
            AdminRolePermission rolePermission) {

        AdminRolePermission existing = getById(id);

        return repository.save(existing);
    }

    public void delete(AdminRolePermissionId id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "Admin role permission not found");
        }

        repository.deleteById(id);
    }
}