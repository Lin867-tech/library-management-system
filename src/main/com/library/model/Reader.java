package com.library.model;

import java.util.Date;

/**
 * 读者实体类，对应数据库reader表
 */
public class Reader {
    private Integer readerId;      // 读者ID（主键，自增）
    private String name;           // 读者姓名
    private String phone;          // 手机号（唯一）
    private String password;       // 登录密码（加密存储）
    private String email;          // 邮箱（可选）
    private Date registerTime;     // 注册时间
    private Integer status;        // 账号状态：0禁用/1启用

    // 空构造
    public Reader() {}

    // 全字段构造
    public Reader(Integer readerId, String name, String phone, String password, String email, Date registerTime, Integer status) {
        this.readerId = readerId;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.registerTime = registerTime;
        this.status = status;
    }

    // Getter & Setter
    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "readerId=" + readerId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", registerTime=" + registerTime +
                ", status=" + status +
                '}';
    }
}