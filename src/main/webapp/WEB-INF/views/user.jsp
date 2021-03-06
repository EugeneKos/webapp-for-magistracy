<%@ page import="java.util.Set" %>
<%@ page import="org.eugene.webapp.core.model.device.Device" %>
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
        Device Name <input type="text" name="deviceName" size="15">
        Command Name <input type="text" name="commandName" size="20">
        Params <input type="text" name="params" size="30">
        <p><input type="submit" value="Execute"></p>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>

    <%
        out.println("<h3 style=\"color:black\">Devices</h3>");
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
    <form action="user_mqtt_status" method="post">
        <p><input type="submit" value="View Mqtt status"></p>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
    <form action="user_view_queue" method="post">
        <input type="submit" value="View queue with filters">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
    <form action="user_view_queue_two" method="post">
        <p><input type="submit" value="View queue without filters"></p>
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
