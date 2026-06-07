package com.library.dao;

import com.library.DBUtil;
import com.library.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> listAll() throws SQLException {
        String sql = "SELECT * FROM book";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> list = new ArrayList<>();
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setIsbn(rs.getString("isbn"));
                book.setBookName(rs.getString("book_name"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublishDate(rs.getDate("publish_date"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setStock(rs.getInt("stock"));
                book.setStatus(rs.getInt("status"));
                list.add(book);
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    public Book getById(Integer bookId) throws SQLException {
        String sql = "SELECT * FROM book WHERE book_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setIsbn(rs.getString("isbn"));
                book.setBookName(rs.getString("book_name"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublishDate(rs.getDate("publish_date"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setStock(rs.getInt("stock"));
                book.setStatus(rs.getInt("status"));
                return book;
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }

    public boolean add(Book book) throws SQLException {
        String sql = "INSERT INTO book(isbn, book_name, author, publisher, publish_date, category_id, stock, status) VALUES (?,?,?,?,?,?,?,0)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getBookName());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setDate(5, new java.sql.Date(book.getPublishDate().getTime()));
            pstmt.setInt(6, book.getCategoryId());
            pstmt.setInt(7, book.getStock());
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public boolean borrowBook(Integer bookId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            // 仅查询库存，不执行UPDATE（扣减由触发器做）
            String checkSql = "SELECT stock FROM book WHERE book_id=? AND stock>0";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();

            // 有库存返回true，无库存返回false
            return rs.next();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
    }

    public boolean returnBook(Integer bookId) throws SQLException {
        // 无需执行UPDATE，触发器会在borrow表更新return_time时自动加库存
        // 这里仅返回true，保证Servlet逻辑能正常执行
        return true;
    }


    public boolean updateStatus(Integer bookId, Integer status) throws SQLException {
        String sql = "UPDATE book SET status=? WHERE book_id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, status);
            pstmt.setInt(2, bookId);
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }
}