//var mongo = new Mongo();
var mongo = new Mongo("localhost:27017");
var db = mongo.getDB("shaimei-test");

var sourceVal = "pgc";
for (var i=1; i<=1000; i++) {
    // 生成1-12的随机数表示月份
    var month = Math.floor(Math.random() * 12) + 1;
    // 为了简化逻辑，如果是二月，就将其变为三月
    if (month == 2) {
        month += 1;
    }
    if (month < 10) {
        month = "0" + month;
    }
    // 生成1-30的随机数表示具体哪天，为了简化处理，不考虑第31天
    var day = Math.floor(Math.random() * 30) + 1;
    if (day < 10) {
        day = "0" + day;
    }
    // 生成0-23的随机数表示小时
    var hour = Math.floor(Math.random() * 24);
    if (hour < 10) {
        hour = "0" + hour;
    }
    // 生成0-59的随机数表示分钟
    var minutes = Math.floor(Math.random() * 60);
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    // 生成0-59的随机数表示秒数
    var seconds = Math.floor(Math.random() * 60);
    if (seconds < 10) {
        seconds = "0" + seconds;
    }

    var docId = ObjectId();
    var dateTime = ISODate("2015-"+month+"-"+day+"T"+hour+":"+minutes+":"+seconds+".229Z");
    var pictureStoryJson = { "_id" : ObjectId("55af48f207962dbaad9fd5cf"), "title" : "最西藏的季节", "subtitle" : "聆听自然的脉动", "layout" : { "cols" : 6 }, "cover" : { "sectionId" : ObjectId("55af10547671d31cec8a3b90") }, "shots" : [ { "size" : { "rowSpan" : 5, "colSpan" : 3 }, "sectionId" : ObjectId("55af10547671d31cec8a3b97"), "order" : 1 }, { "size" : { "rowSpan" : 5, "colSpan" : 3 }, "sectionId" : ObjectId("55af10547671d31cec8a3b92"), "order" : 2 }, { "size" : { "rowSpan" : 5, "colSpan" : 6 }, "sectionId" : ObjectId("55af10547671d31cec8a3b99"), "order" : 3 }, { "size" : { "rowSpan" : 4, "colSpan" : 6 }, "sectionId" : ObjectId("55af10547671d31cec8a3b9c"), "order" : 4 }, { "size" : { "rowSpan" : 5, "colSpan" : 6 }, "sectionId" : ObjectId("55af10547671d31cec8a3b98"), "order" : 5 }, { "size" : { "rowSpan" : 2, "colSpan" : 6 }, "sectionId" : ObjectId("55af10547671d31cec8a3b93"), "order" : 6 }, { "size" : { "rowSpan" : 2, "colSpan" : 3 }, "sectionId" : ObjectId("55af10547671d31cec8a3b96"), "order" : 7 }, { "size" : { "rowSpan" : 2, "colSpan" : 3 }, "sectionId" : ObjectId("55af10547671d31cec8a3b9d"), "order" : 8 }, { "size" : { "rowSpan" : 4, "colSpan" : 3 }, "sectionId" : ObjectId("55af10547671d31cec8a3b9e"), "order" : 9 }, { "size" : { "rowSpan" : 4, "colSpan" : 3 }, "sectionId" : ObjectId("55af10547671d31cec8a3b9a"), "order" : 10 }, { "size" : { "rowSpan" : 2, "colSpan" : 6 }, "sectionId" : ObjectId("55af10547671d31cec8a3b9b"), "order" : 11 }, { "size" : { "rowSpan" : 3, "colSpan" : 2 }, "sectionId" : ObjectId("55af10547671d31cec8a3b95"), "order" : 12 }, { "size" : { "rowSpan" : 3, "colSpan" : 4 }, "sectionId" : ObjectId("55af10547671d31cec8a3b94"), "order" : 13 }, { "size" : { "rowSpan" : 3, "colSpan" : 6 }, "sectionId" : ObjectId("55af10547671d31cec8a3b91"), "order" : 14 } ], "authorId" : ObjectId("55af02a17671d31cec8a3b7d"), "publishedAt" : ISODate("2015-07-22T03:39:00.229Z"), "createdAt" : ISODate("2015-07-22T03:39:00.229Z"), "source" : "ugc" };
    pictureStoryJson._id = docId;
    pictureStoryJson.title = pictureStoryJson.title + i;
    pictureStoryJson.createdAt = dateTime;
    pictureStoryJson.publishedAt = dateTime;
    pictureStoryJson.source = sourceVal;
    db.picture_stories.insert(pictureStoryJson);

    var presentationJson = { "_id" : ObjectId("55af48f207962dbaad9fd5cf"), "slides" : [ { "sectionId" : ObjectId("55af10547671d31cec8a3b90"), "order" : 0 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b97"), "order" : 1 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b92"), "order" : 2 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b99"), "order" : 3 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b9c"), "order" : 4 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b98"), "order" : 5 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b93"), "order" : 6 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b96"), "order" : 7 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b9d"), "order" : 8 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b9e"), "order" : 9 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b9a"), "order" : 10 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b9b"), "order" : 11 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b95"), "order" : 12 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b94"), "order" : 13 }, { "sectionId" : ObjectId("55af10547671d31cec8a3b91"), "order" : 14 } ], "authorId" : ObjectId("55af02a17671d31cec8a3b7d"), "publishedAt" : ISODate("2015-07-22T03:39:00.245Z"), "createdAt" : ISODate("2015-07-22T03:39:00.245Z"), "source" : "ugc" };
    presentationJson._id = docId;
    presentationJson.createdAt = dateTime;
    presentationJson.publishedAt = dateTime;
    presentationJson.source = sourceVal;
    db.presentations.insert(presentationJson);
}