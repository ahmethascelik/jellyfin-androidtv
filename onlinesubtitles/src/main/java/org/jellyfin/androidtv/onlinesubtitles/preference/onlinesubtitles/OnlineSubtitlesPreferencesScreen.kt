package org.jellyfin.androidtv.onlinesubtitles.preference.onlinesubtitles

import org.jellyfin.androidtv.onlinesubtitles.R
import org.jellyfin.androidtv.onlinesubtitles.preference.OnlineSubtitlePreferences
import org.jellyfin.androidtv.onlinesubtitles.preference.dsl.OptionsFragment
import org.jellyfin.androidtv.onlinesubtitles.preference.dsl.optionsScreen
import org.jellyfin.androidtv.onlinesubtitles.preference.onlinesubtitles.opensubtitles.OpenSubtitlesLanguagesScreen
import org.jellyfin.androidtv.onlinesubtitles.preference.onlinesubtitles.opensubtitles.OpenSubtitlesLoginScreen
import org.jellyfin.androidtv.onlinesubtitles.preference.onlinesubtitles.opensubtitles.OpenSubtitlesLogoutScreen
import org.jellyfin.androidtv.onlinesubtitles.preference.onlinesubtitles.subdl.SubdlCustomApiKeyScreen
import org.jellyfin.androidtv.onlinesubtitles.preference.onlinesubtitles.subdl.SubdlLanguagesScreen

import org.koin.android.ext.android.inject

class OnlineSubtitlesPreferencesScreen : OptionsFragment() {
	private val userPreferences: OnlineSubtitlePreferences by inject()

	override val screen by optionsScreen {
		title = "Online Subtitles"

		category {
			title = "Open Subtitles"

			if (userPreferences[OnlineSubtitlePreferences.openSubtitlesToken].isEmpty()) {
				link {
					title = "Login"
					icon = R.drawable.ic_user
					withFragment<OpenSubtitlesLoginScreen>()
				}
			} else {
				link {
					title = "Account Details"
					icon = R.drawable.ic_user
					withFragment<OpenSubtitlesLogoutScreen>()
				}
				link {
					title = "Preferred Languages"
					icon = R.drawable.ic_select_subtitle
					content = userPreferences[OnlineSubtitlePreferences.openSubtitlesPreferredLanguages]
					withFragment<OpenSubtitlesLanguagesScreen>()
				}
			}

		}



		category {
			title = "SUBDL"

			link {
				title = "Set Custom Api Key"
				icon = R.drawable.ic_settings
				content = userPreferences[OnlineSubtitlePreferences.subdlCustomApiKey]
				withFragment<SubdlCustomApiKeyScreen>()
			}
			link {
				title = "Preferred Languages"
				icon = R.drawable.ic_select_subtitle
				content = userPreferences[OnlineSubtitlePreferences.subdlPreferredLanguages]
				withFragment<SubdlLanguagesScreen>()
			}
		}

	}

	override val rebuildOnResume: Boolean
		get() = true
}
