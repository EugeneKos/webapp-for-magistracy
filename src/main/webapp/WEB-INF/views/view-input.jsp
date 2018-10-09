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
    <meta http-equiv="Refresh" content="2" />
    <title>View Input Data</title>
</head>
<body>
    <form action="user" method="post">
        <input type="submit" value="Back">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
    <table>
        <tr><th>Key</th><th>Value</th></tr>
        <c:forEach var="data" items="${inputData}">
            <c:forEach var="key" items="${data.keyValues.keySet()}">
                <tr>
                    <td><c:out value="${key}"/></td>
                    <td><c:out value="${data.keyValues.get(key)}"/></td>
                </tr>
            </c:forEach>
        </c:forEach>
    </table>
</body>
</html>
