# Cordova Plugin Wallpaper - WPUtils

[NPM Repository](https://www.npmjs.com/package/cordova-plugin-wputils)

This is a simple plugin that exposes 3 methods exclusively for android, for you to set home and/or lock screen wallpaper.

## Usage

Add the plugin. I have named it WPUtils, standing for wallpaper utils.

`cordova plugin add cordova-plugin-wputils`.

Use the global variable `WPUtils` to set:

- `WPUtils.setImageAsWallpaper(url, successFunction, errorFunction)`;
- `WPUtils.setImageAsLockScreen(url, successFunction, errorFunction)`;
- `WPUtils.setImageAsWallpaperAndLockScreen(url, successFunction, errorFunction)`;

Where the url, is the link to the image that you want to set as wallpaper, successFunction and errorFunction are the functions to run on succes or error.

---

[LICENSE - GPL-3.0 - André Varandas](LICENSE)
