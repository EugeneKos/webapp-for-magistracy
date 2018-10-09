<%--
  Created by IntelliJ IDEA.
  User: Eugene
  Date: 16.09.2018
  Time: 20:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Mqtt Connect Status</title>
</head>
<body>
    <form action="user" method="post">
        <input type="submit" value="Back">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
    <table>
        <tr><th>Mqtt Broker</th><th>Connect Status</th></tr>
        <c:forEach var="mqttName" items="${mqttConnectsStatus.keySet()}">
            <tr>
                <td><c:out value="${mqttName}"/></td>
                <td><c:out value="${mqttConnectsStatus.get(mqttName)}"/></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
