package org.jellyfin.androidtv.onlinesubtitles.preference.onlinesubtitles.subdl

import android.os.Bundle
import androidx.preference.EditTextPreference
import org.jellyfin.androidtv.onlinesubtitles.R
import org.jellyfin.androidtv.onlinesubtitles.preference.OnlineSubtitlePreferences
import org.jellyfin.androidtv.onlinesubtitles.preference.dsl.OptionsFragment
import org.jellyfin.androidtv.onlinesubtitles.preference.dsl.OptionsScreen
import org.jellyfin.androidtv.onlinesubtitles.preference.widgets.ButtonWithProgressbarPreference
import org.koin.android.ext.android.inject

class SubdlCustomApiKeyScreen : OptionsFragment() {

	private val userPreferences: OnlineSubtitlePreferences by inject()

	override val screen: OptionsScreen
		get() =  OptionsScreen(requireContext())

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.subdl_custom_apikey_preferences_screen, rootKey)

		val apiKeyPref = findPreference<EditTextPreference>("subdl_api_key")
		apiKeyPref?.text = userPreferences[OnlineSubtitlePreferences.subdlCustomApiKey]
		val loginButtonPref = findPreference<ButtonWithProgressbarPreference>("subdl_save_button")

		loginButtonPref?.setOnPreferenceClickListener {

			val apiKey = apiKeyPref?.text ?: ""
			userPreferences[OnlineSubtitlePreferences.subdlCustomApiKey] = apiKey
			this.parentFragmentManager.popBackStack()
			true
		}
	}
}
