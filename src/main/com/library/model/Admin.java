package com.library.model;

import java.util.Date;

/**
 * 管理员实体类，对应数据库admin表
 */
public class Admin {
    // 字段对应数据库列名（驼峰转下划线：adminId → admin_id）
    private Integer adminId;       // 管理员ID（主键，自增）
    private String username;       // 登录用户名（唯一）
    private String password;       // 登录密码（加密存储）
    private String role;           // 角色：super_admin/normal_admin
    private Integer status;        // 账号状态：0禁用/1启用
    private Date createTime;       // 账号创建时间

    // 空构造（必须，用于反射/ORM赋值）
    public Admin() {}

    // 全字段构造（可选，便于快速创建对象）
    public Admin(Integer adminId, String username, String password, String role, Integer status, Date createTime) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.createTime = createTime;
    }

    // Getter & Setter
    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    // 重写toString（便于调试）
    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}