<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

	
<sec:authorize access="isAuthenticated()">

<c:url var="webSocketEndpoint" value="/chat" scope="request" />

	<script>
	
		var csrfTokenName = $("meta[name='_csrf_header']").attr("content");
		var csrfTokenValue = $("meta[name='_csrf']").attr("content");
		
		console.log("CSRF name", csrfTokenName);
		console.log("CSRF value", csrfTokenValue);
		
		var headers = [];
		headers[csrfTokenName] = csrfTokenValue;
		
		var wsocket = new SockJS("${webSocketEndpoint}");
		var client = Stomp.over(wsocket);
		
		client.connect(headers, function() {
			console.log("Established web socket connection");
		});
		
	
	</script>

</sec:authorize>