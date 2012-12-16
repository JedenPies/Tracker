<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

	  <form id="loginForm" onreset="closeForms();" action="<c:url value="/user/login" />" method="post">
	    <h1>Zaloguj się</h1>
	    <label for="username" class="firstInRow">Nazwa Użytkownika<small>Wpisz nazwę użytkownika</small></label>
	    <input id="loginFormUsername" name="username" type="text" />
	
	    <label for="password" class="firstInRow">Hasło</label>
	    <input id="loginFormPassword" name="password" type="password" />
	 
	 	<div class="messageBar" id="loginFormMessageBar" style="display: none;"></div>
	 
	    <div class="buttonBar">		    
	      <input type="submit" value="Zaloguj się" />
	      <input type="reset" value="Anuluj" />
	    </div>
	  </form>