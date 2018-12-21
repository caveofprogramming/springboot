/**********************************************************************************
 *
 * Deals with retrieving messages, creating message notifications, etc
 *
 **********************************************************************************/

function ConnectionManager() {
	console.log("New connection manager created");
	
	this.subscriptions = [];
}

ConnectionManager.prototype.connect = function() {
	console.log("Connect method called");
	
	for(var i=0; i<this.subscriptions.length; i++) {
		var subscription = this.subscriptions[i];
		
		var inboundDestination = subscription.inboundDestination;
		var newMessageCallback = subscription.newMessageCallback;
		
		console.log(inboundDestination, ": ", newMessageCallback);
	}
}

ConnectionManager.prototype.addSubscription = function(inboundDestination, newMessageCallback) {
	
	this.subscriptions.push({
		"inboundDestination" : inboundDestination,
		"newMessageCallback" : newMessageCallback
	});
	
	
	console.log("add subscription method called");
}
