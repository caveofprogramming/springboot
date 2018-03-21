/*******************************************************************************
 * 
 * Deals with retrieving messages, message counts, etc.
 * 
 ******************************************************************************/

function ConnectionManager(args) {

	var csrfTokenName = $("meta[name='_csrf_header']").attr("content");
	var csrfTokenValue = $("meta[name='_csrf']").attr("content");

	// How long to wait before trying to reconnect
	// after the connection is lost.
	this.reconnectInterval = 60000;

	// Maximum number of times to try connecting.
	this.maxRetries = 10;

	this.connectionAttempts = 0;

	this.args = args;
	this.debug = args != null && args.debug != null && args.debug == true;
	// this.pingTimerId = null;
	this.keepLoggedIn = false;

	//this.lastStatusCheckTime = null;

	this.headers = {};
	this.headers[csrfTokenName] = csrfTokenValue;

	if (this.debug) {
		console.log("CSRF: ", csrfTokenName, ": ", csrfTokenValue);
	}

	this.subscriptions = [];

	var _self = this;

	$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
		jqXHR.setRequestHeader(csrfTokenName, csrfTokenValue);
	});
}

ConnectionManager.prototype.requestNotificationPermission = function() {
	if ('Notification' in window) {
		if (Notification.permission !== "granted") {
			Notification.requestPermission();
		}
	}
}

ConnectionManager.prototype.messageAlert = function(title, text, url) {

	if (!'Notification' in window) {
		return;
	}

	if (Notification.permission !== "granted") {
		return;
	}

	var notification = new Notification(title, {
		body : text
	});

	notification.onclick = function() {
		//event.preventDefault();
		window.location.href = url;
	};
}

/*
ConnectionManager.prototype.toggleStayLoggedIn = function() {
	this.keepLoggedIn = this.keepLoggedIn ? false : true;
	this.doStatusCheck();
}
*/

ConnectionManager.prototype.disconnect = function() {
	if (this.client != null) {

		if (this.debug != null) {
			console.log("Disconnecting from stomp websocket");
		}

		this.client.disconnect();
	}
}

/*
 * This is the destination we subscribe to to receive new message alerts, and
 * the base url those alerts take you to when clicked.
 */
ConnectionManager.prototype.configureNotifier = function(
		notificationDestination, notificationUrl) {
	var _self = this;

	this.notificationDestination = notificationDestination;
	this.notificationUrl = notificationUrl;
}

ConnectionManager.prototype.addSubscription = function(stompInboundDestination,
		newMessageCallback) {

	this.subscriptions.push({
		'stompInboundDestination' : stompInboundDestination,
		'newMessageCallback' : newMessageCallback,
		'subscriptionId' : null,
	});
};

ConnectionManager.prototype.connect = function(socksEndPoint) {

	this.socksEndPoint = socksEndPoint;

	this.connectStomp();
};

/*
 * Connect to STOMP over the websocket, or reconnect if necessary.
 */

ConnectionManager.prototype.connectStomp = function() {

	this.connectionAttempts++;

	if (this.debug) {
		console.log("Connecting to ", this.socksEndPoint,
				", connection attempt ", this.connectionAttempts);
	}

	if (this.connectionAttempts > this.maxRetries) {
		this.goToTimeoutUrl();
		return;
	}

	var wsocket = new SockJS(this.socksEndPoint);

	this.client = Stomp.over(wsocket);
	this.client.debug = null;

	if (this.debug == true) {
		this.client.debug = true;
	}

	var _self = this;

	this.client.connect(this.headers, function() {
		_self.stompConnectionSuccessCallback();
	},

	function() {
		_self.stompErrorCallback();
	});

}

/*
 * This gets called when a successful websocket connection is made.
 */
ConnectionManager.prototype.stompConnectionSuccessCallback = function() {

	var _self = this;

	for (var i = 0; i < this.subscriptions.length; i++) {
		var stompSubscription = this.subscriptions[i];

		var stompInboundDestination = stompSubscription.stompInboundDestination;
		var newMessageCallback = stompSubscription.newMessageCallback;

		if (this.debug) {
			console.log("Subscribing to ", stompInboundDestination);
		}

		var subscriptionId = this.client.subscribe(stompInboundDestination,
				function(message) {
					_self.processMessage(newMessageCallback, message)
				});

		stompSubscription.subscriptionId = subscriptionId;

		if (this.debug) {
			console.log("Subscribed to ", stompInboundDestination, " with ID ",
					subscriptionId);
		}

		var notificationSubscriptionId = this.client.subscribe(
				this.notificationDestination, function(notificationMessage) {
					_self.processNotification(notificationMessage);
				});

		if (this.debug) {
			console.log("Subscribed to ", this.notificationDestination,
					" with ID ", notificationSubscriptionId);
		}
	}
}

ConnectionManager.prototype.processNotification = function(message) {
	if (this.debug) {
		console.log("Message alert: ", message);
	}

	var messageContent = JSON.parse(message.body);

	var title = "New message from " + messageContent.from;
	var text = messageContent.text.substring(0, 50) + "...";
	var url = this.notificationUrl + "/" + messageContent.fromUserId;

	this.messageAlert(title, text, url);
}

