<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<c:if test="${empty results}">
   	No results.
   </c:if>

<c:forEach var="result" items="${results}">
	<p>
		<strong>${result.userId} ${result.firstname}
			${result.surname}</strong>

		<c:forEach var="interest" items="${result.interests}">
			${interest} 
		</c:forEach>
	</p>
</c:forEach>

