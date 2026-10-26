package com.ascendingcloud.staffing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_role_permissions")
public class AdminRolePermission {

    @EmbeddedId
    private AdminRolePermissionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private AdminRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    public AdminRolePermission() {
    }

    public AdminRolePermission(
            AdminRolePermissionId id,
            AdminRole role,
            Permission permission) {
        this.id = id;
        this.role = role;
        this.permission = permission;
    }

    public AdminRolePermissionId getId() {
        return id;
    }

    public void setId(AdminRolePermissionId id) {
        this.id = id;
    }

    public AdminRole getRole() {
        return role;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}