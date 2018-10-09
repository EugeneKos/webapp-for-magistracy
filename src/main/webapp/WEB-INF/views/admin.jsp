<%@ page import="java.util.List" %><%--
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
        List<String> resultCommand = null;

        if(request.getAttribute("resultCommand") instanceof List){
            resultCommand = (List<String>) request.getAttribute("resultCommand");
        }

        if (resultCommand != null) {
            for (String message : resultCommand){
                out.println("<h4 style=\"color:green\">"+message+"</h4>");
            }
        }

    %>

    <form action="admin_operation" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="submit" value="View Operations">
    </form>

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
