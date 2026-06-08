package com.library.controller;

import com.library.dao.BookDAO;
import com.library.dao.BorrowDAO;
import com.library.model.Borrow;
import com.library.model.Reader;
import com.library.model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Date;

/**
 * 借阅/归还控制器
 */
//@WebServlet("/borrow/*")   // 保留原注释
public class BorrowServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.getWriter().println("BorrowServlet doPost called: pathInfo=" + req.getPathInfo());
        HttpSession session = req.getSession();
        Reader reader = (Reader) session.getAttribute("loginReader");
        if (reader == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String pathInfo = req.getPathInfo(); // /borrow/borrow 或 /borrow/return
        try {
            if (pathInfo == null || "/borrow".equals(pathInfo)) {
                // 借阅图书
                Integer bookId = Integer.parseInt(req.getParameter("bookId"));
                BookDAO bookDAO = new BookDAO();
                Book book = bookDAO.getById(bookId);
                if (book == null) {
                    String errorMsg = "图书不存在";
                    resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg="
                            + URLEncoder.encode(errorMsg, "UTF-8"));
                    return;
                }
                if (book.getStatus() == 2) {
                    String errorMsg = "该图书已下架，无法借阅";
                    resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg="
                            + URLEncoder.encode(errorMsg, "UTF-8"));
                    return;
                }

                boolean borrowSuccess = bookDAO.borrowBook(bookId);
                if (borrowSuccess) {
                    // 添加借阅记录
                    Borrow borrow = new Borrow();
                    borrow.setReaderId(reader.getReaderId());
                    borrow.setBookId(bookId);
                    borrow.setBorrowTime(new Date());
                    borrow.setDueTime(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)); // 30天到期
                    BorrowDAO borrowDAO = new BorrowDAO();
                    borrowDAO.add(borrow);
                    resp.sendRedirect(req.getContextPath() + "/reader/borrowList");
                    return;
                } else {
                    String errorMsg = "借阅失败：图书库存不足";
                    resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg="
                            + URLEncoder.encode(errorMsg, "UTF-8"));
                    return;
                }
            } else if ("/return".equals(pathInfo)) {
                // 归还图书
                Integer borrowId = Integer.parseInt(req.getParameter("borrowId"));
                Integer bookId = Integer.parseInt(req.getParameter("bookId"));

                BookDAO bookDAO = new BookDAO();
                boolean returnSuccess = bookDAO.returnBook(bookId);
                if (returnSuccess) {
                    BorrowDAO borrowDAO = new BorrowDAO();
                    borrowDAO.returnBook(borrowId, new Date());
                    resp.sendRedirect(req.getContextPath() + "/reader/borrowList");
                    return;
                } else {
                    String errorMsg = "归还失败：数据库异常";
                    resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg="
                            + URLEncoder.encode(errorMsg, "UTF-8"));
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String errorMsg = "操作失败：数据库异常";
            resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg="
                    + URLEncoder.encode(errorMsg, "UTF-8"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            String errorMsg = "参数错误：图书ID格式异常";
            resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg="
                    + URLEncoder.encode(errorMsg, "UTF-8"));
        }
    }

    /**
     * 查看我的借阅记录
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Reader reader = (Reader) session.getAttribute("loginReader");
        if (reader == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            BorrowDAO borrowDAO = new BorrowDAO();
            req.setAttribute("borrowList", borrowDAO.listByReaderId(reader.getReaderId()));
            req.getRequestDispatcher("/borrowList.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "获取借阅记录失败：数据库异常");
            req.getRequestDispatcher("/readerIndex.jsp").forward(req, resp);
        }
    }
}