package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.UserAdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAdminRoleRepository extends JpaRepository<UserAdminRole, Integer> {

    List<UserAdminRole> findByUserId(Integer userId);

    boolean existsByUserIdAndAdminRoleId(Integer userId, Integer adminRoleId);
}
