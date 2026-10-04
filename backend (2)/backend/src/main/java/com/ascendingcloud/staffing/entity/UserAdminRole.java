package com.ascendingcloud.staffing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_admin_roles")
public class UserAdminRole {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    public UserAdminRole() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}