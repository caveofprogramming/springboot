<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  
  <c:url var="outboundDestination" value="/app/message/send/${chatWithUserID}" />  
  <c:url var="inboundDestination" value="/user/queue/${chatWithUserID}" />
  <c:url var="conversationAjaxUrl" value="/conversation/${chatWithUserID}" />

    <script>
    
    function newMessageCallback(message) {
    	addMessage(JSON.parse(message.body));
    }
    
    function addMessage(message) {
    	var text = message.text;
    	var isReply = message.isReply;
    	
    	var className = isReply ? 'chat-message-reply' : 'chat-message-sent';
    	
    	var div = document.createElement('div');
    	div.className = 'chat-message ' + className;
    	div.innerHTML = text;
    	
    	$('#chat-message-record').append(div);
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
	});
	
	
	function refreshMessages(messages) {
		alert("Received " + messages.length + " messages");
	}

	
	connectionManager.fetchMessages("${conversationAjaxUrl}", refreshMessages, 0);
	
    </script>
    
    
    
    
    
    
    
    
    