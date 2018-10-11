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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class echoes a string called from JavaScript.
 */
public class WPUtils extends CordovaPlugin {

    private Context context = null;
    private static final boolean IS_NOUGAT_OR_GREATER = Build.VERSION.SDK_INT >= 24;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        context = IS_NOUGAT_OR_GREATER ? cordova.getActivity().getWindow().getContext()
                : cordova.getActivity().getApplicationContext();

        String url = args.getString(0);
        Bitmap bmp = null;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Bitmap> future = executorService.submit(new getBitmapFromURLCallable(url, callbackContext));

        try {
            bmp = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        switch (action) {
            case "setImageAsWallpaper":
                cordova.getThreadPool().execute(new setBackgroundImageRunnable(bmp, callbackContext));
                return true;

            case "setImageAsLockScreen":
                cordova.getThreadPool().execute(new setLockScreenImageRunnable(bmp, callbackContext));
                return true;

            case "setImageAsWallpaperAndLockScreen":
                cordova.getThreadPool().execute(new setLockScreenAndWallpaperImageRunnable(bmp, callbackContext));
                return true;

            default:
                return false;
        }
    }

    /**
     * Creates a Bitmap from a given url string.
     */
    private class getBitmapFromURLCallable implements Callable<Bitmap> {

        private String URL;
        private CallbackContext callbackContext;

        private getBitmapFromURLCallable(String URL, CallbackContext callbackContext) {
            this.URL = URL;
            this.callbackContext = callbackContext;
        }

        @Override
        public Bitmap call() {
            Bitmap bmp = null;
            try {
                URL imageUrl = new URL(this.URL);
                bmp = BitmapFactory.decodeStream(imageUrl.openStream());
            } catch (IOException e) {
                callbackContext.error(e.getMessage());
                e.printStackTrace();
            }

            return bmp;
        }
    }

    /**
     * Sets a Bitmap resource as the device wallpaper.
     */
    private class setBackgroundImageRunnable implements Runnable {

        private Bitmap bmp;
        private CallbackContext callbackContext;

        private setBackgroundImageRunnable(Bitmap bmp, CallbackContext callbackContext) {
            this.bmp = bmp;
            this.callbackContext = callbackContext;
        }

        @Override
        public void run() {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            try {
                wallpaperManager.setBitmap(bmp);
            } catch (IOException e) {
                callbackContext.error(e.getMessage());
                e.printStackTrace();
            }

            callbackContext.success();
        }
    }

    /**
     * Sets a Bitmap resource as the device lock screen wallpaper.
     */
    private class setLockScreenImageRunnable implements Runnable {

        private Bitmap bmp;
        private CallbackContext callbackContext;

        private setLockScreenImageRunnable(Bitmap bmp, CallbackContext callbackContext) {
            this.bmp = bmp;
            this.callbackContext = callbackContext;
        }

        @Override
        public void run() {
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

    private class setLockScreenAndWallpaperImageRunnable implements Runnable {

        private Bitmap bmp;
        private CallbackContext callbackContext;

        private setLockScreenAndWallpaperImageRunnable(Bitmap bmp, CallbackContext callbackContext) {
            this.bmp = bmp;
            this.callbackContext = callbackContext;
        }

        @Override
        public void run() {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            try {
                wallpaperManager.setBitmap(bmp);
                wallpaperManager.setBitmap(bmp, null, true, WallpaperManager.FLAG_LOCK);
            } catch (IOException e) {
                callbackContext.error(e.getMessage());
                e.printStackTrace();
            }

            callbackContext.success();
        }
    }

}
