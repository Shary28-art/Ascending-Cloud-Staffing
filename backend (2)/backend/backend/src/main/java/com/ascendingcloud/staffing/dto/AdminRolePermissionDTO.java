package com.ascendingcloud.staffing.dto;

public class AdminRolePermissionDTO {

    private Integer id;
    private Integer adminRoleId;
    private Integer permissionId;

    public AdminRolePermissionDTO() {
    }

    public AdminRolePermissionDTO(
            Integer id,
            Integer adminRoleId,
            Integer permissionId) {
        this.id = id;
        this.adminRoleId = adminRoleId;
        this.permissionId = permissionId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdminRoleId() {
        return adminRoleId;
    }

    public void setAdminRoleId(Integer adminRoleId) {
        this.adminRoleId = adminRoleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }
}