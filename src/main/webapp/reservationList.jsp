<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.library.model.Reservation" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
    <title>我的预约</title>
    <style>
        table { border-collapse: collapse; width: 900px; margin: 20px auto; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        .nav { margin: 20px 0; text-align: center; }
        .error { color: red; text-align: center; }
        .status-pending { color: #ff9800; }    /* 待处理-橙色 */
        .status-effective { color: #4caf50; } /* 已生效-绿色 */
        .status-canceled { color: #999; }     /* 已取消-灰色 */
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
        <th>预约ID</th>
        <th>读者ID</th>
        <th>图书ID</th>
        <th>预约时间</th>
        <th>预约状态</th>
        <th>操作</th>
    </tr>
    <%
        // 获取后台传递的预约列表
        List<Reservation> reserveList = (List<Reservation>) request.getAttribute("reserveList");
        // 日期格式化工具（统一显示格式）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // 非空判断，避免空指针
        if (reserveList != null && !reserveList.isEmpty()) {
            for (Reservation reservation : reserveList) {
                // 格式化预约时间（空值处理）
                String reserveTime = reservation.getReserveTime() == null ? "" : sdf.format(reservation.getReserveTime());

                // 预约状态文字描述（匹配实体类定义：0待处理/1已生效/2已取消）
                String statusDesc = "";
                String statusClass = "";
                if (reservation.getReserveStatus() == 0) {
                    statusDesc = "待处理";
                    statusClass = "status-pending";
                } else if (reservation.getReserveStatus() == 1) {
                    statusDesc = "已生效";
                    statusClass = "status-effective";
                } else if (reservation.getReserveStatus() == 2) {
                    statusDesc = "已取消";
                    statusClass = "status-canceled";
                } else {
                    statusDesc = "未知状态";
                }
    %>
    <tr>
        <td><%= reservation.getReserveId() %></td>
        <td><%= reservation.getReaderId() %></td>
        <td><%= reservation.getBookId() %></td>
        <td><%= reserveTime %></td>
        <td class="<%= statusClass %>"><%= statusDesc %></td>
        <td>
            <%
                // 仅“待处理”状态允许取消预约
                if (reservation.getReserveStatus() == 0) {
            %>
            <form action="${pageContext.request.contextPath}/reserve/cancel" method="post" style="display: inline;">
                <input type="hidden" name="reserveId" value="<%= reservation.getReserveId() %>">
                <button type="submit" onclick="return confirm('确定取消该预约吗？')">取消预约</button>
            </form>
            <%
            } else {

            %>
            <%= statusDesc %>
            <%
                }
            %>
        </td>
    </tr>
    <%
        }
    } else {
        // 无预约记录时显示提示
    %>
    <tr>
        <td colspan="6">暂无预约记录</td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>