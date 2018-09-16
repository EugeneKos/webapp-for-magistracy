<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: kosin
  Date: 03.09.2018
  Time: 21:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home Page</title>
</head>
<body>
    <h2>User authorization</h2>
    <spring:form method="post"  modelAttribute="userJSP" action="user_service">
        <p>User Login  <spring:input path="login"/></p>
        <p>User Password <spring:input type="password" path="password"/></p>
        <p><spring:button>Log In</spring:button></p>
    </spring:form>
</body>
</html>
