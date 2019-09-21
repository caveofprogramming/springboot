<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  
  <c:url var="outboundDestination" value="/app/message/send/${chatWithUserID}" />  
  <c:url var="inboundDestination" value="/user/queue/${chatWithUserID}" />
  <c:url var="conversationAjaxUrl" value="/conversation/${chatWithUserID}" />

    <script>
    
    var pagesFetched = 1;
    
    function sizeChatWindow() {
    	$('#chat-message-record').height(0);
    	
    	var documentHeight = $(document).height();
    	
    	var sendMessageHeight = $('#chat-message-send').height();
    	var messageRecordPos = $('#chat-message-record').offset().top;
    	var panelBodyPadding = 15;
    	
    	var messageRecordHeight = documentHeight - (messageRecordPos + sendMessageHeight + 2 * panelBodyPadding);
    	$('#chat-message-record').height(messageRecordHeight);
    }
    
    function newMessageCallback(message) {
    	addMessage(JSON.parse(message.body), true);
    }
    
    function addMessage(message, isNew) {
    	var text = message.text;
    	var isReply = message.isReply;
    	
    	var className = isReply ? 'chat-message-reply' : 'chat-message-sent';
    	
    	var div = document.createElement('div');
    	div.className = 'chat-message ' + className;
    	div.innerHTML = text;
    	
    	if(isNew) {
    		$('#chat-message-record').prepend(div);
    	} 
    	else {
    		$('#chat-message-record').append(div);
    	}
    	
    }
    
    connectionManager.addSubscription("${inboundDestination}", newMessageCallback);
    
    function sendMessage() {
    	
    	var text = $("#chat-message-text").val();
    	
    	var message = {
    		'text': text	
    	};
    	
    	connectionManager.send("${outboundDestination}", message);
    	
    	
    	$("#chat-message-text").val("");
    	$("#chat-message-text").focus();
    }
    
    
    function addPreviousMessages(messages) {
    	
    	for(var i=0; i<messages.length; i++) {
			addMessage(messages[i], false);
		}
    	
    	pagesFetched++;
    	
    	$('#chat-message-record').animate({scrollTop: 0}, 800);
    }
    
	$(document).ready(function() {
		
		$(document).keypress(function(e) {
			if(e.which == 13) {
				
				// Enter key pressed
				sendMessage();
				
				return false;
			}
		});
		
		$('#chat-send-button').click(function(){
			sendMessage();
		});
		
		$('#chat-older-messages').click(function() {
			
			connectionManager.fetchMessages("${conversationAjaxUrl}", addPreviousMessages, pagesFetched);
		});
		
		sizeChatWindow();
		$(window).resize(sizeChatWindow);
	});
	
	
	function refreshMessages(messages) {
		for(var i=0; i<messages.length; i++) {
			addMessage(messages[i], false);
		}
	}

	
	connectionManager.fetchMessages("${conversationAjaxUrl}", refreshMessages, 0);
	
    </script>
    
    
    
    
    
    
    
    
    