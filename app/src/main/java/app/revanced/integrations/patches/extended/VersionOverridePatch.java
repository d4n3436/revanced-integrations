package app.revanced.integrations.patches.extended;

import static app.revanced.integrations.utils.ReVancedUtils.context;

import app.revanced.integrations.utils.LogHelper;
import app.revanced.integrations.utils.SharedPrefHelper;

public class VersionOverridePatch {

    /*
    * Context is overridden when trying to play a YouTube video from the Google Play Store,
    * Which is speculated to affect VersionOverridePatch
    */
    public static String getVersionOverride(String version) {

        try {
            String versionToSpoof = SharedPrefHelper.getString(context, SharedPrefHelper.SharedPrefNames.REVANCED, "revanced_spoof_app_version_target", "");

            if (!versionToSpoof.isEmpty()) {
                return versionToSpoof;
            } else {
                return version;
            }
        } catch (Exception ex){
            LogHelper.printException(VersionOverridePatch.class, "Failed to getBoolean", ex);
            return version;
        }
    }
}
