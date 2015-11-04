db.works.update({}, { $rename: {"pictureStory": "story"}, $set: { "contentTypes": 3}}, {multi:true})
db.discarded_works.update({}, { $rename: {"pictureStory": "story"}, $set: { "contentTypes": 3}}, {multi:true})
db.publishing_works.update({}, { $rename: {"pictureStory": "story"}, $set: { "contentTypes": 3}}, {multi:true})
