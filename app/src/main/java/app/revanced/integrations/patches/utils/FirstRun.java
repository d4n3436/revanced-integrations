package app.revanced.integrations.patches.utils;

import static app.revanced.integrations.utils.ReVancedUtils.runOnMainThreadDelayed;
import static app.revanced.integrations.utils.SharedPrefHelper.SharedPrefNames.YOUTUBE;
import static app.revanced.integrations.utils.SharedPrefHelper.saveString;
import static app.revanced.integrations.utils.SharedPrefHelper.getString;
import static app.revanced.integrations.utils.StringRef.str;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import java.util.Objects;

import app.revanced.integrations.BuildConfig;
import app.revanced.integrations.sponsorblock.PlayerController;
import app.revanced.integrations.settings.SettingsEnum;

public class FirstRun {

    public static void initializationSB(Context context) {
        if (SettingsEnum.SB_FIRSTRUN.getBoolean()) return;

        // set dummy video-id to initialize Sponsorblock: TeamVanced's Sponsorblock tutorial (https://www.youtube.com/watch?v=sE2IzSn-hHU)
        PlayerController.setCurrentVideoId("sE2IzSn-hHU");
        SettingsEnum.SB_FIRSTRUN.saveValue(true);
    }

    /**
     * The new layout is not loaded when the app is first installed.
     * (Also reproduced on unPatched YouTube)
     * <p>
     * Side effects when new layout is not loaded:
     * - Button container's layout is broken
     * - Zoom to fill screen not working
     * - 8X zoom not working in fullscreen
     * <p>
     * To fix this, show the reboot dialog when the app is installed for the first time.
     * <p>
     * The version of the current integrations is saved to YouTube's SharedPreferences to identify if the app was first installed.
     */
    public static void initializationRVX(Context context) {
        var integrationVersion = getString(context, YOUTUBE, "integrations", null);
        if (!Objects.equals(integrationVersion, BuildConfig.VERSION_NAME))
            saveString(context, YOUTUBE, "integrations", BuildConfig.VERSION_NAME);
        if (integrationVersion != null) return;

        // show dialog
        Activity activity = (Activity) context;

        new AlertDialog.Builder(activity)
                .setMessage(str("revanced_reboot_first_run"))
                .setPositiveButton(str("in_app_update_restart_button"), (dialog, id) ->
                        runOnMainThreadDelayed(() -> {
                            activity.finishAffinity();
                            activity.startActivity(activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName()));
                            System.exit(0);
                        }, 1000L)
                )
                .setNegativeButton(str("sign_in_cancel"), null)
                .setCancelable(false)
                .show();
    }

}