
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

// Used to delete users
Parse.Cloud.define("deleteUserWithId", function(request, response) {
    Parse.Cloud.useMasterKey();
    var userId = request.params.userId;
    var query = new Parse.Query(Parse.User);
    query.get(userId).then(function(user) {
        return user.destroy();
    }).then(function() {
        response.success("User removed");
    }, function(error) {
        response.error("Failed to remove user");
    });
});