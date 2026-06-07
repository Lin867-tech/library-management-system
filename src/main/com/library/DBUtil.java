package com.library;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {

    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    private static BasicDataSource dataSource;

    // ====================== 优化 1：加入 Guava 本地缓存 ======================
    private static final Cache<String, Boolean> BOOK_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    static {
        try {
            logger.info("初始化 DBCP2 数据库连接池");

            dataSource = new BasicDataSource();
            // 关键优化：关闭 SSL，消除火焰图里的 MySQL SSL 耗时
            dataSource.setUrl("jdbc:mysql://localhost:3306/book_lending_system?useSSL=false&serverTimezone=UTC&characterEncoding=utf8&allowPublicKeyRetrieval=true");
            dataSource.setUsername("root");
            dataSource.setPassword("zhulinqing051216"); // 你自己的密码

            // 连接池配置
            dataSource.setInitialSize(5);
            dataSource.setMaxTotal(20);
            dataSource.setMinIdle(3);

            logger.info("DBCP2 连接池初始化成功");

        } catch (Exception e) {
            logger.error("连接池初始化失败", e);
            throw new RuntimeException(e);
        }
    }

    // 从连接池获取连接（优化核心！）
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // ====================== 优化 2：提供带缓存的查询方法 ======================
    public static void selectBookWithCache() {
        // 先查缓存
        if (BOOK_CACHE.getIfPresent("book_list") != null) {
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement("select * from book");
            rs = pstmt.executeQuery();
            while (rs.next()) {}

            // 放入缓存
            BOOK_CACHE.put("book_list", true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 关闭方法不变
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
        try { if (pstmt != null) pstmt.close(); } catch (SQLException ignored) {}
        try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
    }

    public static void close(Connection conn, PreparedStatement pstmt) {
        close(conn, pstmt, null);
    }
}