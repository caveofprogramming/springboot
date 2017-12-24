<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url var="search" value="/search" />

<div class="row tos-row">
	<div class="col-md-8 col-md-offset-2">

		<div class="homepage-tos">
			<h3>This website is a development release, for <strong>demonstration purposes only</strong>.</h3>
			<div>
				<ul>
					<li>Cave of Programming Ltd cannot
						guarantee the security of any information you enter here.</li>
					<li>Furthermore, we will arbitrarily delete data from this
						site and may take the site offline at any time.</li>
				</ul>
			</div>

			<div>
				<h4>By using this website, you agree to the following terms.</h4>
				<ul>
					<li>This website is used entirely at your own risk.</li>
					<li>You will not enter any information that identifies you
						personally.</li>
					<li>In particular, you will not enter your real name, nor your
						real email address. A valid email address is not necessary.</li>
					<li>All information may be read by shareholders and employees
						of Cave of Programming Ltd.</li>
					<li>Any information you do enter may be released into the
						public domain. If this occurs, you will not hold Cave of
						Programming Ltd. liable.</li>
					<li>You will refrain from entering anything that does not
						comply with the laws in your local jurisdiction and you will not
						enter anything that contravenes the laws of the United Kingdom and
						laws of the United States of America.</li>
					<li>Use of this website by minors is prohibited.</li>
				</ul>
			</div>
		</div>

	</div>
</div>

<div class="row status-row">
	<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">

		<div class="homepage-status">${statusUpdate.text}</div>

	</div>
</div>


<div class="row">
	<div class="col-md-8 col-md-offset-2">

		<form action="${search}" method="post">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />

			<div class="input-group input-group-lg">

				<input type="text" class="form-control" name="s"
					placeHolder="Search Hobbies"> <span class="input-group-btn">
					<button id="search-button" class="btn btn-primary" type="submit">
						Find People</button>
				</span>
			</div>

		</form>


	</div>
</div>