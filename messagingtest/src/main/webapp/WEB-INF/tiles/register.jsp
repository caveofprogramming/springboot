<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url var="loginUrl" value="/login" />
<c:url var="tosUrl" value="/tos" />

<div class="row">

	<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">

		<div class="login-error ">
			<form:errors path="user.*" />
		</div>


		<div class="panel panel-default">

			<div class="panel-heading">
				<div class="panel-title">Create an Account</div>
			</div>


			<div class="panel-body">
				<form:form method="post" modelAttribute="user" class="login-form">

					<div class="input-group">
						<form:input type="text" path="firstname" placeholder="First name"
							class="form-control" />
						<span class="input-group-btn" style="width: 20px;"></span>
						<form:input type="text" path="surname" placeholder="Surname"
							class="form-control" />

					</div>

					<div class="input-group">
						<form:input type="text" path="email" placeholder="Email"
							class="form-control" />
					</div>

					<div class="input-group">
						<form:input type="password" path="plainPassword"
							placeholder="Password" class="form-control" />
					</div>

					<div class="input-group">
						<form:input type="password" path="repeatPassword"
							placeholder="Repeat password" class="form-control" />
					</div>

					<div class="input-group">
						<button type="submit" class="btn-primary pull-right">Register</button>
					</div>

					<div class="input-group">
						<div class="g-recaptcha"
							data-sitekey="6Lf_-E4UAAAAAJobnzML7IVxsNtKWOgcg3vrf0kY"></div>
					</div>

				</form:form>

				<div class="register-tos">
					By registering on this site, you agree to these <a target="_blank"
						href="${tosUrl}">Terms of Service (click to open in a popup
						window)</a>
				</div>
				<div class="register-alpha">
					<p>
						This is a <strong>pre-alpha test site</strong>, email
						notifications and verification are disabled, and you are
						encouraged to use fake email addresses when registering. You may
						use the example.com domain to ensure a corresponding real email
						address does not exist; for instance, me@example.com
					</p>
				</div>
				<div class="register-suggestions">
					Please email bugs and feature suggestions to <a
						href="mailto:support@cavesupport.com?subject=Otherfeaks.com">support@cavesupport.com</a>
				</div>
			</div>
		</div>

	</div>



</div>
