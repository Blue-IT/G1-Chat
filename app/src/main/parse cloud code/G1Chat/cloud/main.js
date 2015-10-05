/**
 * References and guides:
 * https://parse.com/apps/quickstart#parse_push/android/native/existing
 * https://www.parse.com/docs/js/guide#push-notifications
 * http://www.androidhive.info/2015/06/android-push-notifications-using-parse-com/
*/

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

// Called when a new chat message is received
Parse.Cloud.beforeSave("ChatMessage", function(request, response) {
	var message = request.object;
	var msgChannel = message.get("channel");
	
	var Channel = Parse.Object.extend("Channel");
	var query = new Parse.Query(Channel);
	query.equalTo("name", msgChannel);
	// If there is more than 1 result we already know something is wrong
	query.limit(2);
	query.find({
		success: function(results) {
			if (results.length != 1) {
				response.error("No valid chat channel");
			}
			else {
				response.success(message);
			}
		},
		error: function(err) {
			response.error(err);
		}
	});
});

// Called when a new chat message has been approved
Parse.Cloud.afterSave("ChatMessage", function(request) {
	var message = request.object;
	var author = message.get("author");
	var content = message.get("content");
	var channel = message.get("channel");
	
	
	
    console.log("Message received from [" + author + "]: '" + content + "'");
	
	Parse.Push.send({
		// Attach chatroom id
		channels: [channel],
		data: {
			// Attach message id
			messageId: message.id
		}
	},
	{
		success: function() {
			console.log("Message sent to channel: '" + channel + "'");
		},
		// Something happened
		error: function(e) {
			console.error(e);
		}
	});
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