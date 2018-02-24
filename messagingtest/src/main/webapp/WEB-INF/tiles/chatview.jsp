<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url var="webSocketEndpoint" value="/chat" />
<c:url var="outboundDestination" value="/app/message/send/${chatWithUserID}" />
<c:url var="inboundDestination" value="/user/queue/${thisUserId}" />
<c:url var="conversationAjaxUrl" value="/conversation/${chatWithUserID}" />
<c:url var="serverPingUrl" value="/ajax/statuscheck" />
<c:url var="sessionTimeoutUrl" value="/sessiontimeout" />
<c:url var="notificationUrl" value="/chatview" />


<script>
	
	var pagesFetched = 0;

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
		var messageTypeClass = isReply ? "chat-message-reply" : "chat-message-sent";

		var date = new Date(sent);

		text = date.toLocaleString() + ":      " + text;
		
		var messageDiv = document.createElement('div');
		messageDiv.className = "chat-message " + messageTypeClass;
		messageDiv.innerHTML = text;

		if(isNew) {
			$('#chat-message-record').append(messageDiv);
		}
		else {
			$('#chat-message-record').prepend(messageDiv);
		}
		
		$('#chat-message-record').scrollTop($('#chat-message-record')[0].scrollHeight);
		
	}
	
	function sendMessage(connectionManager) {
		var text = $('#chat-message-text').val();
		
		var message = {
			'text': text			
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
		
		$('#chat-message-record').animate({scrollTop: 0}, 800);
	}

	$(document).ready(
			function() {

				sizeChatWindow();
				$(window).resize(sizeChatWindow);

				var connectionManager = new ConnectionManager({
					statusCheckUrl: "${serverPingUrl}",
					sessionTimeoutUrl: "${sessionTimeoutUrl}",
					debug: true
				});
				
				connectionManager.setNewMessageCallback(newMessageReceived);

				connectionManager.connectMessaging("${webSocketEndpoint}", "${inboundDestination}");

				$('#chat-send-button').click(function() {
					sendMessage(connectionManager);
				});

				$('#chat-message-previous').click(function() {
					connectionManager.fetchPreviousMessages("${conversationAjaxUrl}", addPreviousMessages, pagesFetched);
				});

				$(document).keypress(function(e) {
					if (e.which == 13) {
						sendMessage(connectionManager);
						return false;
					}
				});

				$('#chat-message-keep-logged-in-checkbox').click(
						$.proxy(connectionManager.toggleStayLoggedIn,
								connectionManager));

				connectionManager.fetchMessages("${conversationAjaxUrl}", refreshMessages, 0);
			});
</script>

<div class="row">
	<div class="col-md-12">
	<h2>${sessionTimeoutUrl}</h2>
		<h2>
			You are chatting with
			<c:out value="${chattingWithName}" />
		</h2>
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<div class="chat-message-keep-logged-in">
			<label for="chat-message-keep-logged-in-checkbox">Keep me logged into this chat; I'm not on a public computer: </label>
			
			<input
				id="chat-message-keep-logged-in-checkbox" type="checkbox" />
		</div>
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-default">


			<div class="panel-heading">
				<div class="panel-title">Send Message</div>
			</div>

			<div class="panel-body">

				<div id="chat-message-view">

					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />

					<div id="chat-message-previous">
						<a href="#">view older messages</a>
					</div>

					<div id="chat-message-record"></div>

					<div id="chat-message-send" class="input-group input-group-lg">

						<textarea class="form-control" id="chat-message-text"
							placeHolder="Enter message"></textarea>
						<span class="input-group-btn">
							<button id="chat-send-button" class="btn btn-primary"
								type="button">Send</button>
						</span>
					</div>
				</div>

			</div>

		</div>

	</div>
</div>


