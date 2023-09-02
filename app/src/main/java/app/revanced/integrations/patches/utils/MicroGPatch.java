package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.StringRef.str;

import android.content.ContentProviderClient;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

public class MicroGPatch {
    private static final String MICROG_VENDOR = "com.mgoogle";
    private static final String MICROG_PACKAGE_NAME = MICROG_VENDOR + ".android.gms";
    private static final Uri VANCED_MICROG_PROVIDER = Uri.parse("content://" + MICROG_VENDOR + ".android.gsf.gservices/prefix");

    public static void checkAvailability(Context context) {
        try {
            context.getPackageManager().getPackageInfo(MICROG_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException exception) {
            Toast.makeText(context, str("microg_not_installed_warning"), Toast.LENGTH_LONG).show();
            Toast.makeText(context, str("microg_not_installed_notice"), Toast.LENGTH_LONG).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // try-with-resources calls ContentProviderClient.close(), which requires API level 24
            try (var client = context.getContentResolver().acquireContentProviderClient(VANCED_MICROG_PROVIDER)) {
                if (client != null) return;
                Toast.makeText(context, str("microg_not_running_warning"), Toast.LENGTH_LONG).show();
            }

        } else {
            ContentProviderClient client = null;
            try {
                client = context.getContentResolver().acquireContentProviderClient(VANCED_MICROG_PROVIDER);
                if (client != null) return;
                Toast.makeText(context, str("microg_not_running_warning"), Toast.LENGTH_LONG).show();
            } finally {
                if (client != null) client.release();
            }
        }
    }
}
