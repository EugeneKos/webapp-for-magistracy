<%--
  Created by IntelliJ IDEA.
  User: ED.Kosinov
  Date: 03.10.2018
  Time: 19:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Page</title>
</head>
<body>
    <form action="admin" method="post">
        <input name="command_text" type="text" size="80">
        <input type="submit" value="Execute">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>

    <%
        String resultCommand = (String) request.getAttribute("resultCommand");

        if (resultCommand != null)
        {
            out.println("<h4 style=\"color:green\">"+resultCommand+"</h4>");
        }

    %>

    <form action="main" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="submit" value="Home Page">
    </form>
    <form action="logout" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="submit" value="Logout">
    </form>
</body>
</html>
