<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加图书</title>
    <style>
        .add-box { width: 400px; margin: 50px auto; padding: 20px; border: 1px solid #ccc; }
        .error { color: red; margin: 10px 0; }
    </style>
</head>
<body>
<div class="add-box">
    <h2>添加图书</h2>
    <% if (request.getAttribute("errorMsg") != null) { %>
    <div class="error"><%= request.getAttribute("errorMsg") %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/admin/book/add" method="post">
        <div style="margin: 10px 0;">
            <label>ISBN：</label>
            <input type="text" name="isbn" required>
        </div>
        <div style="margin: 10px 0;">
            <label>图书名称：</label>
            <input type="text" name="bookName" required>
        </div>
        <div style="margin: 10px 0;">
            <label>作者：</label>
            <input type="text" name="author" required>
        </div>
        <div style="margin: 10px 0;">
            <label>出版社：</label>
            <input type="text" name="publisher" required>
        </div>
        <div style="margin: 10px 0;">
            <label>出版日期：</label>
            <input type="date" name="publishDate" required>
        </div>
        <div style="margin: 10px 0;">
            <label>分类ID：</label>
            <input type="number" name="categoryId" required>
        </div>
        <div style="margin: 10px 0;">
            <label>库存：</label>
            <input type="number" name="stock" required min="0">
        </div>
        <!-- “状态”输入框，放在“库存”下面 -->
        <div>
            <label>状态：</label>
            <select name="status" required>
                <option value="0">可借阅</option>
                <option value="1">不可借阅</option>
            </select>
        </div>
        <button type="submit">添加</button>
        <a href="${pageContext.request.contextPath}/adminBookList.jsp" style="margin-left: 10px;">返回列表</a>
    </form>
</div>
</body>
</html>