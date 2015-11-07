
db.works.update({ presentPriority: 10 }, { $set: { "presentPriority": 6 } }, { multi: true });

db.works.find().forEach(function(work) {
    var weight = 1E13;
    var orderingVal = work.presentPriority * weight + work.publishedAt.getTime();
    db.works.update({ _id: work._id }, { $set: { "ordering": NumberLong(orderingVal) } }, { multi: true });
});