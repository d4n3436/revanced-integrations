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
            boolean isNewLayoutEnabled = SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.REVANCED, "revanced_enable_old_layout", false);
            boolean isOldShortsEnabled = SharedPrefHelper.getBoolean(context, SharedPrefHelper.SharedPrefNames.REVANCED, "revanced_hide_shorts_player_pivot_bar_type_b", false);

            if (isNewLayoutEnabled) {
                return "18.05.40";
            } else if (isOldShortsEnabled) {
                return "17.03.38";
            } else {
                return version;
            }
        } catch (Exception ex){
            LogHelper.printException(VersionOverridePatch.class, "Failed to getBoolean", ex);
            return version;
        }
    }
}
