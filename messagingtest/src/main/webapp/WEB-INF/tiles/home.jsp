<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url var="search" value="/search" />
<c:url var="img" value="/img" />


<div class="row homepage-status-row">
	<div class="col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">

		<div class="homepage-status">${statusUpdate.text}</div>

	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<div class="homepage-logo">
			<img class="center-block"  src="${img}/logo.jpg" alt="OtherFreaks.com Logo" />
		</div>
	</div>
</div>


<div class="row">
	<div class="col-md-8 col-md-offset-2">

		<form action="${search}" method="get">
			
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