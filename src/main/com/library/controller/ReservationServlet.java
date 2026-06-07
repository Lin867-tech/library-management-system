package com.library.controller;

import com.library.dao.ReservationDAO;
import com.library.model.Reservation;
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
 * 预约控制器：预约图书/取消预约/查看预约记录
 */
//@WebServlet("/reserve/*")
public class ReservationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Reader reader = (Reader) session.getAttribute("loginReader");
        if (reader == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String pathInfo = req.getPathInfo();
        try {
            if ("/add".equals(pathInfo)) {
                // 预约图书
                Integer bookId = Integer.parseInt(req.getParameter("bookId"));
                Reservation reservation = new Reservation();
                reservation.setReaderId(reader.getReaderId());
                reservation.setBookId(bookId);

                ReservationDAO reserveDAO = new ReservationDAO();
                boolean success = reserveDAO.add(reservation);
                if (success) {
                    resp.sendRedirect(req.getContextPath() + "/reserve/list");
                } else {
                    req.setAttribute("errorMsg", "预约失败：你已预约该图书或图书可直接借阅");
                    req.getRequestDispatcher("/reader/bookList").forward(req, resp);
                }
            } else if ("/cancel".equals(pathInfo)) {
                // 取消预约
                Integer reserveId = Integer.parseInt(req.getParameter("reserveId"));
                ReservationDAO reserveDAO = new ReservationDAO();
                boolean success = reserveDAO.cancel(reserveId);
                if (success) {
                    resp.sendRedirect(req.getContextPath() + "/reserve/list");
                } else {
                    req.setAttribute("errorMsg", "取消预约失败：预约已生效或不存在");
                    req.getRequestDispatcher("/reserve/list").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "预约操作失败：数据库异常");
            req.getRequestDispatcher("/reader/bookList").forward(req, resp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "参数错误：ID格式异常");
            req.getRequestDispatcher("/reader/bookList").forward(req, resp);
        }
    }

    /**
     * 查看我的预约记录
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
            ReservationDAO reserveDAO = new ReservationDAO();
            List<Reservation> reserveList = reserveDAO.listByReaderId(reader.getReaderId());
            req.setAttribute("reserveList", reserveList);
            req.getRequestDispatcher("/reservationList.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMsg", "获取预约记录失败：数据库异常");
            req.getRequestDispatcher("/readerIndex.jsp").forward(req, resp);
        }
    }
}