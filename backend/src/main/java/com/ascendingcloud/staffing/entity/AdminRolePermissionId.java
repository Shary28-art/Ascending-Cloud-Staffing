package com.ascendingcloud.staffing.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AdminRolePermissionId implements Serializable {

    private Integer roleId;

    private Integer permissionId;

    public AdminRolePermissionId() {
    }

    public AdminRolePermissionId(Integer roleId, Integer permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminRolePermissionId)) return false;

        AdminRolePermissionId that = (AdminRolePermissionId) o;

        return Objects.equals(roleId, that.roleId)
                && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }
}