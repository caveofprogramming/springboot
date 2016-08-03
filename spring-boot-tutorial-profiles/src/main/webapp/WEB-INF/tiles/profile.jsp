<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url var="img" value="/img" />
<c:url var="editProfileAbout" value="/edit-profile-about" />

<div class="row">

	<div class="col-md-10 col-md-offset-1">

		<div class="profile-about">

			<div class="profile-image">
				<img src="${img}/avatar.jpg" />
			</div>


			<div class="profile-text">
				<c:choose>
					<c:when test="${profile.about == null}">
				Click 'edit' to add information about yourself to your profile
				</c:when>
					<c:otherwise>
						${profile.about}
					</c:otherwise>
				</c:choose>

			</div>
		</div>

		<div class="profile-about-edit">
			<a href="${editProfileAbout}">edit</a>
		</div>


		<p>&nbsp;</p>
		
		<c:url value="/upload-profile-photo" var="uploadPhotoLink" />
		<form method="post" enctype="multipart/form-data" action="${uploadPhotoLink}">
			
			select photo: <input type="file" accept="image/*" name="file" />
			<input type="submit" value="upload" />
			
			<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
		
		</form>

	</div>



</div>
