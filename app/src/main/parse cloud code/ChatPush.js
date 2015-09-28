Parse.Cloud.afterSave("Chatroom", function(request) {
	console.log("A new message was received!");
});