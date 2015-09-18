// Populate seeding data into app_config collection
db.app_config.remove({});
db.app_config.insert({ name: "sms_android_credential", value: {appKey: "82119a3e256e", appSecret: "3d6807122a76d2afa070a9f56ac2abd0"} });
db.app_config.insert({ name: "sms_ios_credential", value: {appKey: "82129b3c6f30", appSecret: "ae8147db0d6e4b24121508e42faf598c"} });
db.app_config.insert({ name: "sharing_h5_shots_num", value: { val: 8 } });

db.users.update({globalId:"+86-15000332279"}, {$set:{group:"professional"}})

db.works.update({}, {$set:{presentPriority:1}}, {multi:true})