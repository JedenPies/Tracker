<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<form action="http://localhost:8080/TrackerServer/user/register" method="POST">
	Username: <input name="username" /><br />
	Password: <input type="password" name="password" /><br />
	Password2: <input type="password" name="password2" /><br />
	Captcha: <input name="captchaAnswer" /><br />
	<img alt="captcha" src="<c:url value="/captcha" />">
	<input type="submit" />	
</form>


Formularz rejestracji Hahaha