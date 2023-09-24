package app.revanced.integrations.patches.ads;

import android.view.View;

import app.revanced.integrations.adremover.AdRemoverAPI;
import app.revanced.integrations.settings.SettingsEnum;
import app.revanced.integrations.utils.ReVancedUtils;

public final class GeneralAdsPatch extends Filter {
    private final String[] IGNORE = {
            "comment_thread", // skip blocking anything in the comments
            "download_",
            "downloads_",
            "home_video_with_context", // Don't filter anything in the home page video component.
            "library_recent_shelf",
            "playlist_add",
            "related_video_with_context", // Don't filter anything in the related video component.
            "|comment." // skip blocking anything in the comments replies
    };

    private final BlockRule custom = new CustomBlockRule(
            SettingsEnum.ADREMOVER_USER_FILTER,
            SettingsEnum.ADREMOVER_CUSTOM_FILTER
    );

    public GeneralAdsPatch() {
        var carouselAd = new BlockRule(SettingsEnum.ADREMOVER_GENERAL_ADS, "carousel_ad");
        var channelGuidelines = new BlockRule(SettingsEnum.ADREMOVER_CHANNEL_GUIDELINES, "channel_guidelines_entry_banner");
        var channelMemberShelf = new BlockRule(SettingsEnum.ADREMOVER_CHANNEL_MEMBER_SHELF, "member_recognition_shelf");
        var chapterTeaser = new BlockRule(SettingsEnum.ADREMOVER_CHAPTER_TEASER, "expandable_metadata");
        var communityGuidelines = new BlockRule(SettingsEnum.ADREMOVER_COMMUNITY_GUIDELINES, "community_guidelines");
        var communityPosts = new BlockRule(SettingsEnum.ADREMOVER_COMMUNITY_POSTS, "post_base_wrapper");
        var compactBanner = new BlockRule(SettingsEnum.ADREMOVER_COMPACT_BANNER, "compact_banner");
        var graySeparator = new BlockRule(SettingsEnum.ADREMOVER_GRAY_SEPARATOR, "cell_divider");
        var imageShelf = new BlockRule(SettingsEnum.ADREMOVER_IMAGE_SHELF, "image_shelf");
        var inFeedSurvey = new BlockRule(SettingsEnum.ADREMOVER_FEED_SURVEY, "infeed_survey");
        var infoPanel = new BlockRule(SettingsEnum.ADREMOVER_INFO_PANEL, "publisher_transparency_panel", "single_item_information_panel");
        var joinMembership = new BlockRule(SettingsEnum.ADREMOVER_CHANNELBAR_JOIN_BUTTON, "compact_sponsor_button");
        var latestPosts = new BlockRule(SettingsEnum.ADREMOVER_LATEST_POSTS, "post_shelf");
        var medicalPanel = new BlockRule(SettingsEnum.ADREMOVER_MEDICAL_PANEL, "medical_panel", "emergency_onebox");
        var merchandise = new BlockRule(SettingsEnum.ADREMOVER_MERCHANDISE, "product_carousel");
        var officialCard = new BlockRule(SettingsEnum.ADREMOVER_OFFICIAL_CARDS, "official_card");
        var paidContent = new BlockRule(SettingsEnum.ADREMOVER_PAID_CONTENT, "paid_content_overlay");
        var selfSponsor = new BlockRule(SettingsEnum.ADREMOVER_SELF_SPONSOR, "cta_shelf_card");
        var subscribersCommunityGuidelines = new BlockRule(SettingsEnum.ADREMOVER_SUBSCRIBERS_COMMUNITY_GUIDELINES, "sponsorships_comments_upsell");
        var timedReactions = new BlockRule(SettingsEnum.ADREMOVER_TIMED_REACTIONS, "emoji_control_panel", "timed_reaction_player_animation", "timed_reaction_live_player_overlay");
        var viewProducts = new BlockRule(SettingsEnum.ADREMOVER_VIEW_PRODUCTS, "product_item", "products_in_video");
        var webSearchPanel = new BlockRule(SettingsEnum.ADREMOVER_WEB_SEARCH_PANEL, "web_link_panel");

        var generalAds = new BlockRule(
            SettingsEnum.ADREMOVER_GENERAL_ADS,
                "active_view_display_container",
                "ads_",
                "ads_video_with_context",
                "ad_",
                "banner_text_icon",
                "brand_video_shelf",
                "brand_video_singleton",
                "carousel_footered_layout",
                "carousel_headered_layout",
                "full_width_square_image_layout",
                "hero_promo_image",
                "landscape_image_wide_button_layout",
                "legal_disclosure_cell",
                "lumiere_promo_carousel",
                "primetime_promo",
                "product_details",
                "square_image_layout",
                "statement_banner",
                "text_image_button_group_layout",
                "text_image_button_layout",
                "video_display_button_group_layout",
                "video_display_carousel_buttoned_layout",
                "video_display_full_layout",
                "watch_metadata_app_promo",
                "_ad",
                "_ads",
                "_ad_with",
                "_buttoned_layout",
                "|ads_",
                "|ad_"
        );
        var movieAds = new BlockRule(
                SettingsEnum.ADREMOVER_MOVIE_SHELF,
                "browsy_bar",
                "compact_movie",
                "horizontal_movie_shelf",
                "movie_and_show_upsell_card",
                "compact_tvfilm_item"
        );

        this.pathRegister.registerAll(
                channelGuidelines,
                channelMemberShelf,
                chapterTeaser,
                communityGuidelines,
                communityPosts,
                compactBanner,
                generalAds,
                imageShelf,
                inFeedSurvey,
                infoPanel,
                joinMembership,
                latestPosts,
                medicalPanel,
                merchandise,
                movieAds,
                officialCard,
                paidContent,
                selfSponsor,
                subscribersCommunityGuidelines,
                timedReactions,
                viewProducts,
                webSearchPanel
        );

        this.identifierRegister.registerAll(
                graySeparator,
                carouselAd
        );
    }

