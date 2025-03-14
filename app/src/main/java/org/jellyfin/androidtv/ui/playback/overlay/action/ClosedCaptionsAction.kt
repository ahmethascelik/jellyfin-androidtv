package org.jellyfin.androidtv.ui.playback.overlay.action

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.onlinesubtitles.OnlineSubtitlesHelper
import org.jellyfin.androidtv.ui.playback.PlaybackController
import org.jellyfin.androidtv.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.jellyfin.androidtv.ui.playback.overlay.VideoPlayerAdapter
import org.jellyfin.androidtv.ui.playback.setSubtitleIndex
import org.jellyfin.sdk.model.api.MediaStreamType
import org.koin.android.ext.android.inject
import timber.log.Timber

class ClosedCaptionsAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
) : CustomAction(context, customPlaybackTransportControlGlue) {
	private var popup: PopupMenu? = null

	val onlineSubtitlesHelper by (context as Activity).inject<OnlineSubtitlesHelper>()


	init {
		initializeWithIcon(R.drawable.ic_select_subtitle)
	}

	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		if (playbackController.currentStreamInfo == null) {
			Timber.w("StreamInfo null trying to obtain subtitles")
			Toast.makeText(context, "Unable to obtain subtitle info", Toast.LENGTH_LONG).show()
			return
		}

		videoPlayerAdapter.leanbackOverlayFragment.setFading(false)
		removePopup()

		popup = PopupMenu(context, view, Gravity.END).apply {
			with(menu) {
				var order = 0
				add(0, -1, order++, context.getString(R.string.lbl_none)).apply {
					isChecked = playbackController.subtitleStreamIndex == -1
				}

				for (sub in playbackController.currentMediaSource.mediaStreams.orEmpty()) {
					if (sub.type != MediaStreamType.SUBTITLE) continue

					add(0, sub.index, order++, sub.displayTitle).apply {
						isChecked = sub.index == playbackController.subtitleStreamIndex
					}
				}

				for (subtitle in onlineSubtitlesHelper.getSubtitlesForMedia(playbackController.currentlyPlayingItem.id) ) {

					var offsetInfo = ""
					if(subtitle.offset != 0L){
						val sign = if (subtitle.offset >= 0) "+" else ""
						offsetInfo = "(⏱: " +sign + subtitle.offset+ " ms) "
					}


					val isDownloaded = onlineSubtitlesHelper.getDownloadedFile(context, subtitle.localFileName).exists()
					val icon = if (isDownloaded) "✅" else "⬇\uFE0F"
					val title = "$icon ${subtitle.language} - ${offsetInfo}${subtitle.title}"
					add(0, subtitle.localSubtitleId, order++, title ).apply {
						isChecked = subtitle.localSubtitleId == playbackController.subtitleStreamIndex
					}
				}

				setGroupCheckable(0, true, false)
			}
			setOnDismissListener {
				videoPlayerAdapter.leanbackOverlayFragment.setFading(true)
				popup = null
			}
			setOnMenuItemClickListener { item ->
				playbackController.setSubtitleIndex(item.itemId)
				true
			}
		}
		popup?.show()
	}

	fun removePopup() {
		popup?.dismiss()
	}


}
