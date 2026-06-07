<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.library.model.Book" %>
<html>
<head>
    <title>图书列表</title>
    <style>
        table { border-collapse: collapse; width: 800px; margin: 20px auto; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        .nav { margin: 20px 0; text-align: center; }
        .error { color: red; text-align: center; }
    </style>
</head>
<body>
<div class="nav">
    <a href="${pageContext.request.contextPath}/readerIndex.jsp">返回首页</a>
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
        <th>操作</th>
    </tr>
    <%
        List<Book> bookList = (List<Book>) request.getAttribute("bookList");
        if (bookList != null && !bookList.isEmpty()) {
            for (Book book : bookList) {
    %>
    <tr>
        <td><%= book.getBookId() %></td>
        <td><%= book.getIsbn() %></td>
        <td><%= book.getBookName() %></td>
        <td><%= book.getAuthor() %></td>
        <td><%= book.getPublisher() %></td>
        <td><%= book.getStock() %></td>
        <td>
            <% if (book.getStock() > 0) { %>
            <form action="${pageContext.request.contextPath}/borrow/borrow" method="post" style="display: inline;">
                <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                <button type="submit">借阅</button>
            </form>
            <% } %>
            <% if (book.getStock() == 0) { %>
            <form action="${pageContext.request.contextPath}/reserve/add" method="post" style="display: inline;">
                <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                <button type="submit">预约</button>
            </form>
            <% } %>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>
</body>
</html>