// identity access
db.users.remove({});
db.devices.remove({});
db.access_tokens.remove({});

// work
db.content_sections.remove({});
db.picture_stories.remove({});
db.presentations.remove({});

// app config
db.app_config.remove({});

// request log
db.request_logs.remove({});