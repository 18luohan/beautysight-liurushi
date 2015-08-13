var secCursor = db.content_sections.find();
while ( secCursor.hasNext() ) {
    var aSection = secCursor.next();

    if (aSection.type == "image") {
        var aFile = {};
        aFile._id = ObjectId();
        aFile.type = "image";
        aFile.key = aSection.resource.key;
        aFile.hash = aSection.resource.hash;
        aFile.createdAt = aSection.createdAt;
        db.file_metadata.insert(aFile);

        var secQuery = {};
        secQuery._id = aSection._id;

        db.content_sections.update(secQuery, { $unset: { resource: "", size: "" }, $set: { fileId: aFile._id } });
    }
}
print("Migrate content sections: OK!");