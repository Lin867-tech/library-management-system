package com.library.dao;

import com.library.DBUtil;
import com.library.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> listAll() throws SQLException {
        String sql = "SELECT * FROM category";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Category> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setParentId(rs.getInt("parent_id"));
                category.setDescription(rs.getString("description"));
                list.add(category);
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    public boolean add(Category category) throws SQLException {
        String sql = "INSERT INTO category(category_name, parent_id, description) VALUES (?,?,?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category.getCategoryName());
            pstmt.setInt(2, category.getParentId() == null ? 0 : category.getParentId());
            pstmt.setString(3, category.getDescription());
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public Category getById(Integer categoryId) throws SQLException {
        String sql = "SELECT * FROM category WHERE category_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setParentId(rs.getInt("parent_id"));
                category.setDescription(rs.getString("description"));
                return category;
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }
}