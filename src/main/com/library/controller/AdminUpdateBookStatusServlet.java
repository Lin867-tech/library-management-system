package com.library.controller;

import com.library.dao.BookDAO;
import com.library.model.Admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class AdminUpdateBookStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("loginAdmin");
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 获取参数
        String bookIdStr = req.getParameter("bookId");
        String statusStr = req.getParameter("status");

        if (bookIdStr == null || statusStr == null) {
            resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg=参数缺失");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdStr);
            int status = Integer.parseInt(statusStr);
            // 状态只能是0或1或2
            if (status != 0 && status != 1 && status != 2) {
                resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg=状态值无效");
                return;
            }

            BookDAO bookDAO = new BookDAO();
            boolean success = bookDAO.updateStatus(bookId, status);
            if (success) {
                resp.sendRedirect(req.getContextPath() + "/book/list");
            } else {
                resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg=更新失败，图书不存在");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg=参数格式错误");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/book/list?errorMsg=数据库异常");
        }
    }
}