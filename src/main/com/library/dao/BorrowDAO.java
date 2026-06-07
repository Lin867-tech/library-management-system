package com.library.dao;

import com.library.DBUtil;
import com.library.model.Borrow;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowDAO {

    // 1. 添加借阅记录：替换setDate为setTimestamp，保留时分秒
    public boolean add(Borrow borrow) throws SQLException {
        String sql = "INSERT INTO borrow(reader_id, book_id, borrow_time, due_time, overdue_status, fine_amount) VALUES (?,?,?,?,0,0.00)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, borrow.getReaderId());
            pstmt.setInt(2, borrow.getBookId());
            // 核心修改1：把util.Date转Timestamp，用setTimestamp存储（保留时分秒）
            pstmt.setTimestamp(3, new Timestamp(borrow.getBorrowTime().getTime()));
            pstmt.setTimestamp(4, new Timestamp(borrow.getDueTime().getTime()));
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 2. 查询读者借阅记录：替换getDate为getTimestamp，转回util.Date
    public List<Borrow> listByReaderId(Integer readerId) throws SQLException {
        String sql = "SELECT * FROM borrow WHERE reader_id=? ORDER BY borrow_time DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Borrow> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, readerId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Borrow borrow = new Borrow();
                borrow.setBorrowId(rs.getInt("borrow_id"));
                borrow.setReaderId(rs.getInt("reader_id"));
                borrow.setBookId(rs.getInt("book_id"));
                // 核心修改2：用getTimestamp读取，转成util.Date（保留时分秒）
                borrow.setBorrowTime(new Date(rs.getTimestamp("borrow_time").getTime()));
                borrow.setDueTime(new Date(rs.getTimestamp("due_time").getTime()));
                // 归还时间可能为NULL，需判空
                Timestamp returnTs = rs.getTimestamp("return_time");
                borrow.setReturnTime(returnTs == null ? null : new Date(returnTs.getTime()));
                borrow.setOverdueStatus(rs.getInt("overdue_status"));
                borrow.setFineAmount(rs.getBigDecimal("fine_amount"));
                list.add(borrow);
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    // 3. 归还图书：替换setDate为setTimestamp，保留归还时间的时分秒
    public boolean returnBook(Integer borrowId, Date returnTime) throws SQLException {
        Borrow borrow = getById(borrowId);
        if (borrow == null) return false;

        // 逾期判断：用精确时间（时分秒）对比，而非仅年月日
        int overdueStatus = returnTime.after(borrow.getDueTime()) ? 1 : 0;
        BigDecimal fineAmount = overdueStatus == 1 ? calculateFine(borrow.getDueTime(), returnTime) : new BigDecimal("0.00");

        String sql = "UPDATE borrow SET return_time=?, overdue_status=?, fine_amount=? WHERE borrow_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            // 核心修改3：setTimestamp存储归还时间（保留时分秒）
            pstmt.setTimestamp(1, new Timestamp(returnTime.getTime()));
            pstmt.setInt(2, overdueStatus);
            pstmt.setBigDecimal(3, fineAmount);
            pstmt.setInt(4, borrowId);
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 4. 根据ID查询借阅记录：同步修改getTimestamp，保证时间精度
    public Borrow getById(Integer borrowId) throws SQLException {
        String sql = "SELECT * FROM borrow WHERE borrow_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, borrowId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Borrow borrow = new Borrow();
                borrow.setBorrowId(rs.getInt("borrow_id"));
                borrow.setReaderId(rs.getInt("reader_id"));
                borrow.setBookId(rs.getInt("book_id"));
                // 核心修改4：getTimestamp读取，转util.Date
                borrow.setBorrowTime(new Date(rs.getTimestamp("borrow_time").getTime()));
                borrow.setDueTime(new Date(rs.getTimestamp("due_time").getTime()));
                // 归还时间判空
                Timestamp returnTs = rs.getTimestamp("return_time");
                borrow.setReturnTime(returnTs == null ? null : new Date(returnTs.getTime()));
                borrow.setOverdueStatus(rs.getInt("overdue_status"));
                borrow.setFineAmount(rs.getBigDecimal("fine_amount"));
                return borrow;
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }

    // 计算逾期罚款（无需修改，时间精度已由上层保证）
    private BigDecimal calculateFine(Date dueTime, Date returnTime) {
        long days = (returnTime.getTime() - dueTime.getTime()) / (1000 * 60 * 60 * 24);
        return new BigDecimal(days).multiply(new BigDecimal("1.00"));
    }
}