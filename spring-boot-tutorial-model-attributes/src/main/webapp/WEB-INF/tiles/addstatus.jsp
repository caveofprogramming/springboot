<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>	
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>



<div class="row">

	<div class="col-md-8 col-md-offset-2">
	
		<div class="panel panel-default">
		
			<div class="panel-heading">
				<div class="panel-title">Add a Status Update</div>
			</div>
			
			<div class="panel-body">
				
				<form:form modelAttribute="statusUpdate">
				
					<div class="form-group">
						<form:textarea path="text" name="text" rows="10" cols="50"></form:textarea>
					</div>
					
					<input type="submit" name="submit" value="Add Status" />				
				</form:form>
			
			</div>
		
		
		</div>
		
		<div class="panel panel-default">
		
			<div class="panel-heading">
				<div class="panel-title">Status update added on <fmt:formatDate pattern="EEEE d MMMM y 'at' H:mm:s" value="${latestStatusUpdate.added}" /></div>
			</div>
			
			<div class="panel-body">
				
				<c:out value="${latestStatusUpdate.text}" />
			
			</div>
		
		
		</div>
	
	
	</div>



</div>

