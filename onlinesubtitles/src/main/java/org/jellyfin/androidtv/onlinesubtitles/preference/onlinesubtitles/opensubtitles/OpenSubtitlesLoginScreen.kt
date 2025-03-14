package org.jellyfin.androidtv.onlinesubtitles.preference.onlinesubtitles.opensubtitles

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.preference.EditTextPreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.onlinesubtitles.R
import org.jellyfin.androidtv.onlinesubtitles.opensubtitles.OpenSubtitlesClient
import org.jellyfin.androidtv.onlinesubtitles.preference.OnlineSubtitlePreferences
import org.jellyfin.androidtv.onlinesubtitles.preference.dsl.OptionsFragment
import org.jellyfin.androidtv.onlinesubtitles.preference.dsl.OptionsScreen
import org.jellyfin.androidtv.onlinesubtitles.preference.widgets.ButtonWithProgressbarPreference
import org.jellyfin.androidtv.onlinesubtitles.preference.widgets.InfoTextPreference
import org.koin.android.ext.android.inject

class OpenSubtitlesLoginScreen : OptionsFragment() {

	private val openSubtitlesClient: OpenSubtitlesClient by inject()
	private val userPreferences: OnlineSubtitlePreferences by inject()

	override val screen: OptionsScreen
		get() =  OptionsScreen(requireContext())

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.open_subtitles_login_preferences_screen, rootKey)

		val usernamePref = findPreference<EditTextPreference>("os_username")
		val passwordPref = findPreference<EditTextPreference>("os_password")
		val apiKeyPref = findPreference<EditTextPreference>("os_api_key")
		val userAgentPref = findPreference<EditTextPreference>("os_user_agent")

		val loginButtonPref = findPreference<ButtonWithProgressbarPreference>("os_login_button")
		val infoTextPref = findPreference<InfoTextPreference>("os_info_text")

		//reset password text for security
		passwordPref?.text = ""

		loginButtonPref?.setOnPreferenceClickListener {

			val username = usernamePref?.text ?: ""
			val password = passwordPref?.text ?: ""
			val apiKey = apiKeyPref?.text ?: ""
			val userAgent = userAgentPref?.text ?: ""

			if (username.isNotEmpty() && password.isNotEmpty()) {

				loginButtonPref.setLoading(true)
				infoTextPref?.title = ""

				lifecycleScope.launch {

					val result = openSubtitlesClient.login(username, password, apiKey, userAgent)

					result.onSuccess { loginResponse ->
						loginButtonPref.setLoading(false)

						userPreferences[OnlineSubtitlePreferences.openSubtitlesToken] = loginResponse.token ?: ""
						userPreferences[OnlineSubtitlePreferences.openSubtitlesCustomUserAgent] = userAgent
						userPreferences[OnlineSubtitlePreferences.openSubtitlesCustomApiKey] = apiKey

						Toast.makeText(requireContext(), "Logged In Successfully", Toast.LENGTH_SHORT).show()

						delay(500)
						parentFragmentManager.popBackStack()


					}.onFailure { error ->
						loginButtonPref.setLoading(false)

						infoTextPref?.apply {
							isVisible = true
							title = error.message
						}
					}


				}
			} else {
				infoTextPref?.apply {
					isVisible = true
					title = "Please enter username and password"
				}

				Toast.makeText(requireContext(), "Please enter username and password", Toast.LENGTH_SHORT).show()
			}
			true
		}
	}
}
