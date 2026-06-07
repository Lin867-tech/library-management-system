package com.library.controller;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.model.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 图书列表控制器：读者/管理员均可查看图书列表
 */
//@WebServlet({"/book/list", "/reader/bookList"})
public class BookListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 校验登录状态（读者/管理员均可）
        HttpSession session = req.getSession();
        Reader reader = (Reader) session.getAttribute("loginReader");
        if (reader == null && session.getAttribute("loginAdmin") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            BookDAO bookDAO = new BookDAO();
            List<Book> bookList = bookDAO.listAll();
            req.setAttribute("bookList", bookList);
            // 区分读者/管理员页面
            if (reader != null) {
                req.getRequestDispatcher("/bookList.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/adminBookList.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "获取图书列表失败：数据库异常");
            req.getRequestDispatcher("/readerIndex.jsp").forward(req, resp);
        }
    }
}