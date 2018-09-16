<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: kosin
  Date: 03.09.2018
  Time: 21:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Page</title>
</head>
<body>
    <h2>Administrator authorization</h2>
    <spring:form method="post"  modelAttribute="adminJSP" action="admin_service">
        <p>Password <spring:input type="password" path="password"/></p>
        <p><spring:button>Log In</spring:button></p>
    </spring:form>
</body>
</html>
