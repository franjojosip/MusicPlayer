package hr.fjjukic.template.app_common.playlist

import hr.fjjukic.template.app_common.model.Playlist
import hr.fjjukic.template.app_common.model.Track

interface PlaylistManager {
    fun getPlaylist(): ArrayList<Playlist>
    fun createPlaylist(name: String)
    fun deletePlaylist(id: Long)
    fun addTracksToPlaylist(id: Long, tracks: ArrayList<Track>)
    fun deletePlaylistTrack(playlistId: Long, trackId: Long)
    fun playlistItemReorder(playlistId: Long, oldPosition: Int, newPosition: Int)
    fun getPlaylistTracks(id: Long): ArrayList<Track>
}