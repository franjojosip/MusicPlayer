package hr.fjjukic.template.app_common.manager.playlist

import hr.fjjukic.template.app_common.model.Playlist
import hr.fjjukic.template.app_common.model.Track

/**
 * Interface with functions for manipulating playlists from local storage
 */
interface PlaylistManager {
    fun getPlaylists(): ArrayList<Playlist>
    fun createPlaylist(name: String)
    fun deletePlaylist(id: Long)
    fun addTracksToPlaylist(id: Long, tracks: ArrayList<Track>)
    fun deletePlaylistTrack(playlistId: Long, trackId: Long)
    fun playlistItemReorder(playlistId: Long, oldPosition: Int, newPosition: Int)
    fun getPlaylistTracks(id: Long): ArrayList<Track>
}