db.works2.find().forEach(function(workDoc) {
    db.works.update({ _id: workDoc._id }, {$set: {"title": workDoc.pictureStory.title, "subtitle": workDoc.pictureStory.subtitle}, $unset: {"pictureStory.title": "", "pictureStory.subtitle": "" }}, {multi:true});
});