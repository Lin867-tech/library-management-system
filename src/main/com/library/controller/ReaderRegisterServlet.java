package com.library.controller;

import com.library.dao.ReaderDAO;
import com.library.model.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 读者注册控制器
 */
//@WebServlet("/reader/register")
public class ReaderRegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 跳转到注册页
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // 获取注册参数
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        // 封装读者对象
        Reader reader = new Reader();
        reader.setName(name);
        reader.setPhone(phone);
        reader.setPassword(password); // 实际项目需加密
        reader.setEmail(email);

        try {
            ReaderDAO readerDAO = new ReaderDAO();
            boolean success = readerDAO.register(reader);
            if (success) {
                // 注册成功，跳转登录页
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                req.setAttribute("errorMsg", "注册失败，手机号已存在");
                req.getRequestDispatcher("/register.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "数据库异常，注册失败");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}