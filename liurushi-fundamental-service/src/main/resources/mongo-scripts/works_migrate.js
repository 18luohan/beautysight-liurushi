var picStoryCursor = db.picture_stories.find();
while ( picStoryCursor.hasNext() ) {
    var aPicStory = picStoryCursor.next();

    var presentQuery = {};
    presentQuery._id = aPicStory._id;
    var presentCursor = db.presentations.find(presentQuery);
    var aPresent = presentCursor.next();

    var work = {};
    work._id = aPicStory._id;
    work.authorId = aPicStory.authorId;
    work.source = aPicStory.source;
    work.createdAt = aPicStory.createdAt;
    work.publishedAt = aPicStory.publishedAt;

    aPicStory._id = null;
    aPicStory.authorId = null;
    aPicStory.source = null;
    aPicStory.publishedAt = null;
    aPicStory.createdAt = null;

    aPresent._id = null;
    aPresent.authorId = null;
    aPresent.source = null;
    aPresent.publishedAt = null;
    aPresent.createdAt = null;

    work.pictureStory = aPicStory;
    work.presentation = aPresent;

    db.works.insert(work);
}
print("Migrate picture_stories and presentations into works: OK!");

db.works.find().forEach(function(workDoc) {
    db.works.update({ _id: workDoc._id }, {$set: {"title": workDoc.pictureStory.title, "subtitle": workDoc.pictureStory.subtitle}, $unset: {"pictureStory.title": "", "pictureStory.subtitle": "" }}, {multi:true});
});

db.discarded_works.find().forEach(function(workDoc) {
    db.users.update({ _id: workDoc.authorId }, {$inc: { "stats.worksNum": -1}}, {multi:true});
});