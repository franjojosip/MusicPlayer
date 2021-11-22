package hr.fjjukic.template.app_common.manager.media

import hr.fjjukic.template.app_common.enums.TrackSearchType
import hr.fjjukic.template.app_common.model.Album
import hr.fjjukic.template.app_common.model.Artist
import hr.fjjukic.template.app_common.model.Genre
import hr.fjjukic.template.app_common.model.Track

/**
 * Interface with functions for searching media in local storage
 */
interface MediaManager {
    suspend fun getTracks(searchType: TrackSearchType): ArrayList<Track>
    suspend fun getAlbums(): ArrayList<Album>
    suspend fun getAlbumImagePath(albumID: Long): String?
    suspend fun getArtists(): ArrayList<Artist>
    suspend fun getGenres(): ArrayList<Genre>
}