// identity access
db.createCollection("users");
db.users.createIndex( { mobile: 0 }, { unique: true } );

db.createCollection("devices");
db.devices.createIndex( { imei: 0 }, { unique: true } );

db.createCollection("access_tokens");
db.access_tokens.createIndex( { accessToken: 0 }, { unique: true } );
db.access_tokens.createIndex( { lastAccessToken: 0 }, { unique: true, sparse: true } );

// work
db.createCollection("content_sections");
db.createCollection("picture_stories");
db.createCollection("presentations");

// app config
db.createCollection("app_config");
db.app_config.createIndex( { name: 0 }, { unique: true } );

// request log
db.createCollection("request_logs");