
// Called when the server receives a new chat message
Parse.Cloud.afterSave("Chatroom", function(request, response) {
	console.log("A new message was received!");
	response.success();
});

// Called when a new user is created
Parse.Cloud.afterSave("User", function(request, response) {
	console.log("A new user was created!");
	response.success("User was created.");
});
