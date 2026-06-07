package com.library.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登出控制器：清空会话，跳转登录页
 */
//@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        // 清空会话中的登录信息
        session.removeAttribute("loginReader");
        session.removeAttribute("loginAdmin");
        session.invalidate(); // 销毁会话
        // 跳转登录页
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}