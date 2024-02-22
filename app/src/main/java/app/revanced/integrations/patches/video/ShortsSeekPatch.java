package app.revanced.integrations.patches.video;

import static app.revanced.integrations.utils.ResourceUtils.identifier;

import android.view.View;
import android.widget.ImageView;

import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.sponsorblock.PlayerController;
import app.revanced.integrations.utils.ResourceType;

public class ShortsSeekPatch {

    /**
     * @return whether hooked
     */
    public static boolean hookToolBar(Enum<?> buttonEnum, ImageView imageView) {
        if (!SettingsEnum.ENABLE_SHORTS_SEEK.getBoolean()) return false;

        final String enumString = buttonEnum.name();
        if (enumString.isEmpty() || !(imageView.getParent() instanceof View))
            return false;
        View view = (View) imageView.getParent();

        final boolean useLongSeek = SettingsEnum.SHORTS_SEEK_AMOUNT.getBoolean();

        if (enumString.equals("SEARCH_FILLED")) {
            // hook search button
            imageView.setImageResource(
                    identifier(useLongSeek ? "ic_seek_back_10_fill_36px" : "ic_seek_back_5_fill_36px", ResourceType.DRAWABLE)
            );
            view.setOnClickListener(v -> PlayerController.skipRelativeMilliseconds(useLongSeek ? -10000 : -5000));
            return true;
        }

        if (enumString.equals("MORE_VERT")) {
            // hook 3-dots menu button
            imageView.setImageResource(
                    identifier(useLongSeek ? "ic_seek_forward_10_fill_36px" : "ic_seek_forward_5_fill_36px", ResourceType.DRAWABLE)
            );
            view.setOnClickListener(v -> PlayerController.skipRelativeMilliseconds(useLongSeek ? 10000 : 5000));
            return true;
        }

        return false;
    }

}
