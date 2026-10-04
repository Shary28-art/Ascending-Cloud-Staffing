package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Permission;
import com.ascendingcloud.staffing.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission getPermissionById(Integer id) {
        return permissionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Permission not found with id: " + id));
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Integer id, Permission permission) {
        Permission existingPermission = getPermissionById(id);

        existingPermission.setName(permission.getName());
        existingPermission.setDescription(permission.getDescription());

        return permissionRepository.save(existingPermission);
    }

    public void deletePermission(Integer id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException(
                    "Permission not found with id: " + id
            );
        }

        permissionRepository.deleteById(id);
    }
}