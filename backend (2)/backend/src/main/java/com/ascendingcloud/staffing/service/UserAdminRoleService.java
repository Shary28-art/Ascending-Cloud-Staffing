package com.ascendingcloud.staffing.service;

import com.ascendingcloud.staffing.entity.UserAdminRole;
import com.ascendingcloud.staffing.repository.UserAdminRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAdminRoleService {

    private final UserAdminRoleRepository userAdminRoleRepository;

    public UserAdminRoleService(UserAdminRoleRepository userAdminRoleRepository) {
        this.userAdminRoleRepository = userAdminRoleRepository;
    }

    public List<UserAdminRole> getAllUserAdminRoles() {
        return userAdminRoleRepository.findAll();
    }

    public UserAdminRole getUserAdminRoleById(Integer id) {
        return userAdminRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User admin role not found"));
    }

    public UserAdminRole createUserAdminRole(UserAdminRole userAdminRole) {
        return userAdminRoleRepository.save(userAdminRole);
    }

    public UserAdminRole updateUserAdminRole(
            Integer id,
            UserAdminRole userAdminRole) {

        UserAdminRole existing = getUserAdminRoleById(id);

        existing.setRoleId(userAdminRole.getRoleId());

        return userAdminRoleRepository.save(existing);
    }

    public void deleteUserAdminRole(Integer id) {

        if (!userAdminRoleRepository.existsById(id)) {
            throw new RuntimeException("User admin role not found");
        }

        userAdminRoleRepository.deleteById(id);
    }
}