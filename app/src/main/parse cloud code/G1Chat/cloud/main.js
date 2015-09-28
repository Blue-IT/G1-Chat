
// Called when the server receives a new chat message
Parse.Cloud.afterSave("Chatroom", function(request, response) {
	console.log("A new message was received!");
	response.success();
});
