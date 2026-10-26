package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.Permission;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository repository;

    public PermissionService(PermissionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Permission> getAllPermissions() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Permission getPermissionById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Permission not found with id: " + id));
    }

    @Transactional
    public Permission createPermission(Permission entity) {
        String name = cleanName(entity.getName());

        Permission created = new Permission();
        created.setName(name);
        created.setDescription(entity.getDescription());

        return repository.save(created);
    }

    @Transactional
    public Permission updatePermission(Integer id, Permission entity) {
        Permission existing = getPermissionById(id);

        existing.setName(cleanName(entity.getName()));
        existing.setDescription(entity.getDescription());

        return repository.save(existing);
    }

    @Transactional
    public void deletePermission(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Permission not found with id: " + id);
        }

        repository.deleteById(id);
    }

    private String cleanName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Permission name is required");
        }

        return name.trim();
    }
}
