<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.library.model.Reader" %>
<html>
<head>
  <title>管理员-读者管理</title>
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
  <% if (request.getAttribute("errorMsg") != null) { %>
  <div class="error"><%= request.getAttribute("errorMsg") %></div>
  <% } %>
</div>

<table>
  <tr>
    <th>读者ID</th>
    <th>姓名</th>
    <th>手机号</th>
    <th>邮箱</th>
    <th>状态</th>
    <th>操作</th>
  </tr>
  <%
    List<Reader> readerList = (List<Reader>) request.getAttribute("readerList");
    if (readerList != null && !readerList.isEmpty()) {
      for (Reader reader : readerList) {
        String statusDesc = reader.getStatus() == 1 ? "正常" : "禁用";
  %>
  <tr>
    <td><%= reader.getReaderId() %></td>
    <td><%= reader.getName() %></td>
    <td><%= reader.getPhone() %></td>
    <td><%= reader.getEmail() == null ? "-" : reader.getEmail() %></td>
    <td><%= statusDesc %></td>
    <td>
      <form action="${pageContext.request.contextPath}/admin/reader/updateStatus" method="post" style="display: inline;">
        <input type="hidden" name="readerId" value="<%= reader.getReaderId() %>">
        <input type="hidden" name="status" value="<%= reader.getStatus() == 1 ? 0 : 1 %>">
        <button type="submit"><%= reader.getStatus() == 1 ? "禁用" : "启用" %></button>
      </form>
    </td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="6">暂无读者数据</td>
  </tr>
  <%
    }
  %>
</table>
</body>
</html>