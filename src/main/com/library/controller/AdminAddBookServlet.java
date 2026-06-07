package com.library.controller;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.model.Admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * 管理员添加图书控制器
 */
//@WebServlet("/admin/book/add")
public class AdminAddBookServlet extends HttpServlet {
    // 日期格式正则：yyyy-MM-dd
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    // ========== 新增：重复字符串常量定义 ==========
    private static final String ADD_BOOK_PAGE = "/addBook.jsp";
    private static final String ERROR_MSG = "errorMsg";
    private static final String LOGIN_URL = "/login";
    private static final String ADMIN_BOOK_LIST = "/adminBookList.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 校验管理员登录
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("loginAdmin");
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + LOGIN_URL);
            return;
        }
        // 跳转到添加图书页（对应webapp根目录的addBook.jsp）
        req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("loginAdmin");
        if (admin == null) {
            resp.sendRedirect(req.getContextPath() + LOGIN_URL);
            return;
        }

        // ========== 第一步：获取参数 + 非空/格式校验 ==========
        String isbn = req.getParameter("isbn");
        String bookName = req.getParameter("bookName");
        String author = req.getParameter("author");
        String publisher = req.getParameter("publisher");
        String publishDateStr = req.getParameter("publishDate");
        String categoryIdStr = req.getParameter("categoryId");
        String stockStr = req.getParameter("stock");
        String statusStr = req.getParameter("status");

        // 1. 非空校验（必填字段）
        if (isbn == null || isbn.trim().isEmpty()) {
            req.setAttribute(ERROR_MSG, "ISBN不能为空");
            req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
            return;
        }
        if (bookName == null || bookName.trim().isEmpty()) {
            req.setAttribute(ERROR_MSG, "图书名称不能为空");
            req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
            return;
        }
        if (publishDateStr == null || publishDateStr.trim().isEmpty()) {
            req.setAttribute(ERROR_MSG, "出版日期不能为空");
            req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
            return;
        }

        // 2. 日期格式校验（必须是yyyy-MM-dd）
        if (!DATE_PATTERN.matcher(publishDateStr.trim()).matches()) {
            req.setAttribute(ERROR_MSG, "出版日期格式错误，需为：yyyy-MM-dd（如2025-12-17）");
            req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
            return;
        }

        // 3. 数字参数非空+格式校验
        Integer categoryId = null;
        Integer stock = null;
        Integer status = null;
        try {
            categoryId = Integer.parseInt(categoryIdStr.trim());
            stock = Integer.parseInt(stockStr.trim());
            status = Integer.parseInt(statusStr.trim());
            // 数字合法性校验（库存/状态不能为负）
            if (stock < 0) {
                req.setAttribute(ERROR_MSG, "库存不能为负数");
                req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
                return;
            }
            if (status != 0 && status != 1) {
                req.setAttribute(ERROR_MSG, "状态只能是0（可借阅）或1（不可借阅）");
                req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
                return;
            }
        } catch (NumberFormatException e) {
            req.setAttribute(ERROR_MSG, "分类ID/库存/状态必须是有效数字");
            req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
            return;
        } catch (NullPointerException e) {
            req.setAttribute(ERROR_MSG, "分类ID/库存/状态不能为空");
            req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
            return;
        }

        // ========== 第二步：封装Book对象（校验通过后） ==========
        Book book = new Book();
        book.setIsbn(isbn.trim());
        book.setBookName(bookName.trim());
        book.setAuthor(author == null ? "" : author.trim()); // 非必填字段赋空串
        book.setPublisher(publisher == null ? "" : publisher.trim());
        book.setPublishDate(Date.valueOf(publishDateStr.trim()));
        book.setCategoryId(categoryId);
        book.setStock(stock);
        book.setStatus(status);

        // ========== 第三步：数据库操作 + 全异常捕获 ==========
        try {
            BookDAO bookDAO = new BookDAO();
            boolean success = bookDAO.add(book);
            if (success) {
                resp.sendRedirect(req.getContextPath() + ADMIN_BOOK_LIST);
            } else {
                req.setAttribute(ERROR_MSG, "添加图书失败：ISBN已存在");
                req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute(ERROR_MSG, "添加图书失败：数据库异常（如ISBN重复）");
            req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            req.setAttribute(ERROR_MSG, "出版日期格式错误，需为：yyyy-MM-dd");
            req.getRequestDispatcher(ADD_BOOK_PAGE).forward(req, resp);
        }
    }
}