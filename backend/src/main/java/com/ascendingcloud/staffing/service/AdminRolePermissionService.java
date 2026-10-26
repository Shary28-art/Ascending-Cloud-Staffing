package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.AdminRolePermissionDTO;
import com.ascendingcloud.staffing.entity.AdminRole;
import com.ascendingcloud.staffing.entity.AdminRolePermission;
import com.ascendingcloud.staffing.entity.AdminRolePermissionId;
import com.ascendingcloud.staffing.entity.Permission;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.AdminRolePermissionRepository;
import com.ascendingcloud.staffing.repository.AdminRoleRepository;
import com.ascendingcloud.staffing.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminRolePermissionService {

    private final AdminRolePermissionRepository repository;
    private final AdminRoleRepository adminRoleRepository;
    private final PermissionRepository permissionRepository;

    public AdminRolePermissionService(
            AdminRolePermissionRepository repository,
            AdminRoleRepository adminRoleRepository,
            PermissionRepository permissionRepository) {
        this.repository = repository;
        this.adminRoleRepository = adminRoleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminRolePermissionDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<AdminRolePermissionDTO> getByRole(Integer roleId) {
        return repository.findByIdRoleId(roleId).stream().map(this::toDTO).toList();
    }

    @Transactional
    public AdminRolePermissionDTO create(AdminRolePermissionDTO dto) {
        if (dto == null || dto.adminRoleId() == null || dto.permissionId() == null) {
            throw new BadRequestException("adminRoleId and permissionId are required");
        }

        AdminRolePermissionId id =
                new AdminRolePermissionId(dto.adminRoleId(), dto.permissionId());

        if (repository.existsById(id)) {
            throw new BadRequestException("This permission is already assigned to the role");
        }

        AdminRole role = adminRoleRepository.findById(dto.adminRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

        Permission permission = permissionRepository.findById(dto.permissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        return toDTO(repository.save(new AdminRolePermission(id, role, permission)));
    }

    @Transactional
    public void delete(Integer roleId, Integer permissionId) {
        AdminRolePermissionId id = new AdminRolePermissionId(roleId, permissionId);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Admin role permission not found");
        }

        repository.deleteById(id);
    }

    private AdminRolePermissionDTO toDTO(AdminRolePermission arp) {
        return new AdminRolePermissionDTO(
                arp.getId().getRoleId(),
                arp.getId().getPermissionId());
    }
}
