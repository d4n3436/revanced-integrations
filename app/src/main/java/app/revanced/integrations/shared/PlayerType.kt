package app.revanced.integrations.shared

import app.revanced.integrations.utils.Event

/**
 * WatchWhile player type
 */
@Suppress("unused")
enum class PlayerType {
    NONE,
    HIDDEN,
    WATCH_WHILE_MINIMIZED,
    WATCH_WHILE_MAXIMIZED,
    WATCH_WHILE_FULLSCREEN,
    WATCH_WHILE_SLIDING_MAXIMIZED_FULLSCREEN,
    WATCH_WHILE_SLIDING_MINIMIZED_MAXIMIZED,
    WATCH_WHILE_SLIDING_MINIMIZED_DISMISSED,
    WATCH_WHILE_SLIDING_FULLSCREEN_DISMISSED,
    INLINE_MINIMAL,
    VIRTUAL_REALITY_FULLSCREEN,
    WATCH_WHILE_PICTURE_IN_PICTURE;

    companion object {
        /**
         * safely parse from a string
         *
         * @param name the name to find
         * @return the enum constant, or null if not found
         */
        @JvmStatic
        fun safeParseFromString(name: String): PlayerType? {
            return values().firstOrNull { it.name == name }
        }

        /**
         * the current player type, as reported by [app.revanced.integrations.patches.utils.PlayerTypeHookPatch.YouTubePlayerOverlaysLayout_updatePlayerTypeHookEX]
         */
        @JvmStatic
        var current
            get() = currentPlayerType
            set(value) {
                currentPlayerType = value
                onChange(currentPlayerType)
            }
        private var currentPlayerType = NONE

        /**
         * player type change listener
         */
        val onChange = Event<PlayerType>()
    }

    /**
     * Weather Shorts are being played.
     */
    fun isNoneOrHidden(): Boolean {
        return this == NONE || this == HIDDEN
    }

    /**
     * Check if the current player type is
     * [NONE], [HIDDEN], [WATCH_WHILE_SLIDING_MINIMIZED_DISMISSED].
     *
     * Useful to check if a Short is being played or opened.
     *
     * Usually covers all use cases with no false positives, except if called from some hooks
     * when spoofing to an old version this will return false even
     * though a Short is being opened or is on screen (see [isNoneHiddenOrMinimized]).
     *
     * @return If nothing, a Short, or a regular video is sliding off screen to a dismissed or hidden state.
     */
    fun isNoneHiddenOrSlidingMinimized(): Boolean {
        return isNoneOrHidden() || this == WATCH_WHILE_SLIDING_MINIMIZED_DISMISSED
    }

    /**
     * Check if the current player type is
     * [NONE], [HIDDEN], [WATCH_WHILE_MINIMIZED], [WATCH_WHILE_SLIDING_MINIMIZED_DISMISSED].
     *
     * Useful to check if a Short is being played,
     * although will return false positive if a regular video is
     * opened and minimized (and a Short is not playing or being opened).
     *
     * Typically used to detect if a Short is playing when the player cannot be in a minimized state,
     * such as the user interacting with a button or element of the player.
     *
     * @return If nothing, a Short, a regular video is sliding off screen to a dismissed or hidden state,
     *         a regular video is minimized (and a new video is not being opened).
     */
    fun isNoneHiddenOrMinimized(): Boolean {
        return isNoneHiddenOrSlidingMinimized() || this == WATCH_WHILE_MINIMIZED
    }
}