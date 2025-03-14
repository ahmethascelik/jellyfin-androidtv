package org.jellyfin.androidtv.onlinesubtitles.preference.onlinesubtitles.opensubtitles

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.onlinesubtitles.R
import org.jellyfin.androidtv.onlinesubtitles.opensubtitles.OpenSubtitlesClient
import org.jellyfin.androidtv.onlinesubtitles.preference.OnlineSubtitlePreferences
import org.jellyfin.androidtv.onlinesubtitles.preference.dsl.OptionsFragment
import org.jellyfin.androidtv.onlinesubtitles.preference.dsl.OptionsScreen
import org.jellyfin.androidtv.onlinesubtitles.preference.widgets.ButtonWithProgressbarPreference
import org.jellyfin.androidtv.onlinesubtitles.preference.widgets.InfoTextPreference
import org.koin.android.ext.android.inject

class OpenSubtitlesLogoutScreen : OptionsFragment() {

	private val openSubtitlesClient: OpenSubtitlesClient by inject()
	private val userPreferences: OnlineSubtitlePreferences by inject()

	override val screen: OptionsScreen
		get() = OptionsScreen(requireContext())

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.open_subtitles_logout_preferences_screen, rootKey)

		val loginButtonPref = findPreference<ButtonWithProgressbarPreference>("os_logout_button")
		val infoTextPref = findPreference<InfoTextPreference>("os_info_text")

		requestUsageInfo()


		loginButtonPref?.setOnPreferenceClickListener {

			loginButtonPref.setLoading(true)

			lifecycleScope.launch {

				val token = userPreferences[OnlineSubtitlePreferences.openSubtitlesToken]
				val customApiKey = userPreferences[OnlineSubtitlePreferences.openSubtitlesCustomApiKey]
				val customUserAgent = userPreferences[OnlineSubtitlePreferences.openSubtitlesCustomUserAgent]


				val onLoggedOut = {

					loginButtonPref.setLoading(false)

					userPreferences[OnlineSubtitlePreferences.openSubtitlesToken] = ""
					userPreferences[OnlineSubtitlePreferences.openSubtitlesCustomUserAgent] = ""
					userPreferences[OnlineSubtitlePreferences.openSubtitlesCustomApiKey] = ""

					Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show()

					parentFragmentManager.popBackStack()
				}

				val result = openSubtitlesClient.logout(token, customApiKey, customUserAgent)
				result.onSuccess {
					onLoggedOut()
				}.onFailure {
					onLoggedOut()
				}
			}

			true
		}
	}

	private fun requestUsageInfo() {

		val loginButtonPref = findPreference<ButtonWithProgressbarPreference>("os_logout_button")
		val infoTextPref = findPreference<InfoTextPreference>("os_info_text")
		infoTextPref?.title = "Test"


		loginButtonPref?.setLoading(true)

		lifecycleScope.launch {

			val token = userPreferences[OnlineSubtitlePreferences.openSubtitlesToken]
			val customApiKey = userPreferences[OnlineSubtitlePreferences.openSubtitlesCustomApiKey]
			val customUserAgent = userPreferences[OnlineSubtitlePreferences.openSubtitlesCustomUserAgent]


			val result = openSubtitlesClient.getUserInfo(token, customApiKey, customUserAgent)
			result.onSuccess { info ->
				loginButtonPref?.setLoading(false)
				infoTextPref?.title = 	"userID : " + info.data?.userId + "\n" +
									"level  : " + info.data?.level + "\n" +
									"downloads Count : " + info.data?.downloadsCount + "\n" +
									"vip : " + info.data?.vip + "\n" +
									"remaining Downloads : " + info.data?.remainingDownloads + "\n" +
									"extInstalled : " + info.data?.extInstalled + "\n" +
									"allowed Downloads : " + info.data?.allowedDownloads + "\n" +
									"allowed Translations : " + info.data?.allowedTranslations + "\n" ;

			}.onFailure {
				loginButtonPref?.setLoading(false)

			}
		}
	}
}
