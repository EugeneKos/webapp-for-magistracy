<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Authorization</title>
</head>
<body>
	<h2>User Authorization</h2>
	<%
	    String error = (String) request.getAttribute("error");
	    if (error != null && error.equals("true"))
	    {
	        out.println("<h4 style=\"color:red\">Invalid login credentials. Please try again!!</h4>");
	    }
	    
	    String logout = (String) request.getAttribute("logout");
	   
	    if (logout != null && logout.equals("true"))
	    {
	        out.println("<h4 style=\"color:green\">You have logged out successfully!!</h4>");
	    }
	     
	%>
	<form action="<c:url value='login' />" method='POST'>

        <p>User Login  <input type='text' name='username' value=''></p>

        <p>User Password <input type='password' name='password' /></p>

        <input name="submit" type="submit" value="Login" />

        <input name="reset" type="reset" value="Reset" />

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

	</form>
</body>
</html>