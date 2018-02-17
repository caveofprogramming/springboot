function ConnectionManager(args) {

	this.args = args;
	this.pingTimerID = null;
	this.keepLoggedIn = false;

	this.headers = {};
	this.headers[this.args.csrfTokenName] = this.args.csrfTokenValue;

	var _self = this;

	$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
		jqXHR.setRequestHeader(_self.args.csrfTokenName,
				_self.args.csrfTokenValue);
	});
}

// Static method
ConnectionManager.requestNotificationPermission = function() {
	if (Notification.permission !== "granted") {
		Notification.requestPermission();
	}
}

// Note, static method
ConnectionManager.notifyNewMessage = function(title, text, url) {

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

ConnectionManager.prototype.toggleStayLoggedIn = function() {
	this.keepLoggedIn = this.keepLoggedIn ? false : true;
	this.doStatusCheck();
}

ConnectionManager.prototype.connectChat = function() {

	var wsocket = new SockJS(this.args.socksEndPoint);

	this.client = Stomp.over(wsocket);

	this.client.debug = null;

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

	if (this.keepLoggedIn) {
		this.enableKeepAlivePing(sessionTimeout);
	} else {
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
	}
}

ConnectionManager.prototype.disableKeepAlivePing = function(sessionTimeout) {
	clearInterval(this.pingTimerID);
	this.pingTimerID = null;
}

ConnectionManager.prototype.sessionStatusCheckComplete = function(xhr) {
	if (xhr.status == 401) {
		location.reload(true);
	}
}

ConnectionManager.prototype.sessionStatusCheckFailure = function(xhr,
		ajaxOptions, thrownError) {
	console.log("Server uncontactable.", xhr, ajaxOptions, thrownError);
}

ConnectionManager.prototype.stompErrorCallback = function(message) {
	this.doStatusCheck();
}

ConnectionManager.prototype.sendMessage = function(message) {

	this.client.send(this.args.stompOutboundDestination, this.headers, JSON
			.stringify(message));

	this.doStatusCheck();
}

ConnectionManager.prototype.processMessage = function(stompMessage) {
	var messageContent = JSON.parse(stompMessage.body);

	this.args.newMessageCallback(messageContent);

	if (messageContent.isReply) {
		ConnectionManager.notifyNewMessage("New message",
				"You have a new message from " + this.args.chattingWithName,
				this.args.notificationUrl);
	}
}

ConnectionManager.prototype.messageCallback = function() {

	var _self = this;

	this.client.subscribe(this.args.stompInboundDestination, function(message) {
		_self.processMessage(message)
	});
}

ConnectionManager.prototype.fetchPreviousMessages = function(
		previousMessagesCallback, page) {
	this.fetchMessages(previousMessagesCallback, page);
}

ConnectionManager.prototype.fetchMessages = function(messageCallback, page) {

	var _self = this;

	var request = JSON.stringify({
		'page' : page,
		'chatWithUserID' : _self.args.chatWithUserID
	});

	var jqXHR = $.ajax({
		method : 'POST',
		contentType : "application/json",
		context : _self,
		dataType : "json",
		data : request,
		url : _self.args.restMessageServiceUrl
	});

	jqXHR.fail(function(jqXHR, textStatus) {
		console.log("Failed to retreive messages: ", textStatus);
	});

	jqXHR.done(function(messages) {
		messageCallback(messages);
	});
}
