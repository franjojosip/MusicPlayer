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
    fun getTracks(searchType: TrackSearchType): ArrayList<Track>
    fun getAlbums(): ArrayList<Album>
    fun getAlbumImagePath(albumID: Long): String?
    fun getArtists(): ArrayList<Artist>
    fun getGenres(): ArrayList<Genre>
}