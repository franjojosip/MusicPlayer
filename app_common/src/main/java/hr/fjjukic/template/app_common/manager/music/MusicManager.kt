package hr.fjjukic.template.app_common.manager.music

import hr.fjjukic.template.app_common.enums.MusicManagerAction
import hr.fjjukic.template.app_common.model.Track
import hr.fjjukic.template.app_common.service.MusicService

interface MusicManager {
    fun setService(service: MusicService)

    fun playTrack()
    fun pauseTrack()
    fun resumeTrack()
    fun resumePause()
    fun updateTracks(tracks: ArrayList<Track>, currentTrackPosition: Int)

    fun getCurrentTrack(): Track

    fun previousTrack()
    fun nextTrack()

    fun isPlaying(): Boolean

    fun setVolume(leftVolume: Float, rightVolume: Float)

    fun buildNotification()
    fun closeMusicPlayer()

    fun makeAction(action: MusicManagerAction)

    fun getTracksSize(): Int
}