    public boolean filter(final String path, final String identifier) {
        BlockResult result;

        if (custom.isEnabled() && custom.check(path).isBlocked())
            result = BlockResult.CUSTOM;
        else if (ReVancedUtils.containsAny(path, IGNORE))
            result = BlockResult.IGNORED;
        else if (pathRegister.contains(path) || identifierRegister.contains(identifier))
            result = BlockResult.DEFINED;
        else
            result = BlockResult.UNBLOCKED;

        return result.filter;
    }

    private enum BlockResult {
        UNBLOCKED(false, "Unblocked"),
        IGNORED(false, "Ignored"),
        DEFINED(true, "Blocked"),
        CUSTOM(true, "Custom");

        final boolean filter;
        final String message;

        BlockResult(boolean filter, String message) {
            this.filter = filter;
            this.message = message;
        }
    }

    /**
     * Hide the specific view, which shows ads in the homepage.
     *
     * @param view The view, which shows ads.
     */
    public static void hideAdAttributionView(View view) {
        if (!SettingsEnum.ADREMOVER_GENERAL_ADS.getBoolean()) return;
        AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static void hideBreakingNewsShelf(View view) {
        if (!SettingsEnum.ADREMOVER_BREAKING_NEWS_SHELF.getBoolean()) return;
        AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static void hideAlbumCards(View view) {
        if (!SettingsEnum.ADREMOVER_ALBUM_CARDS.getBoolean()) return;
        AdRemoverAPI.HideViewWithLayout1dp(view);
    }

    public static boolean hideInfoPanel() {
        return SettingsEnum.ADREMOVER_INFO_PANEL.getBoolean();
    }

    public static boolean hidePaidContentBanner() {
        return SettingsEnum.ADREMOVER_PAID_CONTENT.getBoolean();
    }
}
