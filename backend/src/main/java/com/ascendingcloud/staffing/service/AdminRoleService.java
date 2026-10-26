package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.AdminRole;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.AdminRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminRoleService {

    private final AdminRoleRepository repository;

    public AdminRoleService(AdminRoleRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<AdminRole> getAllAdminRoles() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public AdminRole getAdminRoleById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin role not found with id: " + id));
    }

    @Transactional
    public AdminRole createAdminRole(AdminRole entity) {
        String name = cleanName(entity.getName());

        AdminRole created = new AdminRole();
        created.setName(name);
        created.setDescription(entity.getDescription());

        return repository.save(created);
    }

    @Transactional
    public AdminRole updateAdminRole(Integer id, AdminRole entity) {
        AdminRole existing = getAdminRoleById(id);

        existing.setName(cleanName(entity.getName()));
        existing.setDescription(entity.getDescription());

        return repository.save(existing);
    }

    @Transactional
    public void deleteAdminRole(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Admin role not found with id: " + id);
        }

        repository.deleteById(id);
    }

    private String cleanName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Admin role name is required");
        }

        return name.trim();
    }
}
