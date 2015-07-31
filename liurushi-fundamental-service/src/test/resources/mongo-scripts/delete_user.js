//var mongo = new Mongo();
var mongo = new Mongo("localhost:27017");
var db = mongo.getDB("shaimei");

function deleteUser(mobile) {
    var userQuery = { mobile : "" };
    userQuery.mobile = "+86-" + mobile;
    db.users.find(userQuery).forEach(function (userDoc) {
        var workQuery = {};
        workQuery.authorId = userDoc._id;
        db.picture_stories.remove(workQuery);
        db.presentations.remove(workQuery);

        var tokenQuery = {};
        tokenQuery.userId = userDoc._id;
        db.access_tokens.remove(tokenQuery);

        var deviceQuery = {};
        deviceQuery.userIds = userDoc._id;
        db.devices.remove(deviceQuery);

        db.users.remove(userQuery);
    });
}