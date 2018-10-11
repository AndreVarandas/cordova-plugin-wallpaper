var exec = require('cordova/exec');

const PLUGIN_NAME = 'WPUtils';

exports.setImageAsWallpaper = function (arg0, success, error) {
    exec(success, error, PLUGIN_NAME, 'setImageAsWallpaper', [arg0]);
};

exports.setImageAsLockScreen = function (arg0, success, error) {
    exec(success, error, PLUGIN_NAME, 'setImageAsLockScreen', [arg0]);
};

exports.setImageAsWallpaperAndLockScreen = function (arg0, success, error) {
    exec(success, error, PLUGIN_NAME, 'setImageAsWallpaperAndLockScreen', [arg0]);
};

