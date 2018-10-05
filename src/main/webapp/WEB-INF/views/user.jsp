<%@ page import="java.util.Set" %>
<%@ page import="org.eugene.webapp.core.device.Device" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>User Page</title>
</head>
<body>
<h2>Enter the data to send to the server</h2>
    <form action="user" method="post">
        Mqtt broker <input type="text" name="mqtt" size="30">
        Topic <input type="text" name="topic" size="40">
        <p>Content <input type="text" name="content" size="70"></p>
        <p><input type="submit" value="Send"></p>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
    <form action="user_device" method="post">
        Device Name <input type="text" name="deviceName" size="15">
        Command Name <input type="text" name="commandName" size="20">
        Params <input type="text" name="params" size="30">
        <p><input type="submit" value="Execute"></p>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
    <%--<table>
        <tr><th>Device Name</th><th>Description</th><th>Commands</th></tr>
        <c:forEach var="device" items="${devices}">
            <tr>
                <td><c:out value="${device.name}"/></td>
                <td><c:out value="${device.description}"/></td>
                &lt;%&ndash;<td><c:out value="${device.commands}"/></td>&ndash;%&gt;
            <c:forEach var="command" items="${device.commands}">
                <td><c:out value="${command}"/></td>
            </c:forEach>
            </tr>
        </c:forEach>
    </table>--%>

    <%
        Set<Device> devices = null;

        if(request.getAttribute("devices") instanceof Set){
            devices = (Set<Device>) request.getAttribute("devices");
        }

        if (devices != null) {
            for (Device device : devices){
                for (String deviceInfo : device.getDeviceInfo()){
                    out.println("<h4 style=\"color:black\">"+deviceInfo+"</h4>");
                }
            }
        }

    %>

    <form action="user_input_data" method="post">
        <p><input type="submit" value="View input data"></p>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
    <form action="user_view_queue" method="post">
        <input type="submit" value="View queue with converters">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
    <form action="user_view_queue_two" method="post">
        <p><input type="submit" value="View queue without converters"></p>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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
