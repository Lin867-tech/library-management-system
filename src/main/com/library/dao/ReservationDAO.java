package com.library.dao;

import com.library.DBUtil;
import com.library.model.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationDAO {

    public boolean add(Reservation reservation) throws SQLException {
        if (checkDuplicate(reservation.getReaderId(), reservation.getBookId())) {
            return false;
        }

        String sql = "INSERT INTO reservation(reader_id, book_id, reserve_time, reserve_status) VALUES (?,?,?,0)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservation.getReaderId());
            pstmt.setInt(2, reservation.getBookId());
            pstmt.setDate(3, new java.sql.Date(new Date().getTime()));
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    private boolean checkDuplicate(Integer readerId, Integer bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservation WHERE reader_id=? AND book_id=? AND reserve_status IN (0,1)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, readerId);
            pstmt.setInt(2, bookId);
            rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return false;
    }

    public List<Reservation> listByReaderId(Integer readerId) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE reader_id=? ORDER BY reserve_time DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Reservation> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, readerId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReserveId(rs.getInt("reserve_id"));
                reservation.setReaderId(rs.getInt("reader_id"));
                reservation.setBookId(rs.getInt("book_id"));
                reservation.setReserveTime(rs.getDate("reserve_time"));
                reservation.setReserveStatus(rs.getInt("reserve_status"));
                list.add(reservation);
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    public boolean cancel(Integer reserveId) throws SQLException {
        String sql = "UPDATE reservation SET reserve_status=2 WHERE reserve_id=? AND reserve_status=0";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reserveId);
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean activate(Integer bookId) throws SQLException {
        String sql = "UPDATE reservation SET reserve_status=1 WHERE book_id=? AND reserve_status=0 LIMIT 1";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}