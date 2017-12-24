function requestNotificationPermission() {
	if (Notification.permission !== "granted") {
		Notification.requestPermission();
	}
}

function notifyNewMessage(title, text, url) {
	if (!Notification) {
		console.log("This browser does not allow notifications.");
		return;
	}

	if (Notification.permission !== "granted") {
		console.log("Notification permission not granted.");
		return;
	}

	var notification = new Notification(title, {
		body : text
	});

	notification.onclick = function() {
		window.location.href = url;
	};

}
