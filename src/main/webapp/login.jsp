<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图书管理系统-登录</title>
    <style>
        .login-box { width: 300px; margin: 100px auto; padding: 20px; border: 1px solid #ccc; }
        .error { color: red; margin: 10px 0; }
    </style>
</head>
<body>
<div class="login-box">
    <h2>图书管理系统登录</h2>
    <% if (request.getAttribute("errorMsg") != null) { %>
    <div class="error"><%= request.getAttribute("errorMsg") %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div>
            <label>登录类型：</label>
            <input type="radio" name="type" value="reader" checked> 读者
            <input type="radio" name="type" value="admin"> 管理员
        </div>
        <div style="margin: 10px 0;">
            <label>账号：</label>
            <input type="text" name="account" required>
        </div>
        <div style="margin: 10px 0;">
            <label>密码：</label>
            <input type="password" name="password" required>
        </div>
        <button type="submit">登录</button>
        <div style="margin-top: 10px;">
            <a href="${pageContext.request.contextPath}/reader/register">读者注册</a>
        </div>
    </form>
</div>
</body>
</html>