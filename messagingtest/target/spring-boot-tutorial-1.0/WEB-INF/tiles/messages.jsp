<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jwp"%>

<c:url var="inboxUrl" value="/messages" />

<div class="row">
	<div class="col-md-12">
		<jwp:pagination url="${inboxUrl}" page="${page}" size="10" />
	</div>
</div>

<div class="row">
	<div class="col-md-12 message-empty">
		<c:if test="${empty page.content}">
			<h2>You don't have any messages yet.</h2>
		</c:if>
	</div>
</div>

<c:forEach var="result" items="${page.content}">

	<c:url var="chatLink" value="/chatview/${result.fromUserId}" />


	<div class="row chat-message-row">
		<div class="col-md-12">

			<a href="${chatLink}"> Message from <c:out value="${result.from}"></c:out>
				on <fmt:formatDate pattern="EEEE d MMMM y 'at' h:mma"
					value="${result.date}" />
			</a>

		</div>
	</div>

</c:forEach>