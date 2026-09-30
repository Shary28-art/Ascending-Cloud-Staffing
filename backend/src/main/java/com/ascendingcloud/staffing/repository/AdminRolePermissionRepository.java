package com.ascendingcloud.staffing.repository;

import com.ascendingcloud.staffing.entity.AdminRolePermission;
import com.ascendingcloud.staffing.entity.AdminRolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRolePermissionRepository
        extends JpaRepository<AdminRolePermission, AdminRolePermissionId> {
}