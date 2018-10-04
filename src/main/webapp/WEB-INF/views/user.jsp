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
    <form action="/user_send" method="post">
        Mqtt broker <input type="text" name="mqtt" size="30">
        Topic <input type="text" name="topic" size="40">
        <p>Content <input type="text" name="content" size="70"></p>
        <p><input type="submit" value="Send"></p>
    </form>
    <form action="/user_input_data" method="get">
        <p><input type="submit" value="View input data"></p>
    </form>
    <form action="/user_queue" method="get">
        <input type="submit" value="View queue with converters">
    </form>
    <form action="/user_queue_two" method="get">
        <p><input type="submit" value="View queue without converters"></p>
    </form>
    <form action="/user_exit" method="post">
        <p><input type="submit" value="Log Out"></p>
    </form>
</body>
</html>
