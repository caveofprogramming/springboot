<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:url var="outboundDestination" value="/app/message/send/${chatWithUserID}" scope="request" />
<c:url var="inboundDestination" value="/user/queue/${thisUserId}" scope="request" />
<c:url var="conversationAjaxUrl" value="/conversation/${chatWithUserID}" scope="request" />
<c:url var="notificationUrl" value="/chatview" scope="request"/>

<script>
	var pagesFetched = 0;
	var lastMessageDate = new Date();
	lastMessageDate.setDate(lastMessageDate.getDate() + 1);

	function sizeChatWindow() {

		$('#chat-message-record').height(0);

		var documentHeight = $(document).height();
		var sendMessageHeight = $('#chat-message-send').height();
		var messageRecordPos = $('#chat-message-record').offset().top;
		var panelBodyPadding = 15;

		var messageRecordHeight = documentHeight
				- (messageRecordPos + sendMessageHeight + 2 * panelBodyPadding);

		$('#chat-message-record').height(messageRecordHeight);
	}

	function addMessage(message, isNew) {

		var isReply = message.isReply;
		var sent = message.sent;
		var text = message.text;
		var messageTypeClass = isReply ? "chat-message-reply"
				: "chat-message-sent";

		var date = new Date(sent);
		
		text = date.toLocaleString() + ":      " + text;
		
		// Set time to noon so we can compare dates
		// ignore time.
		date.setUTCHours(0, 0, 0, 0);
		
		// Check if more than 8.64e7 milliseconds (number of milliseconds in one day)
		// Have passed since the date associated with the last message we output.
		if(date < lastMessageDate) {
			var dateDiv = document.createElement('div');
			dateDiv.className = "chat-date";
			dateDiv.innerHTML = lastMessageDate.toLocaleDateString();
			lastMessageDate = date;
			lastMessageDate.setUTCHours(0, 0, 0, 0);
			
			if($('#chat-message-record').children().length > 1) {
				$('#chat-message-record').append(dateDiv);
			}
		}
		
		var messageDiv = document.createElement('div');
		messageDiv.className = "chat-message " + messageTypeClass;
		messageDiv.innerHTML = text;

		
		if (isNew) {
			$('#chat-message-record').prepend(messageDiv);
		} else {
			$('#chat-message-record').append(messageDiv);
		}
		
		$('#chat-message-record').scrollTop(
				$('#chat-message-record')[0].scrollHeight);

	}

	function sendMessage(connectionManager) {
		var text = $('#chat-message-text').val();

		var message = {
			'text' : text
		};

		connectionManager.sendMessage("${outboundDestination}", message);

		$('#chat-message-text').val("");
		$('#chat-message-text').focus();
	}

	function newMessageReceived(message) {
		addMessage(message, true);
	}

	function refreshMessages(messages) {
		for (var i = 0; i < messages.length; i++) {
			addMessage(messages[i], false);
		}

		pagesFetched++;
	}

	function addPreviousMessages(messages) {
		for (var i = 0; i < messages.length; i++) {
			addMessage(messages[i], false);
		}

		pagesFetched++;

		$('#chat-message-record').animate({
			scrollTop : 0
		}, 800);
	}

	sizeChatWindow();
	$(window).resize(sizeChatWindow);

	
	connectionManager.addSubscription("${inboundDestination}",
			newMessageReceived);

	$('#chat-send-button').click(function() {
		sendMessage(connectionManager);
	});

	$('#chat-message-previous').click(
			function() {
				connectionManager.fetchPreviousMessages(
						"${conversationAjaxUrl}", addPreviousMessages,
						pagesFetched);
			});

	$(document).keypress(function(e) {
		if (e.which == 13) {
			sendMessage(connectionManager);
			return false;
		}
	});

	/*
	$('#chat-message-keep-logged-in-checkbox').click(
			$.proxy(connectionManager.toggleStayLoggedIn, connectionManager));
*/
	connectionManager.fetchMessages("${conversationAjaxUrl}", refreshMessages,
			0);
	
</script>

