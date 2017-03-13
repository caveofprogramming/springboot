<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%----------ATTRIBUTES----------------%>

<%-- Spring Page object --%>
<%@ attribute name="page" required="true"
	type="org.springframework.data.domain.Page"%>
	
<%-- Base url of page --%>
<%@ attribute name="url" required="true"%>

<c:set var="paramListSeparator" value="${fn:contains(url,'?') ? '&': '?'}" />

<%-- Number of page numbers to display at once. --%>
<%@ attribute name="size" required="false"%>

<%----------ATTRIBUTE CALCULATIONS ----------------%>

<c:set var="size" value="${empty size ? 10: size}" />

<c:set var="block" value="${empty param.b ? 0: param.b}" />

<c:set var="startPage" value="${block * size + 1}" />
<c:set var="endPage" value="${(block + 1) * size}" />
<c:set var="endPage"
	value="${endPage > page.totalPages ? page.totalPages : endPage}" />


<%----------OUTPUT ----------------%>

<c:if test="${page.totalPages != 1}">

<div class="pagination">

	<c:if test="${block != 0}">
		<a href="${url}${paramListSeparator}b=${block-1}&p=${(block-1)*size + 1}">&lt;&lt;</a>
	</c:if>

	<c:forEach var="pageNumber" begin="${startPage}" end="${endPage}">

		<c:choose>

			<c:when test="${page.number != pageNumber - 1}">
				<a href="${url}${paramListSeparator}p=${pageNumber}&b=${block}"><c:out value="${pageNumber}" /></a>
			</c:when>

			<c:otherwise>
				<strong><c:out value="${pageNumber}" /></strong>
			</c:otherwise>

		</c:choose>



		<c:if test="${pageNumber != endPage}">
				 |
				</c:if>
	</c:forEach>

	<c:if test="${endPage != page.totalPages}">
		<a href="${url}${paramListSeparator}b=${block+1}&p=${(block+1)*size + 1}">&gt;&gt;</a>
	</c:if>
</div>

</c:if>

