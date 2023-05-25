var exec = require('cordova/exec');

exports.initSDK = function(success, error, arg0) {
    exec(success, error, "BuglySdk", "initSDK", [arg0]);
};

exports.enableJSMonitor = function(success, error) {
    exec(success, error, "BuglySdk", "enableJSMonitor", []);
};

exports.setTagID = function(tarId) {
    exec(null, null, "BuglySdk", "setTagID", [tarId]);
};

exports.setUserID = function(userId) {
    exec(null, null, "BuglySdk", "setUserID", [userId]);
};

exports.setDeviceID = function(userId) {
    exec(null, null, "BuglySdk", "setDeviceID", [userId]);
};

exports.setDeviceModel = function(userId) {
    exec(null, null, "BuglySdk", "setDeviceModel", [userId]);
};

exports.putUserData = function(arg0) {
    exec(null, null, "BuglySdk", "putUserData", [arg0]);
};

exports.testJavaCrash = function() {
    exec(null, null, "BuglySdk", "testJavaCrash", []);
};

exports.testNativeCrash = function() {
    exec(null, null, "BuglySdk", "testNativeCrash", []);
};

exports.testANRCrash = function() {
    exec(null, null, "BuglySdk", "testANRCrash", []);
};

exports.reportJSException = function(msg) {
    exec(null, null, "BuglySdk", "reportJSException", [msg]);
};

