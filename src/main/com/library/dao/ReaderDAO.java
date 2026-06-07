package com.library.dao;

import com.library.DBUtil;
import com.library.model.Reader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReaderDAO {

    public boolean register(Reader reader) throws SQLException {
        String sql = "INSERT INTO reader(name, phone, password, email, register_time, status) VALUES (?,?,?,?,?,1)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reader.getName());
            pstmt.setString(2, reader.getPhone());
            pstmt.setString(3, reader.getPassword());
            pstmt.setString(4, reader.getEmail());
            pstmt.setDate(5, new java.sql.Date(new Date().getTime()));
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public Reader login(String phone, String password) throws SQLException {
        String sql = "SELECT * FROM reader WHERE phone=? AND password=? AND status=1";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Reader reader = new Reader();
                reader.setReaderId(rs.getInt("reader_id"));
                reader.setName(rs.getString("name"));
                reader.setPhone(rs.getString("phone"));
                reader.setEmail(rs.getString("email"));
                reader.setRegisterTime(rs.getDate("register_time"));
                reader.setStatus(rs.getInt("status"));
                return reader;
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }

    public List<Reader> listAll() throws SQLException {
        String sql = "SELECT * FROM reader";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reader> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Reader reader = new Reader();
                reader.setReaderId(rs.getInt("reader_id"));
                reader.setName(rs.getString("name"));
                reader.setPhone(rs.getString("phone"));
                reader.setEmail(rs.getString("email"));
                reader.setRegisterTime(rs.getDate("register_time"));
                reader.setStatus(rs.getInt("status"));
                list.add(reader);
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    public boolean updateStatus(Integer readerId, Integer status) throws SQLException {
        String sql = "UPDATE reader SET status=? WHERE reader_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, readerId);
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public Reader getById(Integer readerId) throws SQLException {
        String sql = "SELECT * FROM reader WHERE reader_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, readerId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Reader reader = new Reader();
                reader.setReaderId(rs.getInt("reader_id"));
                reader.setName(rs.getString("name"));
                reader.setPhone(rs.getString("phone"));
                reader.setEmail(rs.getString("email"));
                reader.setRegisterTime(rs.getDate("register_time"));
                reader.setStatus(rs.getInt("status"));
                return reader;
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }
}