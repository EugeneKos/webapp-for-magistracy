<%--
  Created by IntelliJ IDEA.
  User: Eugene
  Date: 16.09.2018
  Time: 1:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Control Page</title>
</head>
<body>
<form action="/admin_control" method="post">
    <input name="command_text" type="text" size="80">
        <input type="submit" value="Execute">
    </form>
    <form action="/admin_exit" method="post">
        <input type="submit" value="Log Out">
    </form>
</body>
</html>
