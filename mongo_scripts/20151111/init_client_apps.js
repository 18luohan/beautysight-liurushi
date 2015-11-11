db.client_apps.insert({
    name: "android_playplus",
    internalVersion: 3,
    releaseVersion: "play+ V1.1",
    downloadUrl: "xxx",
    upgradePolicy: "force",
    upgradePrompt: "当前版本存在安全隐患，请尽快升级至最新版本"
});

db.client_apps.insert({
    name: "android_playplus",
    internalVersion: 4,
    releaseVersion: "play+ V1.1",
    tag: "latest",
    downloadUrl: "xxx",
    upgradePolicy: "recommend",
    upgradePrompt: "推荐更新"
});

db.client_apps.insert({
    name: "ios_playplus",
    internalVersion: 3,
    releaseVersion: "play+ V1.1",
    tag: "latest",
    downloadUrl: "xxx",
    upgradePolicy: "recommend",
    upgradePrompt: "推荐更新"
});