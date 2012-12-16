<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

	<script type="text/javascript">
	
	var TYPE_USERNAME = "Uzupełnij nazwę użytkownika.";
	var USERNAME_INVALID = "Nazwa jest za krótka, powinna składać się z co najmniej 3 znaków.";
	var USERNAME_EXISTS = "Ta nazwa użytkownika została już zarejestrowana. Wybierz inną.";
	var EMAIL_INVALID = "Adres e-mail jest niepoprawny. Zwróć uwagę, że e-mail jest opcjonalny, nie musisz go podawać.";
	var TYPE_PASSWORD = "Uzupełnij hasło.";
	var PASSWORD_SHORT = "Hasło jest za krótkie, powinno składać się z co najmniej 8 znaków.";
	var PASSWORD_INVALID = "Hasło jest niepoprawne, powinno zawierać przynajmniej jedną cyfrę.";
	var PASSWORDS_DIFF = "Wpisane hasła nie są takie same.";
	var TYPE_CAPTCHA = "Wpisz 7-znakowy kod z obrazka";
	var CAPTCHA_INVALID = "Wpisany kod jest niepoprawny.";
	var INVALID_CREDENTIALS = "Nieprawidłowa nazwa użytkownika lub hasło."
	
	function closeForms() {
		
		var element;
		
		element = document.getElementById("loginForm");
		element.style.visibility = "hidden";
		element.reset();
		
		setMessage('loginForm', null);
		
		element = document.getElementById("registerForm");
		element.style.visibility = "hidden";
		element.reset();

		setMessage('registerUsername', null);
		setMessage('registerEmail', null);
		setMessage('registerPassword', null);
		setMessage('registerPassword2', null);
		setMessage('registerCaptcha', null);
		
		element = document.getElementById("contentCover");
		element.style.visibility = "hidden";
	}
	
	function openForm(id) {
		closeForms();
		var element = document.getElementById(id);
		if (element == null) return false;
		element.style.visibility = "visible";
		element = document.getElementById("contentCover");
		element.style.visibility = "visible";
		return false;
	}
	  
	function sendJSON(url, data, type, callback) {
		var request = $.ajax({
			headers: { 
				'Accept': 'application/json',
				'Content-Type': 'application/json' 
			},
			type: type,
			url: url,
			dataType: "json",
			data: JSON.stringify(data)			
		});
		request.success(callback);		  
	}
	  	
	function checkEmail(email) {
		var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		if (!filter.test(email)) {		  		
			return false;
		}
		return true;
	}
	
	function checkPassword(password) {
		var filter = /^.*[0-9]+.*$/;
		if (!filter.test(password)) {
			return false;
		}
		return true;
	}

	function setMessage(which, message) {
		var bar = document.getElementById(which + "MessageBar");
		if (message == null) {
			bar.style.display = 'none';
			return;
		}
		bar.innerHTML = message;
		bar.style.display = 'block';	    	
	}
	
	function checkRegisterUsername() {
		var username = document.getElementById("registerUsername").value;
		if (username.length < 1) {
			setMessage('registerUsername', TYPE_USERNAME);
			return false;
		}
		setMessage('registerUsername', null);
		if (username.length < 3) {
			setMessage('registerUsername', USERNAME_INVALID);
			return false;
		}
		setMessage('registerUsername', null);
		return true;
	}
	
	function checkRegisterUsernameExists() {
		var username = document.getElementById("registerUsername").value;
		var url = "<c:url value="/json/user/isAvailable" />" + '/' + username;
		sendJSON(url, {}, 'GET', function(result) {
			if (result.status == "NOK") {
				setMessage('registerUsername', USERNAME_EXISTS);				
			} else {
				setMessage('registerUsername', null);				
			}
		});		
	}

	function checkRegisterEmail() {
		var email = document.getElementById("registerEmail").value;
		if (email.length > 0 && !checkEmail(email)) {
			setMessage('registerEmail', EMAIL_INVALID);
			return false;
		}
		setMessage('registerEmail', null);
		return true;
	}
		
	function checkRegisterPassword() {
		var password = document.getElementById("registerPassword").value;
		if (password.length < 1) {
			setMessage('registerPassword', TYPE_PASSWORD);
			return false;
		}
		if (password.length < 8) {
			setMessage('registerPassword', PASSWORD_SHORT);
			return false;
		}
		if (!checkPassword(password)) {
			setMessage('registerPassword', PASSWORD_INVALID);
			return false;
		}
		setMessage('registerPassword', null);
		return true;
	}
		
	function checkRegisterPassword2() {
		var password = document.getElementById("registerPassword").value;
		var password2 = document.getElementById("registerPassword2").value;
		if (password2.length > 0 && password != password2) {
			setMessage('registerPassword2', PASSWORDS_DIFF);
			return false;
		}
		setMessage('registerPassword2', null);
		return true;
	}
		
	function checkRegisterCaptcha() {
		var captcha = document.getElementById("registerCaptcha").value;
		if (captcha.length != 7) {
			setMessage('registerCaptcha', TYPE_CAPTCHA);
			return false;
		} 
		setMessage('registerCaptcha', null);
		return true;
	}
	
	function loadCaptcha() {
		d = new Date();
		$("#captcha").attr("src", "<c:url value="/user/getCaptcha" />?" + d.getTime());		
	}
	
	function submitLoginForm() {
		var url = "<c:url value="/json/user/login" />";
		var login = document.getElementById("loginFormUsername").value;
		var password = document.getElementById("loginFormPassword").value;
		var data = {username: login, password: password};	
		sendJSON(url, data, 'POST', function(result) { 
			 if (result.status == "OK") {
				 window.location.reload(true);					
			 } else {
				 setMessage('loginForm', INVALID_CREDENTIALS);
			 } 
	 	});
		return false;
	}
	
	function submitRegisterForm() {
		setMessage("registerUsername", null);
		setMessage("registerPassword", null);
		setMessage("registerEmail", null);
		setMessage("registerPassword", null);
		setMessage("registerPassword2", null);
		setMessage("registerCaptcha", null);
		var result = true;

		result = checkRegisterUsername() && result;
		result = checkRegisterEmail() && result;
		result = checkRegisterPassword() && result;
		result = checkRegisterPassword2() && result;
		result = checkRegisterCaptcha() && result;

		if (!result) return false;
		
		var url = "<c:url value="/json/user/register" />";
	 	var login = document.getElementById("registerUsername").value;
	 	var email = document.getElementById("registerEmail").value;
	 	var password = document.getElementById("registerPassword").value;
	 	var password2 = document.getElementById("registerPassword2").value;
	 	var captcha = document.getElementById("registerCaptcha").value;
	 	var data = { username: login, email: email, password: password, password2: password2, captchaAnswer: captcha };
		sendJSON(url, data, 'POST', function(result) {
			if (result.status == "NOK") {
				
				if (result.errorMessage == "USERNAME_INVALID") {						
					setMessage("registerUsername", USERNAME_INVALID);
				} else if (result.errorMessage == "USERNAME_EXISTS") {
					setMessage("registerUsername", USERNAME_EXISTS);
				} else if (result.errorMessage == "PASSWORD_INVALID") {
					setMessage("registerPassword", PASSWORD_INVALID);
				} else if (result.errorMessage == "PASSWORDS_DIFF") {
					setMessage("registerPassword2", PASSWORDS_DIFF);
				} else if (result.errorMessage == "EMAIL_INVALID") {
					setMessage("registerEmail", EMAIL_INVALID);
				} else if (result.errorMessage == "CAPTCHA_INVALID") {
					setMessage("registerCaptcha", CAPTCHA_INVALID);
					document.getElementById("registerCaptcha").value = '';
				}						
				loadCaptcha();
			} else {
				alert('OK');
				var url = "<c:url value="/json/user/login" />";
				var data = {username: login, password: password};	
				sendJSON(url, data, 'POST', function(result) { 
					 if (result.status == "OK") {
						 window.location = "<c:url value="/map" />";					
					 } else {
						 
					 }
					 alert(result.status); 
			 	});
				return false;					
			}
		});		 	
	 	return false;		
	}
	
	function logout() {
		var url = "<c:url value="/json/user/logout" />";	
		sendJSON(url, null, 'GET', function(result) { 
		 window.location.reload(true);					
	 	});
	}
	

	</script>