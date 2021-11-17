package hr.fjjukic.template.app_common.manager.audio_focus

import hr.fjjukic.template.app_common.manager.music.MusicManager

/**
 * Interface with functions for working with audio focus
 */
interface AudioFocusManager {
    fun requestAudioFocus()
    fun abandonAudioFocus()
    fun setMusicManager(musicManager: MusicManager)
}