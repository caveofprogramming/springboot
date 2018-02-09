/*
new ConnectionManager({
	socksEndPoint: socksEndPoint,
	newMessageCallback : refreshMessages,
	notificationUrl : notificationUrl,
	chattingWithName : chattingWithName,
	csrfTokenName : csrfTokenName,
	csrfTokenValue : csrfTokenValue,
	statusCheckUrl : statusCheckUrl,
	stompInboundDestination : stompInboundDestination,
	stompOutboundDestination : stompOutboundDestination,
	restMessageServiceUrl : restMessageServiceUrl
});
 */

function ConnectionManager(args) {

	this.args = args;
	this.debug = args.debug;
	this.pingTimerID = null;
	this.keepLoggedIn = false;
	this.messages = [];

	this.headers = {};
	this.headers[this.args.csrfTokenName] = this.args.csrfTokenValue;

	var _self = this;

	$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
		jqXHR.setRequestHeader(_self.args.csrfTokenName,
				_self.args.csrfTokenValue);
	});
}

ConnectionManager.prototype.requestNotificationPermission = function() {
	if (Notification.permission !== "granted") {
		Notification.requestPermission();
	}
}

ConnectionManager.prototype.notifyNewMessage = function(title, text, url) {

	if (!Notification) {
		this.log("This browser does not allow notifications.");
		return;
	}

	if (Notification.permission !== "granted") {
		this.log("Notification permission not granted.");
		return;
	}

	var notification = new Notification(title, {
		body : text
	});

	notification.onclick = function() {
		window.location.href = url;
	};

	this.log("Notification sent.");

}

ConnectionManager.prototype.log = function(text) {
	if (!this.debug) {
		return;
	}

	var containsObjects = false;

	for (var i = 0; i < arguments.length; i++) {
		var argumentType = typeof arguments[i];
		if (argumentType == 'object' || argumentType == "array") {
			containsObjects = true;
			break;
		}
	}

	if (containsObjects) {
		console
				.log("ConnectionManager ", Date(), ": ", arguments[0],
						arguments);
	} else {
		var debugMessage = $.makeArray(arguments).join(' ');
		console.log("ConnectionManager ", Date(), ": ", debugMessage);
	}
}

ConnectionManager.prototype.toggleStayLoggedIn = function() {
	this.keepLoggedIn = this.keepLoggedIn ? false : true;
	this.log("Stay logged in ", this.keepLoggedIn);
	this.doStatusCheck();
}

ConnectionManager.prototype.connectChat = function() {

	var wsocket = new SockJS(this.args.socksEndPoint);

	this.client = Stomp.over(wsocket);

	this.client.debug = null;

	if (this.debug == true) {
		this.client.debug = true;
	}

	var _self = this;

	this.client.connect(this.headers, function() {
		_self.messageCallback()
	}, function() {
		_self.stompErrorCallback()
	});

	this.doStatusCheck();
};

ConnectionManager.prototype.doStatusCheck = function() {
	var _self = this;

	var jqXHR = $.ajax({
		context : this,
		dataType : "json",
		url : _self.args.statusCheckUrl
	});

	jqXHR.done(this.sessionStatusCheckSuccess);
	jqXHR.fail(this.sessionStatusCheckFailure);
	jqXHR.always(this.sessionStatusCheckComplete);
}

ConnectionManager.prototype.sessionStatusCheckSuccess = function(statusCheck) {
	sessionTimeout = statusCheck.sessionTimeout;

	this.log("Status check: ", statusCheck, " with interval ", sessionTimeout);

	if (this.keepLoggedIn) {
		this.log("Keep alive ping set to ", sessionTimeout);
		this.enableKeepAlivePing(sessionTimeout);
	} else {
		this.log("Keep alive ping disabled.");
		this.disableKeepAlivePing(sessionTimeout);
	}

}

ConnectionManager.prototype.enableKeepAlivePing = function(sessionTimeout) {
	
	// Keep the session alive by pinging at less than the session
	// timeout interval.
	
	var _self = this;

	if (this.pingTimerID == null && sessionTimeout >= 60) {
		var pingInterval = Math.round(sessionTimeout * 0.5) * 1000;

		this.pingTimerID = setInterval(function() {
			_self.doStatusCheck();
		}, pingInterval);

		this.log("Set keep-alive ping to", pingInterval, " milliseconds");
	}
}

ConnectionManager.prototype.disableKeepAlivePing = function(sessionTimeout) {
	clearInterval(this.pingTimerID);
	this.pingTimerID = null;
}

ConnectionManager.prototype.sessionStatusCheckComplete = function(xhr) {
	if (xhr.status == 401) {
		location.reload(true);
		alert("Timeout");
		this.log("Session timeout; reloading page");
	} else {
		this.log(Date(), "Session OK; not reloading page");
	}
}

ConnectionManager.prototype.sessionStatusCheckFailure = function(xhr,
		ajaxOptions, thrownError) {
	this.log("Server uncontactable.", xhr, ajaxOptions, thrownError);
}

ConnectionManager.prototype.stompErrorCallback = function(message) {
	this.doStatusCheck();
}

ConnectionManager.prototype.sendMessage = function(text) {

	this.messages.push({
		'isReply' : 0,
		'text' : text
	});

	this.log("Sending message to destination '",
			this.args.stompOutboundDestination, "'");

	this.client.send(this.args.stompOutboundDestination, this.headers, JSON
			.stringify({
				'text' : text,
			}));

	this.args.newMessageCallback(this.messages);

	// TODO, currently no check to see if it really was sent.

	this.doStatusCheck();
}

ConnectionManager.prototype.processMessage = function(message) {
	var text = JSON.parse(message.body).text;

	this.messages.push({
		'isReply' : 1,
		'text' : text
	});

	this.log("Calling new message callback with ", this.messages);
	this.args.newMessageCallback(this.messages);

	this.notifyNewMessage("New message", "You have a new message from "
			+ this.args.chattingWithName, this.args.notificationUrl);
}

ConnectionManager.prototype.messageCallback = function() {

	this.log("Message callback");

	var _self = this;

	this.client.subscribe(this.args.stompInboundDestination, function(message) {
		_self.processMessage(message)
	});
}

ConnectionManager.prototype.retrieveMessages = function(page) {

	var _self = this;

	var jqXHR = $.ajax({
		context : this,
		dataType : "json",
		url : this.args.restMessageServiceUrl
	});

	jqXHR.done(function(messages) {
		_self.messages = messages;
		_self.log("Calling new message callback with ", this.messages);
		_self.args.newMessageCallback(messages);
	});
}

ConnectionManager.prototype.getMessageQueue = function() {
	return this.messages;
}