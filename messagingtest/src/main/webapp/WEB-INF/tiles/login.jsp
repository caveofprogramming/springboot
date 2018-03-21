<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url var="loginUrl" value="/login" />
<c:url var="registerUrl" value="/register" />


<div class="row">

	<div
		class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2 register-prompt">

		Please log in or <a href="${registerUrl}">click here to create an
			account</a>. It's free!

	</div>
</div>


<div class="row">

	<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">

		<c:if test="${param.error != null}">
			<div class="login-error">Incorrect username or password.</div>
		</c:if>

		<c:if test="${param.expired != null}">
			<div class="login-error">Your session has expired. Please log
				in again.</div>
		</c:if>

		<div class="panel panel-default">

			<div class="panel-heading">
				<div class="panel-title">User Log In</div>
			</div>


			<div class="panel-body">
				<form method="post" action="${loginUrl}" class="login-form">

					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />

					<div class="input-group">
						<input type="text" name="username" placeholder="Email address"
							class="form-control" />
					</div>

					<div class="input-group">
						<input type="password" name="password" placeholder="Password"
							class="form-control" />
					</div>

					<div class="input-group">
						<label class="checkbox-inline" for="stayloggedin"><input id="stayloggedin"
							type="checkbox" name="stayloggedin"/>Keep me logged in
							on this computer</label>

					</div>
					<div class="input-group">
						<button type="submit" class="btn-primary pull-right">Sign
							In</button>
					</div>

				</form>
			</div>
		</div>

	</div>



</div>
