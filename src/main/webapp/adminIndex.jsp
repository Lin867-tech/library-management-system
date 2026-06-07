<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>管理员首页</title>
  <style>
    .nav { margin: 20px 0; }
    .nav a { margin-right: 20px; }
  </style>
</head>
<body>
<div style="width: 800px; margin: 0 auto;">
  <h2>管理员后台</h2>
  <div class="nav">
    <a href="${pageContext.request.contextPath}/book/list">图书管理</a>
    <a href="${pageContext.request.contextPath}/admin/reader/list">读者管理</a>
    <a href="${pageContext.request.contextPath}/logout">退出登录</a>
  </div>
</div>
</body>
</html>