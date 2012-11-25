<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
  <head>
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/style.css" />" />
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/horizontalMenu.css" />" />
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/form.css" />" />
    <script src="<c:url value="/static/jquery.js" />" ></script>
  </head>
  
  <body>
    <div id="container">  
	  <div id="menu">
	    <ul class="horizontalMenu">
	      <li class="selected"><a href="#">o projekcie</a></li>   
	      <li id="loginLink"><a href="#">zaloguj się</a></li>
	      <li id="registerLink"><a href="#">zarejestruj się</a></li>
	      <sec:authorize access="isAuthenticated()"><li><a href="#">obejrzyj mapę</a></li></sec:authorize>
	      <sec:authorize access="!isAuthenticated()"><li class="inactive">obejrzyj mapę</li></sec:authorize>
	      <li><a href="#">do pobrania</a></li>
	      <li><a href="#">opis programu</a></li>
	    </ul>	    
	  </div>
	  
	  <div id="content">
	    <p> 
		Niniejsza strona wraz z załączonym oprogramowaniem stanowi część pracy magisterskiej...
		</p>
		<p>
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin vel risus sit amet eros tempor aliquet. Maecenas semper pretium tincidunt. Donec ac lorem justo. 
		Maecenas malesuada vehicula mi, nec pretium eros dictum eu. Donec non lectus volutpat dolor ullamcorper cursus. Etiam sollicitudin, turpis eu fermentum euismod, 
		lacus leo viverra purus, ut gravida urna erat vitae lacus. Vestibulum pulvinar ultricies mi et molestie. Nullam vehicula, nulla ut pretium dapibus, enim augue tempor arcu, 
		vitae mollis libero orci vel urna. Nullam eu augue sapien. 
		</p>
		<p>
		Donec tristique pellentesque turpis, nec molestie orci aliquam vel. Ut dolor neque, tristique sed feugiat fringilla, 
		vehicula id metus. Etiam lacinia, leo eget viverra porta, sapien mi viverra nulla, eget ultrices massa erat eget massa. 
		Donec eget diam commodo est suscipit molestie. Etiam luctus quam ut sapien pretium nec tincidunt ante fermentum. Duis volutpat tincidunt orci, vitae tempor odio consectetur id.
		</p>
		<p>
		Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin vel risus sit amet eros tempor aliquet. Maecenas semper pretium tincidunt. Donec ac lorem justo. 
		Maecenas malesuada vehicula mi, nec pretium eros dictum eu. Donec non lectus volutpat dolor ullamcorper cursus. Etiam sollicitudin, turpis eu fermentum euismod, 
		lacus leo viverra purus, ut gravida urna erat vitae lacus. Vestibulum pulvinar ultricies mi et molestie. Nullam vehicula, nulla ut pretium dapibus, enim augue tempor arcu, 
		vitae mollis libero orci vel urna. Nullam eu augue sapien. 
		</p>
		<p>
		Donec tristique pellentesque turpis, nec molestie orci aliquam vel. Ut dolor neque, tristique sed feugiat fringilla, 
		vehicula id metus. Etiam lacinia, leo eget viverra porta, sapien mi viverra nulla, eget ultrices massa erat eget massa. 
		Donec eget diam commodo est suscipit molestie. Etiam luctus quam ut sapien pretium nec tincidunt ante fermentum. Duis volutpat tincidunt orci, vitae tempor odio consectetur id.
		</p>
	  </div>
	</div>

    <div id="contentCover">&nbsp;</div>
    
    <div id="formWrapper">
	  <form id="loginForm" onreset="closeForms();" action="<c:url value="/user/login" />" method="post">
	    <h1>Zaloguj się</h1>
	    <label for="username" class="firstInRow">Nazwa Użytkownika<small>Wpisz nazwę użytkownika</small></label>
	    <input id="loginFormUsername" name="username" type="text" />
	
	    <label for="password" class="firstInRow">Hasło</label>
	    <input id="loginFormPassword" name="password" type="password" />
	 
	 	<div class="messageBar" id="loginFormMessageBar">
	 	  <p>Bad credentials</p>
	 	</div>
	 
	    <div class="buttonBar">		    
	      <input type="submit" value="Zaloguj się" />
	      <input type="reset" value="Anuluj" />
	    </div>
	  </form>
      <form id="registerForm" onreset="closeForms();">
	    <h1>Zarejestruj się</h1>
	    <label for="username" class="firstInRow">Nazwa Użytkownika<small>Wpisz nazwę użytkownika</small></label>
	    <input name="username" type="text" />

	    <label for="email" class="firstInRow">E-mail<small>Adres e-mail jest opcjonalny</small></label>
	    <input name="email" type="text"/>
		
	    <label for="password" class="firstInRow">Hasło</label>
	    <input name="password" type="password" />
	  	  
	    <label for="password2" class="firstInRow">Hasło Ponownie<small>Wymagane, aby sprawdzić, czy nie pomyliłes się przy wpisywaniu</small></label>
	    <input name="password2" type="password" />
		
		<label class="firstInRow"></label>
		<img  id="captcha" class="captcha" src="<c:url value="/user/getCaptcha" />" />
        <label class="firstInRow">Captcha<small>Przepisz kod z obrazka powyżej</small></label>
		<input name="captcha" type="text"/>

	    <div class="buttonBar">
	      <input type="submit" value="Zarejestruj się" />
	      <input id="loginFormReset" type="reset" value="Anuluj" />		  
	    </div>
	  </form>	  
	</div>	
	
	<script type="text/javascript">
	  
	  function closeForms() {
		  var element;
		  element = document.getElementById("loginForm");
		  element.style.visibility = "hidden";
		  element = document.getElementById("registerForm");
		  element.style.visibility = "hidden";
		  element = document.getElementById("contentCover");
		  element.style.visibility = "hidden";
	  }
	
	  function openForm(id) {
		  closeForms();
		  var element = document.getElementById(id);
		  if (element == null) return;
		  element.style.visibility = "visible";
		  element = document.getElementById("contentCover");
		  element.style.visibility = "visible";
	  }
	
	  $(document).ready(function() {		  
		 $("#loginLink").click(function() {
			 openForm("loginForm");
		 });
		 $("#registerLink").click(function() {
			 d = new Date();
			 $("#captcha").attr("src", "<c:url value="/user/getCaptcha" />" + d.getTime());
			 openForm("registerForm");
		 });
		 $("#loginForm").submit(function() {
			 var url = "<c:url value="/user/login" />";
			 alert('logowanie: ' + url);			 
			 var login = document.getElementById("loginFormUsername").value;
			 var password = document.getElementById("loginFormPassword").value;
			 var request = $.ajax({
				 type: "POST",
				 url: url,
				 data: { username: login, password: password }				 
			 });
			 request.fail(function(jqXHR, textStatus) {
				  alert( "Request failed: " + textStatus + jqXHR );
			 });
			 return false;
		 });

	  });
	</script>
	
  </body>
</html>
  