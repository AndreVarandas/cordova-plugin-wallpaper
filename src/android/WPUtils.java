package com.varandas;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * This class echoes a string called from JavaScript.
 */
public class WPUtils extends CordovaPlugin {

    public Context context = null;
    private static final boolean IS_NOUGAT_OR_GREATER = Build.VERSION.SDK_INT >= 24;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        context = IS_NOUGAT_OR_GREATER ? cordova.getActivity().getWindow().getContext()
                : cordova.getActivity().getApplicationContext();


        String url = args.getString(0);
        Bitmap bmp = this.getBitmapFromUrl(url, callbackContext);

        switch (action) {
            case "setImageAsWallpaper":
                this.setImageAsWallpaper(bmp, callbackContext);

                return true;

            case "setImageAsLockScreen":
                this.setImageAsLockScreen(bmp, callbackContext);

                return true;

            case "setImageAsWallpaperAndLockScreen":
                this.setImageAsLockScreen(bmp, callbackContext);
                this.setImageAsWallpaper(bmp, callbackContext);

                return true;

            default:
                return false;
        }
    }

    /**
     * Creates a Bitmap from a given url string.
     *
     * @param url             - The imageUrl
     * @param callbackContext - Cordova callback context function.
     * @return Bitmap for the image url.
     */
    private Bitmap getBitmapFromUrl(String url, CallbackContext callbackContext) {
        Bitmap image = null;
        try {
            URL imageUrl = new URL(url);
            image = BitmapFactory.decodeStream(imageUrl.openStream());
        } catch (IOException e) {
            callbackContext.error(e.getMessage());
            e.printStackTrace();
        }

        return image;
    }

    /**
     * Sets a Bitmap resource as the device wallpaper.
     *
     * @param bmp             - The bitmap resource to use as wallpaper.
     * @param callbackContext - Cordova callback context function.
     */
    private void setImageAsWallpaper(Bitmap bmp, CallbackContext callbackContext) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            wallpaperManager.setBitmap(bmp);
        } catch (IOException e) {
            callbackContext.error(e.getMessage());
            e.printStackTrace();
        }

        callbackContext.success();
    }

    /**
     * Sets a Bitmap resource as the device lock screen wallpaper.
     *
     * @param bmp             - The bitmap resource to use as wallpaper
     * @param callbackContext Cordova callback context function.
     */
    private void setImageAsLockScreen(Bitmap bmp, CallbackContext callbackContext) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        try {
            wallpaperManager.setBitmap(bmp, null, true, WallpaperManager.FLAG_LOCK);
        } catch (IOException e) {
            callbackContext.error(e.getMessage());
            e.printStackTrace();
        }

        callbackContext.success();
    }

}
