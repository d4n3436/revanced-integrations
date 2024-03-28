package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.StringRef.str;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

public class MicroGPatch {
    private static final String MICROG_VENDOR = "app.revanced";
    private static final String MICROG_PACKAGE_NAME = MICROG_VENDOR + ".android.gms";
    private static final String GMSCORE_DOWNLOAD_LINK = "https://github.com/ReVanced/GmsCore/releases/latest";
    private static final String BACKGROUND_RUN_LINK = "https://github.com/kitadai31/revanced-patches-android6-7/wiki/Allow-GmsCore-run-in-background";
    private static final Uri VANCED_MICROG_PROVIDER = Uri.parse("content://" + MICROG_VENDOR + ".android.gsf.gservices/prefix");

    public static void checkAvailability(Context context) {
        try {
            context.getPackageManager().getPackageInfo(MICROG_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException exception) {
            Toast.makeText(context, str("microg_not_installed_warning"), Toast.LENGTH_LONG).show();
            Toast.makeText(context, str("microg_not_installed_notice"), Toast.LENGTH_LONG).show();
            startIntent(context, GMSCORE_DOWNLOAD_LINK);
            return;
        }

        ContentProviderClient client = null;
        try {
            client = context.getContentResolver().acquireContentProviderClient(VANCED_MICROG_PROVIDER);
            if (client != null) return;
            Toast.makeText(context, str("microg_not_running_warning"), Toast.LENGTH_LONG).show();
            startIntent(context, BACKGROUND_RUN_LINK);
        } finally {
            if (client != null) client.release();
        }

    }

    private static void startIntent(Context context, String uriString) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uriString)));
    }
}
