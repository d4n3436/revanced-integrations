package app.revanced.integrations.patches.video;

import app.revanced.integrations.settings.SettingsEnum;

public class VideoSpeedPatch {

    private static boolean newVideo = false;
    private static boolean userChangedSpeed = false;
    private static String currentContentCpn;

    public static void userChangedSpeed(float speed) {
        userChangedSpeed = true;
    }

    public static float getSpeedValue() {
        if (!newVideo || userChangedSpeed) return -1.0f;

        float defaultSpeed = SettingsEnum.DEFAULT_VIDEO_SPEED.getFloat();

        newVideo = false;

        if (defaultSpeed == -2.0f) return -1.0f;
        else if (!isCustomVideoSpeedEnabled() && defaultSpeed >= 2.0f) defaultSpeed = 2.0f;

        return defaultSpeed;
    }

    public static void newVideoStarted(final String contentCpn, final boolean isLive) {
        if (contentCpn.isEmpty() || contentCpn.equals(currentContentCpn))
            return;
        currentContentCpn = contentCpn;
        newVideo = true;
        userChangedSpeed = false;
    }

    public static boolean isCustomVideoSpeedEnabled() {
        return SettingsEnum.ENABLE_CUSTOM_VIDEO_SPEED.getBoolean();
    }

}
