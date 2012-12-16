<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

      <form id="registerForm" onreset="closeForms();">
	    <h1>Zarejestruj się</h1>
	    <label for="username" class="firstInRow">Nazwa Użytkownika<small>Wpisz nazwę użytkownika</small></label>
	    <input id="registerUsername" name="username" type="text" />
	 	<div class="messageBar" id="registerUsernameMessageBar" style="display: none;"></div>

	    <label for="email" class="firstInRow">E-mail<small>Adres e-mail jest opcjonalny</small></label>
	    <input id="registerEmail" name="email" type="text"/>
	    <div class="messageBar" id="registerEmailMessageBar" style="display: none;"></div>
		
	    <label for="password" class="firstInRow">Hasło<small>Hasło powinno składać się z co najmniej 8 znaków i zawierać 1 cyfrę</small></label>
	    <input id="registerPassword" name="password" type="password" />
	    <div class="messageBar" id="registerPasswordMessageBar" style="display: none;"></div>
	  	  
	    <label for="password2" class="firstInRow">Hasło Ponownie<small>Wymagane, aby sprawdzić, czy nie pomyliłeś się przy wpisywaniu</small></label>
	    <input id="registerPassword2" name="password2" type="password" />
	    <div class="messageBar" id="registerPassword2MessageBar" style="display: none;"></div>
		
		<label class="firstInRow"></label>
		<img  id="captcha" class="captcha" src="<c:url value="/user/getCaptcha" />" />
        <label class="firstInRow">Captcha<small>Przepisz kod z obrazka powyżej</small></label>
		<input id="registerCaptcha" name="captcha" type="text"/>
	    <div class="messageBar" id="registerCaptchaMessageBar" style="display: none;"></div>

	    <div class="buttonBar">
	      <input type="submit" value="Zarejestruj się" />
	      <input id="loginFormReset" type="reset" value="Anuluj" />		  
	    </div>
	  </form>	