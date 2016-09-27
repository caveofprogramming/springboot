<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url var="profilePhoto" value="/profilephoto" />
<c:url var="editProfileAbout" value="/edit-profile-about" />

<div class="row">

	<div class="col-md-10 col-md-offset-1">
	
	<div id="profile-photo-status"></div>

		<div class="profile-about">

			<div class="profile-image">
				<div>
					<img id="profilePhotoImage" src="${profilePhoto}" />
				</div>
				<div class="text-center">
					<a href="#" id="uploadLink">Upload photo</a>
				</div>
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

		<c:url value="/upload-profile-photo" var="uploadPhotoLink" />
		<form method="post" enctype="multipart/form-data" id="photoUploadForm"
			action="${uploadPhotoLink}">

			<input type="file" accept="image/*" name="file" id="photoFileInput"/> <input
				type="submit" value="upload" /> <input type="hidden"
				name="${_csrf.parameterName}" value="${_csrf.token}" />

		</form>

	</div>



</div>


<script>

function setUploadStatusText(text) {
	$("#profile-photo-status").text(text);
	
	window.setTimeout(function() {
		$("#profile-photo-status").text("");
	}, 2000);
}

function uploadSuccess(data) {
	
	$("#profilePhotoImage").attr("src", "${profilePhoto};t=" + new Date());
	
	$("#photoFileInput").val("");
	
	setUploadStatusText(data.message);

}

function uploadPhoto(event) {
	
	$.ajax({
		url: $(this).attr("action"),
		type: 'POST',
		data: new FormData(this),
		processData: false,
		contentType: false,
		success: uploadSuccess,
		error: function() {
			setUploadStatusText("Server unreachable");
		}
	});
	
	event.preventDefault();
}

$(document).ready(function() {
	
	
	$("#uploadLink").click(function(event) {
		event.preventDefault();
		$("#photoFileInput").trigger('click');
	});
	
	$("#photoFileInput").change(function() {
		$("#photoUploadForm").submit();
	});
	
	$("#photoUploadForm").on("submit", uploadPhoto);
});

</script>





