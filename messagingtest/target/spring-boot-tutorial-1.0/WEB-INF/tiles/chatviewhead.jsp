<style>

#chat-message-view {

}

#chat-message-send {
	

}


</style>

<script>

function sizeChatWindow() {

	$('#chat-message-record').height(0);
	
	var documentHeight = $(document).height();
	var sendMessageHeight = $('#chat-message-send').height();
	var messageRecordPos = $('#chat-message-record').offset().top;
	var panelBodyPadding = 15;

	console.log("resize ", documentHeight);

	var messageRecordHeight = documentHeight - (messageRecordPos + sendMessageHeight + 2*panelBodyPadding);

	
	$('#chat-message-record').height(messageRecordHeight);
}

$(sizeChatWindow);
$(window).resize(sizeChatWindow);

</script>