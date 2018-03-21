<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jwp"%>

<c:url var="inboxUrl" value="/messages" />
<c:url var="chatUrl" value="/chatview/" />


<script>

	function displayMessageList(messages) {
		
		if(messages.length == 0) {
			var noMessagesDiv = document.createElement('div');
			noMessagesDiv.className = "message-list-empty";
			noMessagesDiv.innerHTML = 'You currently have no unread messages.';
			
			$('#message-list-content').append(noMessagesDiv);
		}
		
		for (var i = 0; i < messages.length; i++) {
			var message = messages[i];

			var date = new Date(message.sent);
			var sentOn = "received on " + date.toLocaleString();

			var rowDiv = document.createElement('div');
			rowDiv.className = "row message-list-row";
			
			var nameDiv = document.createElement('div');
			nameDiv.className = "col-md-3";
			nameDiv.innerHTML = "From: " + message.from;
			
			var dateDiv = document.createElement('div');
			dateDiv.className = "col-md-4 message-list-date";
			dateDiv.innerHTML = sentOn;
			
			var messageLink = document.createElement('a');
			messageLink.href = "${chatUrl}" + message.fromUserId;
			messageLink.innerHTML = "read message";
			messageLink.className = "message-list-link";
			
			var messageLinkDiv =  document.createElement('div');
			messageLinkDiv.className = "col-md-5";
			messageLinkDiv.appendChild(messageLink);
			
			rowDiv.appendChild(nameDiv);
			rowDiv.appendChild(dateDiv);
			rowDiv.appendChild(messageLinkDiv);
			
			$('#message-list-content').append(rowDiv);
		}
	}

	$(function() {
		
		var csrfTokenName = $("meta[name='_csrf_header']").attr("content");
		var csrfTokenValue = $("meta[name='_csrf']").attr("content");
		

		$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
			jqXHR.setRequestHeader(csrfTokenName, csrfTokenValue);
		});
		
		var request = JSON.stringify({
			'page' : "${pageNumber}"
		});
	
		var jqXHR = $.ajax({
			method : 'POST',
			contentType : "application/json",
			dataType : "json",
			data : request,
			url : "${inboxUrl}"
		});

		jqXHR.fail(function(jqXHR, textStatus) {
			var errorDiv = document.createElement('div');
			errorDiv.className = "message-list-error";
			errorDiv.innerHTML = 'Unable to retrieve your message list. Please try again later.';
			
			$('#message-list-content').append(errorDiv);
		});

		jqXHR.done(function(messages) {
			displayMessageList(messages);
		});

	});
</script>

<div class="row">
	<div class="col-md-12">
		<h2 class="message-list-heading">Unread Messages:</h2>
	</div>
</div>

<div class="row">
	<div class="message-list-pagination col-md-12">
		<jwp:pagination url="${inboxUrl}" page="${messageList}" size="2" />
	</div>
</div>


<div class="row">
	<div id="message-list-content" class="col-md-12">
	
	
	</div>
</div>
