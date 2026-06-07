<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.library.model.Borrow" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
    <title>我的借阅</title>
    <style>
        table { border-collapse: collapse; width: 900px; margin: 20px auto; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        .nav { margin: 20px 0; text-align: center; }
        .error { color: red; text-align: center; }
        .overdue { color: red; font-weight: bold; }
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
        <th>借阅ID</th>
        <th>图书ID</th>
        <th>读者ID</th>
        <th>借阅时间</th>
        <th>应还时间</th>
        <th>实际归还时间</th>
        <th>逾期状态</th>
        <th>罚款金额</th>
        <th>操作</th>
    </tr>
    <%
        // 获取后台传递的借阅列表
        List<Borrow> borrowList = (List<Borrow>) request.getAttribute("borrowList");
        // 日期格式化工具
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (borrowList != null && !borrowList.isEmpty()) {
            for (Borrow borrow : borrowList) {
                // 格式化日期（避免直接toString显示乱码）
                String borrowTime = borrow.getBorrowTime() == null ? "" : sdf.format(borrow.getBorrowTime());
                String dueTime = borrow.getDueTime() == null ? "" : sdf.format(borrow.getDueTime());
                String returnTime = borrow.getReturnTime() == null ? "未归还" : sdf.format(borrow.getReturnTime());

                // 逾期状态描述
                String overdueDesc = borrow.getOverdueStatus() == 0 ? "未逾期" : "已逾期";
                // 罚款金额（默认0.00）
                BigDecimal fine = borrow.getFineAmount() == null ? new BigDecimal("0.00") : borrow.getFineAmount();
    %>
    <tr>
        <td><%= borrow.getBorrowId() %></td>
        <td><%= borrow.getBookId() %></td>
        <td><%= borrow.getReaderId() %></td>
        <td><%= borrowTime %></td>
        <td><%= dueTime %></td>
        <td><%= returnTime %></td>
        <td class="<%= borrow.getOverdueStatus() == 1 ? "overdue" : "" %>"><%= overdueDesc %></td>
        <td>¥<%= fine.setScale(2, BigDecimal.ROUND_HALF_UP) %></td>
        <td>
            <% // 仅未归还的记录显示“归还”按钮 %>
            <% if (borrow.getReturnTime() == null) { %>
            <form action="${pageContext.request.contextPath}/borrow/return" method="post" style="display: inline;">
                <input type="hidden" name="borrowId" value="<%= borrow.getBorrowId() %>">
                <input type="hidden" name="bookId" value="<%= borrow.getBookId() %>">
                <button type="submit">归还图书</button>
            </form>
            <% } else { %>
            <span style="color: #999;">已归还</span>
            <% } %>
        </td>
    </tr>
    <%
        }
    } else {
        // 无数据时显示提示
    %>
    <tr>
        <td colspan="9">暂无借阅记录</td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>