<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<c:url var="chat" value="/chat" />
<c:url var="sendPoint" value="/app/message/send/${toUserId}" />
<c:url var="fromPoint" value="/user/queue/${toUserId}" />
<c:url var="getchat" value="/getchat?u=${toUserId}" />
<c:url var="thisPage" value="/chatview/${toUserId}" />
<c:url var="validSession" value="/validsession" />
<c:url var="statuscheck" value="/statuscheck" />
<c:url var="js" value="/js" />
<c:url var="notificationUrl" value="/chatview" />

<script src="${js}/chat-connection-manager.js"></script>



<script>
	var token = $("meta[name='_csrf']").attr("content");
	var headerName = $("meta[name='_csrf_header']").attr("content");

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

	function refreshMessages(messages) {

		var count = $("#chat-message-record div").length;

		console.log("Adding ", messages.length, " messages to ", count,
				" messages already there.");

		for (var i = count; i < messages.length; i++) {
			var message = messages[i];

			var isReply = message.isReply;
			var text = message.text;
			var cssClass = isReply ? "chat-message-reply" : "chat-message-sent";

			var div = $("<div>");
			div.addClass("chat-message");
			div.addClass(cssClass);
			div.append(document.createTextNode(text));

			$('#chat-message-record').prepend(div);
		}
	}

	$(document).ready(
			function() {

				sizeChatWindow();
				$(window).resize(sizeChatWindow);

				var connectionManager = new ConnectionManager({
					debug : true,
					socksEndPoint : "${chat}",
					newMessageCallback : refreshMessages,
					notificationUrl : "${notificationUrl}",
					chattingWithName : "${chattingWithName}",
					csrfTokenName : headerName,
					csrfTokenValue : token,
					statusCheckUrl : "${statuscheck}",
					stompInboundDestination : "${fromPoint}",
					stompOutboundDestination : "${sendPoint}",
					restMessageServiceUrl : "${getchat}"
				});

				connectionManager.connectChat();
				connectionManager.requestNotificationPermission();

				function sendMessage(text) {
					var text = $('#chat-message-text').val();
					connectionManager.sendMessage(text);
					$('#chat-message-text').val("");
					$('#chat-message-text').focus();
				}

				$('#chat-send-button').click(function() {
					sendMessage();
				});

				$(document).keypress(function(e) {
					if (e.which == 13) {
						sendMessage();
						return false;
					}
				});

				$('#chat-message-keep-logged-in-checkbox').click(
						$.proxy(connectionManager.toggleStayLoggedIn,
								connectionManager));

				connectionManager.retrieveMessages(0);
			});
</script>

<div class="row">
	<div class="col-md-12">
		<h2>
			You are chatting with
			<c:out value="${chattingWithName}" />
		</h2>
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<div class="chat-message-keep-logged-in">
			Keep me logged into this chat; I'm not on a public computer: <input
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
						
					<div id="chat-message-previous"><a href="#">view older messages</a></div>

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


