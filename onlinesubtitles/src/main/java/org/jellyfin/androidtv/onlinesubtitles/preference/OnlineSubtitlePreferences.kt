package org.jellyfin.androidtv.onlinesubtitles.preference

import android.content.Context
import org.jellyfin.preference.store.SharedPreferenceStore
import org.jellyfin.preference.stringPreference

class OnlineSubtitlePreferences(context: Context) : SharedPreferenceStore(
	sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
) {
	companion object {
		const val SHARED_PREFERENCES_NAME = "onlinesubtitles"

		/**
		 * OpenSubtitles related preference values
		 */
		var openSubtitlesToken= stringPreference("open_subtitles_token", "")
		var openSubtitlesCustomApiKey= stringPreference("open_subtitles_custom_api_key", "")
		var openSubtitlesCustomUserAgent= stringPreference("open_subtitles_custom_user_agent", "")
		var openSubtitlesPreferredLanguages= stringPreference("open_subtitles_preferred_languages", "")

		/**
		 * SubDL related preference values
		 */
		var subdlCustomApiKey= stringPreference("subdl_custom_api_key", "")
		var subdlPreferredLanguages= stringPreference("subdl_preferred_languages", "")
	}
}
