<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

<sec:authorize access="isAuthenticated()">

	<c:url var="serverPingUrl" value="/ajax/statuscheck" scope="request" />
	<c:url var="sessionTimeoutUrl" value="/sessiontimeout" scope="request" />
	<c:url var="notificationDestination" value="/user/queue/newmessage"
		scope="request" />
	<c:url var="webSocketEndpoint" value="/chat" scope="request" />
	<c:url var="notificationUrl" value="/chatview" scope="request" />

	<script>
		var connectionManager = new ConnectionManager({
			statusCheckUrl : "${serverPingUrl}",
			sessionTimeoutUrl : "${sessionTimeoutUrl}",
			debug : true
		});

		connectionManager.requestNotificationPermission();

		connectionManager.configureNotifier("${notificationDestination}",
				"${notificationUrl}");
	</script>

</sec:authorize>

