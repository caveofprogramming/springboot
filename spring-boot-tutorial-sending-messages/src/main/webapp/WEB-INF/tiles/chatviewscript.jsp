<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    <script>
    
	$(document).ready(function() {
		
		$(document).keypress(function(e) {
			if(e.which == 13) {
				
				alert("Enter button pressed");
				
				return false;
			}
		});
		
		
		$('#chat-send-button').click(function(){
			alert("Send button clicked");
		});
	});

    </script>