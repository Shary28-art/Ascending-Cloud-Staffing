package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.dto.UserAdminRoleDTO;
import com.ascendingcloud.staffing.entity.AdminRole;
import com.ascendingcloud.staffing.entity.User;
import com.ascendingcloud.staffing.entity.UserAdminRole;
import com.ascendingcloud.staffing.exception.BadRequestException;
import com.ascendingcloud.staffing.exception.ResourceNotFoundException;
import com.ascendingcloud.staffing.repository.AdminRoleRepository;
import com.ascendingcloud.staffing.repository.UserAdminRoleRepository;
import com.ascendingcloud.staffing.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAdminRoleService {

    private final UserAdminRoleRepository repository;
    private final UserRepository userRepository;
    private final AdminRoleRepository adminRoleRepository;

    public UserAdminRoleService(
            UserAdminRoleRepository repository,
            UserRepository userRepository,
            AdminRoleRepository adminRoleRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.adminRoleRepository = adminRoleRepository;
    }

    @Transactional(readOnly = true)
    public List<UserAdminRoleDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<UserAdminRoleDTO> getByUser(Integer userId) {
        return repository.findByUserId(userId).stream().map(this::toDTO).toList();
    }

    @Transactional
    public UserAdminRoleDTO assign(UserAdminRoleDTO dto) {
        if (dto == null || dto.userId() == null || dto.adminRoleId() == null) {
            throw new BadRequestException("userId and adminRoleId are required");
        }

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AdminRole role = adminRoleRepository.findById(dto.adminRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new BadRequestException("Admin roles can only be assigned to ADMIN users");
        }

        if (repository.existsByUserIdAndAdminRoleId(user.getId(), role.getId())) {
            throw new BadRequestException("User already has this admin role");
        }

        UserAdminRole assignment = new UserAdminRole();
        assignment.setUser(user);
        assignment.setAdminRole(role);

        return toDTO(repository.save(assignment));
    }

    @Transactional
    public void remove(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User admin role not found");
        }

        repository.deleteById(id);
    }

    private UserAdminRoleDTO toDTO(UserAdminRole u) {
        return new UserAdminRoleDTO(
                u.getId(),
                u.getUser().getId(),
                u.getAdminRole().getId(),
                u.getAssignedAt());
    }
}
