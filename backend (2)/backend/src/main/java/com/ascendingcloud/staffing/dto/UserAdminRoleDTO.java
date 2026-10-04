package com.ascendingcloud.staffing.dto;

public class UserAdminRoleDTO {

    private Integer id;
    private Integer userId;
    private Integer adminRoleId;

    public UserAdminRoleDTO() {
    }

    public UserAdminRoleDTO(Integer id, Integer userId, Integer adminRoleId) {
        this.id = id;
        this.userId = userId;
        this.adminRoleId = adminRoleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAdminRoleId() {
        return adminRoleId;
    }

    public void setAdminRoleId(Integer adminRoleId) {
        this.adminRoleId = adminRoleId;
    }
}