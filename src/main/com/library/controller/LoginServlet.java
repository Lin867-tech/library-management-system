package com.library.controller;

import com.library.dao.AdminDAO;
import com.library.dao.ReaderDAO;
import com.library.model.Admin;
import com.library.model.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 通用登录控制器：处理读者/管理员登录
 */
//@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 跳转到登录页
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        // 获取登录参数
        String type = req.getParameter("type"); // reader/admin
        String account = req.getParameter("account");
        String password = req.getParameter("password");

        HttpSession session = req.getSession();
        try {
            if ("reader".equals(type)) {
                // 读者登录
                ReaderDAO readerDAO = new ReaderDAO();
                Reader reader = readerDAO.login(account, password);
                if (reader != null) {
                    session.setAttribute("loginReader", reader);
                    resp.sendRedirect(req.getContextPath() + "/readerIndex.jsp");
                } else {
                    req.setAttribute("errorMsg", "读者手机号/密码错误，或账号已禁用");
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                }
            } else if ("admin".equals(type)) {
                // 管理员登录
                AdminDAO adminDAO = new AdminDAO();
                Admin admin = adminDAO.login(account, password);
                if (admin != null) {
                    session.setAttribute("loginAdmin", admin);
                    resp.sendRedirect(req.getContextPath() + "/adminIndex.jsp");
                } else {
                    req.setAttribute("errorMsg", "管理员用户名/密码错误，或账号已禁用");
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "数据库异常，请联系管理员");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}