/*
 * Check to see if we still have a valid session, or whether we get connection
 * refused. If the latter, take the user to an appropriate page. Note: doing
 * this will prolong the session since we're doing a request via http(s). It can
 * be used to keep the session alive. If called after a session has expired, it
 * takes us to the specified timeout page, e.g. the login page.
 */

ConnectionManager.prototype.doStatusCheck = function() {

	/*
	if (this.lastStatusCheckTime != null) {
		var currentTime = new Date();
		var intervalSinceLastCheck = this.lastStatusCheckTime - currentTime;

		// intervalSinceLastCheck is in milliseconds.

		if (intervalSinceLastCheck / 1000 < 60) {
			return;
		}
	}

	this.lastStatusCheckTime = currentTime;
*/
	
	var _self = this;

	var jqXHR = $.ajax({
		context : this,
		dataType : "json",
		url : _self.args.statusCheckUrl
	});

	//jqXHR.done(this.sessionStatusCheckSuccess);
	jqXHR.fail(this.sessionStatusCheckFailure);
	jqXHR.always(this.sessionStatusCheckComplete);
}

/*
ConnectionManager.prototype.sessionStatusCheckSuccess = function(statusCheck) {
	
	sessionTimeout = statusCheck.sessionTimeout;

	if (this.debug) {
		console.log("Session timeout is ", sessionTimeout, " seconds");
	}

	if (this.keepLoggedIn) {
		this.enableKeepAlivePing(sessionTimeout);
	} else {
		this.disableKeepAlivePing(sessionTimeout);
	}
}
*/

ConnectionManager.prototype.sessionStatusCheckComplete = function(xhr) {
	if (xhr.status == 403) {
		// Connection refused. The session has probably timed out.

		if (this.debug) {
			console.log("Connection refused. Going to timeout url.",
					this.args.sessionTimeoutUrl);
		}

		this.goToTimeoutUrl();
	}
}

ConnectionManager.prototype.goToTimeoutUrl = function() {
	window.location = this.args.sessionTimeoutUrl;
}

ConnectionManager.prototype.sessionStatusCheckFailure = function(xhr,
		ajaxOptions, thrownError) {

	if (this.debug) {
		console.log(Date() + " Server uncontactable: ", xhr.status, ": ",
				thrownError);
	}
}

/*
ConnectionManager.prototype.enableKeepAlivePing = function(sessionTimeout) {

	// Keep the session alive by pinging at less than the session
	// timeout interval.

	var _self = this;

	if (this.pingTimerId == null && sessionTimeout >= 60) {
		var pingInterval = Math.round(sessionTimeout * 0.5) * 1000;

		if (this.debug) {
			console
					.log("Enabling keep alive ping with interval ",
							pingInterval);
		}

		this.pingTimerId = setInterval(function() {
			_self.doStatusCheck();
		}, pingInterval);
	}
}


ConnectionManager.prototype.disableKeepAlivePing = function(sessionTimeout) {

	if (this.pingTimerId != null) {

		if (this.debug) {
			console.log("Disabling keep alive ping");
		}

		clearInterval(this.pingTimerId);
		this.pingTimerId = null;
	}

}
*/

ConnectionManager.prototype.stompErrorCallback = function(message) {

	if (this.debug) {
		console.log("Stomp connection error.", message);
	}

	// Maybe the session has timed out?
	this.doStatusCheck();

	// Try to reconnect to the websocket after a while.
	setTimeout(this.connectStomp, this.reconnectInterval);
}

ConnectionManager.prototype.sendMessage = function(stompOutboundDestination,
		message) {

	if (this.debug) {
		console.log("Sending message to ", stompOutboundDestination, message);
	}

	this.client.send(stompOutboundDestination, this.headers, JSON
			.stringify(message));

	// Keep the session alive.
	this.doStatusCheck();
}

ConnectionManager.prototype.processMessage = function(newMessageCallback,
		stompMessage) {

	var messageContent = JSON.parse(stompMessage.body);

	if (this.debug) {
		console.log("Received message: ", messageContent);
	}

	newMessageCallback(messageContent);

	/*
	 * if (messageContent.isReply) { ConnectionManager.notifyNewMessage("New
	 * message", "You have a new message from " + this.args.chattingWithName,
	 * this.args.notificationUrl); }
	 */
}

ConnectionManager.prototype.fetchPreviousMessages = function(
		conversationAjaxUrl, previousMessagesCallback, page) {
	this.fetchMessages(conversationAjaxUrl, previousMessagesCallback, page);
}

ConnectionManager.prototype.fetchMessages = function(conversationAjaxUrl,
		messageCallback, page) {

	var _self = this;

	var request = JSON.stringify({
		'page' : page,
	});

	var jqXHR = $.ajax({
		method : 'POST',
		contentType : "application/json",
		context : _self,
		dataType : "json",
		data : request,
		url : conversationAjaxUrl
	});

	jqXHR.fail(function(jqXHR, textStatus) {
		console.log("Failed to retreive messages: ", textStatus);
	});

	jqXHR.done(function(messages) {
		messageCallback(messages);
	});
}
