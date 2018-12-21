/**********************************************************************************
 *
 * Deals with retrieving messages, creating message notifications, etc
 *
 **********************************************************************************/

function ConnectionManager() {
	console.log("New connection manager created");
}

ConnectionManager.prototype.connect = function() {
	console.log("Connect method called");
}

ConnectionManager.prototype.addSubscription = function() {
	console.log("add subscription method called");
}
