<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jwp"%>

<c:url var="inboxUrl" value="/messages" />
<c:url var="js" value="/js" />

<div class="row">
	<div class="message-list-pagination col-md-12">
		<jwp:pagination url="${inboxUrl}" page="${messageList}" size="2" />
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<h2 class="message-list-heading">Latest Messages:</h2>
	</div>
</div>

<div class="row">
	<div class="col-md-12 message-empty">
		<c:if test="${empty messageList.content}">
			<h2>You don't have any messages yet.</h2>
		</c:if>
	</div>
</div>

<c:forEach var="result" items="${messageList.content}">

	<c:url var="chatLink" value="/chatview/${result.fromUserId}" />

	<div class="row message-list-row">
		<div class="col-md-12">
			<a href="${chatLink}"><c:out value="${result.from}" /> <span class="message-list-date">sent
					you a message on <fmt:formatDate pattern="EEEE d MMMM y 'at' h:mma"
						value="${result.sent}" />
			</span></a>
		</div>

	</div>

</c:forEach>


<script>
ConnectionManager.requestNotificationPermission();
</script>