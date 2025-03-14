package org.jellyfin.androidtv.onlinesubtitles.preference.onlinesubtitles.opensubtitles

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceCategory
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.onlinesubtitles.R
import org.jellyfin.androidtv.onlinesubtitles.opensubtitles.OpenSubtitlesClient
import org.jellyfin.androidtv.onlinesubtitles.preference.OnlineSubtitlePreferences
import org.jellyfin.androidtv.onlinesubtitles.preference.dsl.OptionsFragment
import org.jellyfin.androidtv.onlinesubtitles.preference.dsl.OptionsScreen
import org.jellyfin.androidtv.onlinesubtitles.preference.widgets.InfoTextPreference
import org.koin.android.ext.android.inject

class OpenSubtitlesLanguagesScreen : OptionsFragment() {

	private val openSubtitlesClient: OpenSubtitlesClient by inject()

	private val userPreferences: OnlineSubtitlePreferences by inject()

	override val screen: OptionsScreen
		get() = OptionsScreen(requireContext())

	var langSet = HashSet<String>()

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.open_subtitles_language_preferences_screen, rootKey)

		requestLanguagesInfo()
	}

	private fun requestLanguagesInfo() {

		val infoTextPref = findPreference<InfoTextPreference>("os_info_text")
		infoTextPref?.title = "Test"



		lifecycleScope.launch {

			val token = userPreferences[OnlineSubtitlePreferences.openSubtitlesToken]
			val customApiKey = userPreferences[OnlineSubtitlePreferences.openSubtitlesCustomApiKey]
			val customUserAgent = userPreferences[OnlineSubtitlePreferences.openSubtitlesCustomUserAgent]


			val result = openSubtitlesClient.getSupportedLanguages(customApiKey, customUserAgent)
			result.onSuccess { info ->

				infoTextPref?.title = 	"Success" ;

				val languageCategory = findPreference<PreferenceCategory>("languages")

				info.data.forEach { data ->
					val checkBox = CheckBoxPreference(preferenceManager.context).apply {
						key = "os_key_${data.language_code}"
						title = data.language_name
						summary = data.language_code
						isChecked = false // Varsayılan olarak seçili değil
					}
					languageCategory?.addPreference(checkBox)

					if(checkBox.isChecked){
						langSet.add(data.language_code)
					}

					checkBox.setOnPreferenceChangeListener { preference, newValue -> //
						val willBeChecked = !checkBox.isChecked
						if(willBeChecked){
							langSet.add(data.language_code)
						}else{
							langSet.remove(data.language_code)
						}

						val sortedLanguages = langSet.sorted().joinToString(",")
						infoTextPref?.title = sortedLanguages
						userPreferences[OnlineSubtitlePreferences.openSubtitlesPreferredLanguages] = sortedLanguages
						true
					}
				}

				val sortedLanguages = langSet.sorted().joinToString(",")
				infoTextPref?.title = sortedLanguages

			}.onFailure {
				infoTextPref?.title = 	"Fail" ;

			}
		}
	}
}
