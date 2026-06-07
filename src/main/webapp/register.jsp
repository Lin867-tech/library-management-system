<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>读者注册</title>
    <style>
        .register-box { width: 300px; margin: 50px auto; padding: 20px; border: 1px solid #ccc; }
        .error { color: red; margin: 10px 0; }
    </style>
</head>
<body>
<div class="register-box">
    <h2>读者注册</h2>
    <% if (request.getAttribute("errorMsg") != null) { %>
    <div class="error"><%= request.getAttribute("errorMsg") %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/reader/register" method="post">
        <div style="margin: 10px 0;">
            <label>姓名：</label>
            <input type="text" name="name" required>
        </div>
        <div style="margin: 10px 0;">
            <label>手机号：</label>
            <input type="text" name="phone" required>
        </div>
        <div style="margin: 10px 0;">
            <label>密码：</label>
            <input type="password" name="password" required>
        </div>
        <div style="margin: 10px 0;">
            <label>邮箱：</label>
            <input type="email" name="email">
        </div>
        <button type="submit">注册</button>
        <div style="margin-top: 10px;">
            <a href="${pageContext.request.contextPath}/login">返回登录</a>
        </div>
    </form>
</div>
</body>
</html>