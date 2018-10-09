<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %><%--
  Created by IntelliJ IDEA.
  User: ED.Kosinov
  Date: 03.10.2018
  Time: 19:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Refresh" content="2" />
    <title>Operation Page</title>
</head>
<body>
    <form action="admin" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="submit" value="Back">
    </form>

    <%
        LinkedList<String> listOperations = null;

        if(request.getAttribute("listOperation") instanceof LinkedList){
            listOperations = (LinkedList<String>) request.getAttribute("listOperation");
        }

        if (listOperations != null) {
            for (String message : listOperations){
                out.println("<h4 style=\"color:red\">"+message+"</h4>");
            }
        }

    %>

</body>
</html>
