/****************************************************
 * 
 * This provides methods for working with browser
 * notifications, for browsers that support them.
 * 
 ****************************************************/

function MessageNotifier() {
	
	function requestNotificationPermission() {
		if (Notification.permission !== "granted") {
			Notification.requestPermission();
		}
	}

	function notifyNewMessage(title, text, url) {

		if (!Notification) {
			return;
		}

		if (Notification.permission !== "granted") {
			return;
		}

		var notification = new Notification(title, {
			body : text
		});

		notification.onclick = function() {
			window.location.href = url;
		};
	}
}

/****************************************************
 * 
 * Deals with retrieving messages, message counts, etc.
 * 
 ****************************************************/

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
	this.pingTimerId = null;
	this.keepLoggedIn = false;
	
	this.newMessageCallback = null;

	this.headers = {};
	this.headers[csrfTokenName] = csrfTokenValue;
	
	if(this.debug) {
		console.log("CSRF: ", csrfTokenName, ": ", csrfTokenValue);
	}

	var _self = this;

	$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
		jqXHR.setRequestHeader(csrfTokenName,
				csrfTokenValue);
	});
	
	/*
	 * sockjs does this anyway
	$(window).unload(function() {
		  _self.disconnect();
	});
	*/
}



ConnectionManager.prototype.toggleStayLoggedIn = function() {
	this.keepLoggedIn = this.keepLoggedIn ? false : true;
	this.doStatusCheck();
}

ConnectionManager.prototype.disconnect = function() {
	if(this.client != null) {
		
		if(this.debug != null) {
			console.log("Disconnecting from stomp websocket");
		}
		
		this.client.disconnect();
	}
}

ConnectionManager.prototype.connect = function() {
	
	this.connectionAttempts++;
	
	if(this.debug) {
		console.log("Connecting to ", this.socksEndPoint, ", connection attempt ", this.connectionAttempts);
	}
	
	if(this.connectionAttempts > this.maxRetries) {
		this.goToTimeoutUrl();
		return;
	}
	
	var wsocket = new SockJS(this.socksEndPoint);

	this.client = Stomp.over(wsocket);
	this.client.debug = null;
	
	if(this.debug == true) {
		this.client.debug = true;
	}

	var _self = this;

	this.client.connect(this.headers, 
			function() {
				_self.messageCallback(); 
			},
			
			function() {
				_self.stompErrorCallback();
			});
}

ConnectionManager.prototype.connectMessaging = function(socksEndPoint,
		stompInboundDestination) {
	
	this.socksEndPoint = socksEndPoint;
	this.stompInboundDestination = stompInboundDestination;

	this.connect();
};

/*
 * Check to see if we still have a valid session, or whether
 * we get connection refused. If the latter, take the user
 * back to the login page. Note: doing this will prolong the session
 * since we're doing a request via http(s).
 * It can be used to keep the session alive. If called after
 * a session has expired, it takes us to the specified
 * timeout page, e.g. the login page.
 */

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

	if(this.debug) {
		console.log("Session timeout is ", sessionTimeout, " seconds");
	}
	
	if (this.keepLoggedIn) {
		this.enableKeepAlivePing(sessionTimeout);
	} else {
		this.disableKeepAlivePing(sessionTimeout);
	}
}

ConnectionManager.prototype.sessionStatusCheckComplete = function(xhr) {
	if (xhr.status == 403) {
		// Connection refused. The session has probably timed out.
		
		if(this.debug) {
			console.log("Connection refused. Going to timeout url.", this.args.sessionTimeoutUrl);
		}
		
		this.goToTimeoutUrl();
	}
}

ConnectionManager.prototype.goToTimeoutUrl = function() {
	window.location = this.args.sessionTimeoutUrl;
}

ConnectionManager.prototype.sessionStatusCheckFailure = function(xhr,
		ajaxOptions, thrownError) {
	console.log(Date() + " Server uncontactable: ", xhr.status, ": ", thrownError);
}


ConnectionManager.prototype.enableKeepAlivePing = function(sessionTimeout) {

	// Keep the session alive by pinging at less than the session
	// timeout interval.
	
	var _self = this;

	if (this.pingTimerId == null && sessionTimeout >= 60) {
		var pingInterval = Math.round(sessionTimeout * 0.5) * 1000;
		
		if(this.debug) {
			console.log("Enabling keep alive ping with interval ", pingInterval);
		}

		this.pingTimerId = setInterval(function() {
			_self.doStatusCheck();
		}, pingInterval);
	}
}

ConnectionManager.prototype.disableKeepAlivePing = function(sessionTimeout) {
	
	if(this.pingTimerId != null) {
		
		if(this.debug) {
			console.log("Disabling keep alive ping");
		}
		
		clearInterval(this.pingTimerId);
		this.pingTimerId = null;
	}
	
}

ConnectionManager.prototype.stompErrorCallback = function(message) {
	
	if(this.debug) {
		console.log("Stomp connection error.", message);
	}
	
	
	// Maybe the session has timed out?
	this.doStatusCheck();
	
	// Try to reconnect to the websocket after a while.
	setTimeout(this.connect, this.reconnectInterval);
}

ConnectionManager.prototype.sendMessage = function(stompOutboundDestination,
		message) {
	
	if(this.debug) {
		console.log("Sending message to ", stompOutboundDestination, message);
	}

	this.client.send(stompOutboundDestination, this.headers, JSON
			.stringify(message));
	
	this.doStatusCheck();
}

ConnectionManager.prototype.setNewMessageCallback = function(newMessageCallback) {
	this.newMessageCallback = newMessageCallback;
}

ConnectionManager.prototype.processMessage = function(stompMessage) {
	
	var messageContent = JSON.parse(stompMessage.body);
	
	if(this.debug) {
		console.log("Received message: ", messageContent);
	}

	if(this.newMessageCallback != null) {
		this.newMessageCallback(messageContent);
	}
	
	/*
	 * if (messageContent.isReply) { ConnectionManager.notifyNewMessage("New
	 * message", "You have a new message from " + this.args.chattingWithName,
	 * this.args.notificationUrl); }
	 */
}

/*
 * This gets called when a successful websocket connection is made.
 */
ConnectionManager.prototype.messageCallback = function() {

	var _self = this;
	
	if(this.debug) {
		console.log("Subscribing to ", this.stompInboundDestination);
	}

	this.client.subscribe(this.stompInboundDestination, function(message) {
		_self.processMessage(message)
	});
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
