package com.library.controller;

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
import java.util.List;

/**
 * 管理员管理读者控制器：查看读者列表、禁用/启用读者
 */
//@WebServlet("/admin/reader/*")
public class AdminReaderManageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 校验管理员登录
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("loginAdmin");
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            ReaderDAO readerDAO = new ReaderDAO();
            List<Reader> readerList = readerDAO.listAll();
            req.setAttribute("readerList", readerList);
            // 跳转到管理员读者列表页（对应webapp根目录的adminReaderList.jsp）
            req.getRequestDispatcher("/adminReaderList.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "获取读者列表失败：数据库异常");
            req.getRequestDispatcher("/adminIndex.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("loginAdmin");
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 获取参数：读者ID + 目标状态（0禁用/1启用）
        Integer readerId = Integer.parseInt(req.getParameter("readerId"));
        Integer status = Integer.parseInt(req.getParameter("status"));

        try {
            ReaderDAO readerDAO = new ReaderDAO();
            boolean success = readerDAO.updateStatus(readerId, status);
            if (success) {
                // 操作成功，刷新读者列表
                resp.sendRedirect(req.getContextPath() + "/admin/reader/list");
            } else {
                req.setAttribute("errorMsg", "操作失败：读者不存在");
                req.getRequestDispatcher("/admin/reader/list").forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "操作失败：数据库异常");
            req.getRequestDispatcher("/admin/reader/list").forward(req, resp);
        }
    }
}