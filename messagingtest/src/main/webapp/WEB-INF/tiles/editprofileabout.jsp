<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url var="deleteAccount" value="/delete-account" />


<div class="row">

	<div class="col-md-8 col-md-offset-2">

		<div class="errors">
			<form:errors path="profile.*" />
		</div>

		<div class="panel panel-default">

			<div class="panel-heading">
				<div class="panel-title">Edit Your 'About' Text</div>
			</div>

			<form:form modelAttribute="profile">

				<div class="form-group">
					<form:textarea path="about" name="about" rows="10" cols="50"></form:textarea>
				</div>

				<input type="submit" name="submit" value="Save" />
			</form:form>

		</div>

	</div>

</div>

<div class="row">
	<div class="col-md-8 col-md-offset-2">
		Click here to delete your profile: <a id="delete-account" class="delete-account" href="${deleteAccount}">delete account</a>
	</div>
</div>

		
<script src='//cdn.tinymce.com/4/tinymce.min.js'></script>
<script>

	$(function() {
		$('#delete-account').click(function() {
			return confirm("Are you sure you want to delete your account? This action cannot be undone.");
		});

	});

	tinymce.init({
		selector : 'textarea'
	});
</script>

