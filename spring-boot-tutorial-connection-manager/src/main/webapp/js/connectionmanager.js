/**********************************************************************************
 *
 * Deals with retrieving messages, creating message notifications, etc
 *
 **********************************************************************************/

function ConnectionManager(webSocketEndpoint) {
	console.log("New connection manager created");
	
	var csrfTokenName = $("meta[name='_csrf_header']").attr("content");
	var csrfTokenValue = $("meta[name='_csrf']").attr("content");
	
	console.log("CSRF name", csrfTokenName);
	console.log("CSRF value", csrfTokenValue);
	
	this.webSocketEndpoint = webSocketEndpoint;
	this.client = null;
	this.headers = [];
	headers[csrfTokenName] = csrfTokenValue;
	
	this.subscriptions = [];
}

ConnectionManager.prototype.connect = function() {
	console.log("Connect method called");
	
	var wsocket = new SockJS(this.webSocketEndpoint);
	this.client = Stomp.over(wsocket);
	
	client.connect(this.headers, this.connectSuccess);
	
	for(var i=0; i<this.subscriptions.length; i++) {
		var subscription = this.subscriptions[i];
		
		var inboundDestination = subscription.inboundDestination;
		var newMessageCallback = subscription.newMessageCallback;
		
		console.log(inboundDestination, ": ", newMessageCallback);
	}
}

ConnectionManager.prototype.connectSuccess() {

	console.log("Established web socket connection");
	
	this.client.subscribe("${inboundDestination}", function(messageJson) {
		var message = JSON.parse(messageJson.body);
		
		alert(message.text);
	});

}

ConnectionManager.prototype.addSubscription = function(inboundDestination, newMessageCallback) {
	
	this.subscriptions.push({
		"inboundDestination" : inboundDestination,
		"newMessageCallback" : newMessageCallback
	});
	
	
	console.log("add subscription method called");
}
