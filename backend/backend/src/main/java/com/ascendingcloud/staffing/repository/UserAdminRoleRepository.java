package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.UserAdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAdminRoleRepository extends JpaRepository<UserAdminRole, Integer> {
}