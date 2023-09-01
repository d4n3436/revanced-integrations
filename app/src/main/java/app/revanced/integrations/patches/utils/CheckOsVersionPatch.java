package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;

public class CheckOsVersionPatch {
    public static void checkOsVersion(Context context) {
        if (Build.VERSION.SDK_INT < 26) return;  // No problem!

        // show warning
        Activity activity = (Activity) context;
        new AlertDialog.Builder(activity)
                .setTitle(str("dialog_title_warning"))
                .setMessage(str("revanced_newer_android_warning_body", Build.VERSION.RELEASE)) //%1$s
                .setPositiveButton("OK", (dialog, id) -> activity.finish())
                .setNeutralButton("Continue anyway", null)
                .setCancelable(false)
                .show();
    }
}
