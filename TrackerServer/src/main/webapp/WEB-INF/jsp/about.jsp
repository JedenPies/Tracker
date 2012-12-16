<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
  <head>
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/style.css" />" />
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/horizontalMenu.css" />" />
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/form.css" />" />
    <script src="<c:url value="/static/jquery.js" />" ></script>
    <!-- 
     -->
  </head>
  
  <body>
    <div id="container">  
	  <div id="menu">
	    <ul class="horizontalMenu">
	      <li class="current">o projekcie</li>   
	      <sec:authorize access="isAuthenticated()"><li><a href='<c:url value="/map" />'>obejrzyj mapę</a></li></sec:authorize>
	      <li><a href="<c:url value="/download" />">do pobrania</a></li>
	      <li><a href="<c:url value="/desc" />">opis programu</a></li>
		  <sec:authorize access="!isAuthenticated()">
	        <li id="loginLink" class="right"><a href="#">zaloguj się</a></li>	      
	        <li id="registerLink" class="right"><a href="#">zarejestruj się</a></li>
		  </sec:authorize>
		  <sec:authorize access="isAuthenticated()">
		  	<li class="right"><sec:authentication property="principal.username"/> (<a id="logoutLink" href="#">wyloguj się</a>)</li>	      
		  </sec:authorize>
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
	  <jsp:include page="include/loginForm.jsp"></jsp:include>
	  <jsp:include page="include/registerForm.jsp"></jsp:include>
	</div>	
	
	<jsp:include page="include/forms.js.jsp"></jsp:include>

	<script type="text/javascript">
	
	$(document).ready(function() {		  
		
		$("#loginLink").click(function() {
			return openForm("loginForm");
		});
		$("#registerLink").click(function() {
			loadCaptcha();
			return openForm("registerForm");
		});
		$("#logoutLink").click(function() {
			logout();
			return false;
		});

		$("#loginForm").submit(function() {
			return submitLoginForm();
		});
		 
		$("#registerForm").submit(function() {
			return submitRegisterForm();
		});

		$("#registerUsername").keyup(function() {
			if (checkRegisterUsername()) {
				checkRegisterUsernameExists();
			}
			
		});
		$("#registerEmail").keyup(function() {
			checkRegisterEmail();
		});
		$("#registerPassword").keyup(function() {
			checkRegisterPassword();
			checkRegisterPassword2();
		});
		$("#registerPassword2").keyup(function() {
			checkRegisterPassword();
			checkRegisterPassword2();
		});
		$("#registerCaptcha").keyup(function() {
			checkRegisterCaptcha();
		});
	});
	
	</script>
	
  </body>
</html>
  