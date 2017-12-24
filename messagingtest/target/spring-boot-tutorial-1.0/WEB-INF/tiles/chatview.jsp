<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url var="chat" value="/chat" />
<c:url var="sendPoint" value="/app/message/send/${toUserId}" />
<c:url var="fromPoint" value="/user/queue/${toUserId}" />
<c:url var="getchat" value="/getchat/${toUserId}" />
<c:url var="thisPage" value="/chatview/${toUserId}" />



<script>

	$(requestNotificationPermission);
	
	function connectChat() {
		
	}
	
	var messages = [];
	
	var token = $("meta[name='_csrf']").attr("content");
	var headerName = $("meta[name='_csrf_header']").attr("content");
	
	var headers = {};
	headers[headerName] = token;

	var wsocket = new SockJS('${chat}');

    
	var client = Stomp.over(wsocket);
	client.debug = null;
	
	client.connect(headers, function(frame) {
		client.subscribe("${fromPoint}", function(message) {
			
			var text = JSON.parse(message.body).text;
			
			messages.push({
				'isReply' : 1,
				'text' : text
			});
			
			refreshMessages();
			
			notifyNewMessage("New message", "You have a new message from <c:out value="${chattingWithName}" />", "${thisPage}");
		});
	});

	function refreshMessages() {

		var count = $("#chat-message-record div").length;
		
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

	function sendMessage() {
		
		var text = $('#chat-message-text').val();

		messages.push({
			'isReply' : 0,
			'text' : text
		});

		client.send("${sendPoint}", {}, JSON.stringify({
			'text' : text,
		}));

		$('#chat-message-text').val("");
		$('#chat-message-text').focus();
	}
	
	function addMessagesToList(messageList) {		
		messages = messageList;
		
		refreshMessages();
	}
	
	function retrieveMessages() {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
			jqXHR.setRequestHeader(header, token);
		});
		
		$.ajax({
			  dataType: "json",
			  url: "${getchat}",
			  success: addMessagesToList
			});
	}

	$(document).ready(function() {

		$('#chat-send-button').click(function() {
			sendMessage();
			refreshMessages();
		});

		$(document).keypress(function(e) {
			if (e.which == 13) {
				sendMessage();
				refreshMessages();
				return false;
			}
		});
		
		retrieveMessages();
	});
</script>

<div class="row">
	<div class="col-md-12">
		<h2>You are chatting with <c:out value="${chattingWithName}" /></h2>
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


