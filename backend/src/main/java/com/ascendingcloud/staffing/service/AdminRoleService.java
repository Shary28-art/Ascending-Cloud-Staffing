package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.AdminRole;
import com.ascendingcloud.staffing.repository.AdminRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminRoleService {

    private final AdminRoleRepository adminRoleRepository;

    public AdminRoleService(AdminRoleRepository adminRoleRepository) {
        this.adminRoleRepository = adminRoleRepository;
    }

    public List<AdminRole> getAllRoles() {
        return adminRoleRepository.findAll();
    }

    public AdminRole getRoleById(Integer id) {
        return adminRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin role not found with id: " + id));
    }

    public AdminRole createRole(AdminRole role) {
        return adminRoleRepository.save(role);
    }

    public AdminRole updateRole(Integer id, AdminRole role) {
        AdminRole existingRole = getRoleById(id);

        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());

        return adminRoleRepository.save(existingRole);
    }

    public void deleteRole(Integer id) {
        if (!adminRoleRepository.existsById(id)) {
            throw new RuntimeException("Admin role not found with id: " + id);
        }

        adminRoleRepository.deleteById(id);
    }
}