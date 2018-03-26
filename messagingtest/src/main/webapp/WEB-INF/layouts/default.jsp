<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="google-site-verification"
	content="s433y9OaxbIh9r7Kyn87cTGsuJAkS9baXy8jyVFY7M8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">


<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<title><tiles:insertAttribute name="title" /></title>

<c:set var="contextRoot" value="${pageContext.request.contextPath}" />

<!-- Bootstrap -->
<link href="${contextRoot}/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextRoot}/css/main.css" rel="stylesheet">

<!-- This is only for javascript tagging on the profile -->
<link href="${contextRoot}/css/jquery.tagit.css" rel="stylesheet">

<script src="${contextRoot}/js/jquery-ui.min.js"></script>

<script src="${contextRoot}/js/tag-it.min.js"></script>


<link href="${contextRoot}/css/jquery.cookiebar.css" rel="stylesheet">
<script src="${contextRoot}/js/jquery.cookiebar.js"></script>

<c:url var="js" value="/js" />

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

<script src='https://www.google.com/recaptcha/api.js'></script>
</head>
<body>

	<!-- Static navbar -->
	<nav class="navbar navbar-default navbar-static-top">
	<div class="container">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="/"><img class="navbar-logo"
				src="${contextRoot}/img/navbar-logo.png" alt="OtherFreaks.com"></a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li class="active"><a href="${contextRoot}/">Home</a></li>
				<li><a href="${contextRoot}/about">About</a></li>

			</ul>
			<ul class="nav navbar-nav navbar-right">

				<sec:authorize access="!isAuthenticated()">
					<li><a href="${contextRoot}/login">Login</a></li>
					<li><a href="${contextRoot}/register">Register</a></li>
				</sec:authorize>

				<sec:authorize access="isAuthenticated()">
					<li><a class="profile" href="${contextRoot}/profile"><sec:authentication
								property="principal.firstname" />'s Profile </a></li>
					<li><a href="${contextRoot}/messages?p=1">Messages</a></li>
					<li><a href="javascript:$('#logoutForm').submit();">Logout</a></li>
				</sec:authorize>

				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">Status <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="${contextRoot}/addstatus">Add Status</a></li>
							<li><a href="${contextRoot}/viewstatus">View Status
									Updates</a></li>
						</ul></li>
				</sec:authorize>

			</ul>
		</div>
		<!--/.nav-collapse -->
	</div>
	</nav>

	<c:url var="logoutLink" value="/logout" />
	<form id="logoutForm" method="post" action="${logoutLink}">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form>

	<div class="container">
		<tiles:insertAttribute name="content" />
	</div>


	<script src="${contextRoot}/js/bootstrap.min.js"></script>

	<script>
		$(function() {

			$
					.cookieBar({
						bottom : true,
						message : 'We use cookies to enhance your experience. Use of this site constitutes agreement.',
						domain : 'otherfreaks.com',
						referrer : 'otherfreaks.com'
					});
		});
	</script>

	<script src="${contextRoot}/js/sockjs.min.js"></script>
	<script src="${contextRoot}/js/stomp.min.js"></script>
	<script src="${js}/connection-manager.js"></script>
	<tiles:insertAttribute name="chatscript" />
	<tiles:insertAttribute name="chatviewscript" ignore="true" />

	<sec:authorize access="isAuthenticated()">
		<script>
			connectionManager.connect("${webSocketEndpoint}");
		</script>
	</sec:authorize>

</body>
</html>