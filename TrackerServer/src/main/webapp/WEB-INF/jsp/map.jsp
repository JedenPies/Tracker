<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
  <head>
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/style.css" />" />
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/horizontalMenu.css" />" />
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/form.css" />" />
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/map.css" />" />
    <script src="<c:url value="/static/jquery.js" />" ></script>
  </head>
  
  <body>
    <div id="container">  
	  <div id="menu">
	    <ul class="horizontalMenu">
	      <li><a href="<c:url value="/" />">o projekcie</a></li>   
	      <sec:authorize access="isAuthenticated()"><li class="current">obejrzyj mapę</li></sec:authorize>
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
	    <div id="map">
			
		  <div id="mapCover"></div>			
		  <div id="zoomInBtn"></div>
		  <div id="zoomOutBtn"></div>
			
		  <img class="mapElement" id="img0" />
		  <img class="mapElement" id="img1" />
		  <img class="mapElement" id="img2" />
		  <img class="mapElement" id="img3" />
		  <img class="mapElement" id="img4" />
		  <img class="mapElement" id="img5" />
		  <img class="mapElement" id="img6" />
		  
		  <img class="mapElement" id="img7" />
		  <img class="mapElement" id="img8" />
		  <img class="mapElement" id="img9" />
		  <img class="mapElement" id="img10" />
		  <img class="mapElement" id="img11" />		
		  <img class="mapElement" id="img12" />
		  <img class="mapElement" id="img13" />

		  <img class="mapElement" id="img14" />
		  <img class="mapElement" id="img15" />
		  <img class="mapElement" id="img16"/>		  
		  <img class="mapElement" id="img17" />	
		  <img class="mapElement" id="img18" />
		  <img class="mapElement" id="img19" />
		  <img class="mapElement" id="img20" />
		  
		  <img class="mapElement" id="img21" />
		  <img class="mapElement" id="img22" />
		  <img class="mapElement" id="img23" />
		  <img class="mapElement" id="img24" />		  
		  <img class="mapElement" id="img25" />
		  <img class="mapElement" id="img26" />
		  <img class="mapElement" id="img27" />
		  
		  <img class="mapElement" id="img28" />
		  <img class="mapElement" id="img29" />	
		  <img class="mapElement" id="img30" />	
		  <img class="mapElement" id="img31" />	
		  <img class="mapElement" id="img32" />	
		  <img class="mapElement" id="img33" />	
		  <img class="mapElement" id="img34" />	
		  
		  <img class="mapElement" id="img35" />	
		  <img class="mapElement" id="img36" />	
		  <img class="mapElement" id="img37" />	
		  <img class="mapElement" id="img38" />	
		  <img class="mapElement" id="img39" />	
		  <img class="mapElement" id="img40" />	
		  <img class="mapElement" id="img41" />	
		</div>	    
	  </div>
	</div>

    <div id="contentCover">&nbsp;</div>
    
    <div id="formWrapper">
	  <jsp:include page="include/loginForm.jsp"></jsp:include>
	  <jsp:include page="include/registerForm.jsp"></jsp:include>
	</div>	
	
	<jsp:include page="include/forms.js.jsp"></jsp:include>
	<jsp:include page="include/map.js.jsp"></jsp:include>
	
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
  