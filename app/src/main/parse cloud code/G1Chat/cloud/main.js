// Called when a new chat message is received
Parse.Cloud.afterSave("Chatroom", function(request, response) {
    console.log("A new message was received!");
    response.success();
});
 
// Called when a new user is created
Parse.Cloud.beforeSave(Parse.User, function(request, response) {
	// Only modify new users
	if (request.object.isNew()) {
		// Log user name
		var name = request.object.get("name");
		console.log("User '" + name + "' was created.");
		
		// Set 'isAdmin' to default value false
		request.object.set("isAdmin", false);
	}
	
    response.success();
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