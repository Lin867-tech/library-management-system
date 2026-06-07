package com.library.dao;

import com.library.DBUtil;
import com.library.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员DAO，处理管理员表的CRUD
 */
public class AdminDAO {

    /**
     * 管理员登录（用户名+密码）
     * @throws SQLException 数据库异常
     */
    public Admin login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admin WHERE username=? AND password=? AND status=1";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection(); // 可能抛SQLException，方法声明抛出
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                admin.setRole(rs.getString("role"));
                admin.setStatus(rs.getInt("status"));
                admin.setCreateTime(rs.getDate("create_time"));
                return admin;
            }
        } finally {
            DBUtil.close(conn, pstmt, rs); // 传全三个参数
        }
        return null;
    }

    /**
     * 查询所有管理员（超级管理员用）
     * @throws SQLException 数据库异常
     */
    public List<Admin> listAll() throws SQLException {
        String sql = "SELECT * FROM admin";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Admin> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setRole(rs.getString("role"));
                admin.setStatus(rs.getInt("status"));
                admin.setCreateTime(rs.getDate("create_time"));
                list.add(admin);
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    /**
     * 添加管理员（超级管理员用）
     * @throws SQLException 数据库异常
     */
    public boolean add(Admin admin) throws SQLException {
        String sql = "INSERT INTO admin(username, password, role, status) VALUES (?,?,?,1)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, admin.getPassword());
            pstmt.setString(3, admin.getRole());
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt); // 调用仅传conn和pstmt的重载方法
        }
    }

    /**
     * 禁用/启用管理员
     * @throws SQLException 数据库异常
     */
    public boolean updateStatus(Integer adminId, Integer status) throws SQLException {
        String sql = "UPDATE admin SET status=? WHERE admin_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, adminId);
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt); // 调用仅传conn和pstmt的重载方法
        }
    }
}