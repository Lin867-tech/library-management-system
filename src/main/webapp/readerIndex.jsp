<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>读者首页</title>
  <style>
    .nav { margin: 20px 0; }
    .nav a { margin-right: 20px; }
    .error { color: red; }
  </style>
</head>
<body>
<div style="width: 800px; margin: 0 auto;">
  <h2>欢迎你，<%= session.getAttribute("loginReader") != null ? ((com.library.model.Reader)session.getAttribute("loginReader")).getName() : "" %></h2>
  <% if (request.getAttribute("errorMsg") != null) { %>
  <div class="error"><%= request.getAttribute("errorMsg") %></div>
  <% } %>

  <div class="nav">
    <a href="${pageContext.request.contextPath}/book/list">图书列表</a>
    <a href="${pageContext.request.contextPath}/reader/borrowList">我的借阅</a>
    <a href="${pageContext.request.contextPath}/reserve/list">我的预约</a>
    <a href="${pageContext.request.contextPath}/logout">退出登录</a>
  </div>
</div>
</body>
</html>