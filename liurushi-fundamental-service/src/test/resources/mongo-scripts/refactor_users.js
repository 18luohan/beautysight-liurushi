// third party login
db.users.find().forEach(function(userDoc) {
    db.users.update({ _id: userDoc._id }, {$set: {globalId: userDoc.mobile, origin: "self"}}, {multi:true});
});