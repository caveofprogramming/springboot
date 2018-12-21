<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  
  <c:url var="outboundDestination" value="/app/message/send/${chatWithUserID}" />  
  
    <script>
    
    connectionManager.addSubscription();
    
    function sendMessage() {
    	
    	var text = $("#chat-message-text").val();
    	
    	var message = {
    		'text': text	
    	};
    	
    	client.send("${outboundDestination}", headers, JSON.stringify(message));
    	
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

    </script>