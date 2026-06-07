<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.library.model.Book" %>
<html>
<head>
    <title>管理员-图书管理</title>
    <style>
        table { border-collapse: collapse; width: 800px; margin: 20px auto; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        .nav { margin: 20px 0; text-align: center; }
        .error { color: red; text-align: center; }
    </style>
</head>
<body>
<div class="nav">
    <a href="${pageContext.request.contextPath}/adminIndex.jsp">返回首页</a>
    <a href="${pageContext.request.contextPath}/admin/book/add">添加图书</a>
    <% if (request.getAttribute("errorMsg") != null) { %>
    <div class="error"><%= request.getAttribute("errorMsg") %></div>
    <% } %>
</div>

<table>
    <tr>
        <th>图书ID</th>
        <th>ISBN</th>
        <th>图书名称</th>
        <th>作者</th>
        <th>出版社</th>
        <th>库存</th>
        <th>状态</th>
        <th>操作</th>
    </tr>
    <%
        List<Book> bookList = (List<Book>) request.getAttribute("bookList");
        if (bookList != null && !bookList.isEmpty()) {
            for (Book book : bookList) {
                String statusDesc = "";
                if (book.getStatus() == 0) {
                    statusDesc = "在馆";
                } else if (book.getStatus() == 1) {
                    statusDesc = "借出";
                } else if (book.getStatus() == 2) {
                    statusDesc = "下架";
                }
    %>
    <tr>
        <td><%= book.getBookId() %></td>
        <td><%= book.getIsbn() %></td>
        <td><%= book.getBookName() %></td>
        <td><%= book.getAuthor() %></td>
        <td><%= book.getPublisher() %></td>
        <td><%= book.getStock() %></td>
        <td><%= statusDesc %></td>
        <td>
            <form action="${pageContext.request.contextPath}/admin/book/updateStatus" method="post" style="display: inline;">
                <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                <select name="status" onchange="this.form.submit()">
                    <option value="0" <%= book.getStatus() == 0 ? "selected" : "" %>>在馆</option>
                    <option value="1" <%= book.getStatus() == 1 ? "selected" : "" %>>借出</option>
                    <option value="2" <%= book.getStatus() == 2 ? "selected" : "" %>>下架</option>
                </select>
            </form>
        </td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="8">暂无图书数据</td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>