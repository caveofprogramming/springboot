<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jwp"%>

<c:url var="img" value="/img" />

<div class="row">
	<div class="col-md-12 results-noresult">
		<c:if test="${empty page.content}">
   			No results.
		</c:if>
	</div>
</div>

<c:url var="searchUrl" value="/search?s=${s}" />

<div class="row">
	<div class="col-md-12">
		<jwp:pagination url="${searchUrl}" page="${page}" size="10" />
	</div>
</div>


<c:forEach var="result" items="${page.content}">

	<c:url var="profilePhoto" value="/profilephoto/${result.userId}" />
	<c:url var="profileLink" value="/profile/${result.userId}" />
	<c:url var="chatViewLink" value="/chatview/${result.userId}" />

	<div class="row person-row">
		<div class="col-md-12">

			<div class="results-photo">
				<a href="${profileLink}"><img class="profile-image" id="profilePhotoImage"
					src="${profilePhoto}" /></a>
			</div>

			<div class="results-details">

				<div class="results-name">
					<a href="${profileLink}"><c:out value="${result.firstname}" />
						<c:out value="${result.surname}" /></a>
				</div>

				<div class="results-interests">

					<c:if test="${!empty result.interests}">
					Interested in: 
					</c:if>

					<c:forEach var="interest" items="${result.interests}"
						varStatus="status">

						<c:url var="interestLink" value="/search?s=${interest}" />
						<a href="${interestLink}"><c:out value="${interest}" /></a>

						<c:if test="${!status.last}"> | </c:if>
					</c:forEach>
				</div>

				<div class="results-message">
					<a href="${chatViewLink}"><img 
						src="${img}/message.jpg" alt="contact" />
					</a>
				</div>
			</div>



		</div>
	</div>

</c:forEach>

