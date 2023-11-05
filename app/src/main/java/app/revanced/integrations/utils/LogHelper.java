package app.revanced.integrations.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import app.revanced.integrations.BuildConfig;

public class LogHelper {

    public static void printDebug(Class<?> clazz, @NonNull String message) {
        if (!BuildConfig.DEBUG) return;
        Log.d("revanced: " + (clazz != null ? clazz.getSimpleName() : ""), message);
    }

    public static void printException(Class<?> clazz, String message, Throwable ex) {
        Log.e("revanced: " + (clazz != null ? clazz.getSimpleName() : ""), message, ex);
    }

    public static void printException(Class<?> clazz, String message) {
        Log.e("revanced: " + (clazz != null ? clazz.getSimpleName() : ""), message);
    }

    public static void info(Class<?> clazz, String message) {
        Log.i("revanced: " + (clazz != null ? clazz.getSimpleName() : ""), message);
    }
}
