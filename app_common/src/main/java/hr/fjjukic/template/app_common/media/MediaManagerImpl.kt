package hr.fjjukic.template.app_common.media

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import hr.fjjukic.template.app_common.enums.TrackSearchType
import hr.fjjukic.template.app_common.model.Album
import hr.fjjukic.template.app_common.model.Artist
import hr.fjjukic.template.app_common.model.Genre
import hr.fjjukic.template.app_common.model.Track

/**
 * MediaManager class used for obtaining information like tracks, genres, albums or artists from phone
 *
 * @param context Used to get contentResolver to obtain data from local storage
 *
 * MediaStore -> Class which provides access to media types like Audio, Video or Images*
 * uri -> Contains Uri to the external volume of device
 * projection -> List of columns which will be searched with query
 * selection -> Filter specific Media
 * cursor -> Pointer for passing through searched files
 */
class MediaManagerImpl(private val context: Context) : MediaManager {

    /**
     * Method used for obtaining tracks from local storage
     *
     * @param searchType -> Parameter of type TrackSearchType
     * Required to perform search for specific or all tracks
     *
     * Track -> Class which represents music track
     * AudioColumn DATA -> DEPRECATED in API level 29
     */
    override fun getTracks(searchType: TrackSearchType): ArrayList<Track> {
        val tracks = ArrayList<Track>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val uri = when (searchType) {
            is TrackSearchType.Genre -> MediaStore.Audio.Genres.Members.getContentUri(
                "external",
                searchType.genreID
            )
            else -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        val selection = when (searchType) {
            is TrackSearchType.Album -> "${MediaStore.Audio.Albums.ALBUM_ID} == ${searchType.albumID}"
            is TrackSearchType.Artist -> "${MediaStore.Audio.Media.ARTIST_ID} == ${searchType.artistID}"
            else -> "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        }
        val sortOrder = "${MediaStore.Audio.AudioColumns.TITLE} COLLATE LOCALIZED ASC"
        val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        cursor?.let {
            it.moveToFirst()
            while (!it.isAfterLast) {
                tracks.add(
                    Track(
                        id = it.getCursorString(MediaStore.Audio.Media._ID).toLong(),
                        title = it.getCursorString(MediaStore.Audio.Media.TITLE),
                        artist = it.getCursorString(MediaStore.Audio.Media.ARTIST),
                        data = it.getCursorString(MediaStore.Audio.Media.DATA),
                        duration = Track.convertDuration(
                            it.getCursorString(MediaStore.Audio.Media.DURATION).toLong()
                        ),
                        albumId = it.getCursorString(MediaStore.Audio.Media.ALBUM_ID).toLong(),
                    )
                )
                it.moveToNext()
            }
            it.close()
        }

        return tracks
    }

    /**
     * Method used for obtaining albums from local storage
     * Album -> Class which represents music album
     * Value ALBUM_ART -> DEPRECATED in API level 29
     */
    override fun getAlbums(): ArrayList<Album> {
        val albums = ArrayList<Album>()
        val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART
        )

        val sortOrder = "${MediaStore.Audio.Media.ALBUM} ASC"

        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        cursor?.let {
            it.moveToFirst()
            while (!it.isAfterLast) {
                albums.add(
                    Album(
                        id = it.getCursorString(MediaStore.Audio.Albums._ID).toLong(),
                        album = it.getCursorString(MediaStore.Audio.Albums.ALBUM),
                        artist = it.getCursorString(MediaStore.Audio.Albums.ARTIST),
                        art = it.getCursorString(MediaStore.Audio.Albums.ALBUM_ART)
                    )
                )
                it.moveToNext()
            }
            it.close()
        }

        return albums
    }

    /**
     * Method used for obtaining album image path from local storage
     * Value ALBUM_ART -> DEPRECATED in API level 29
     */
    override fun getAlbumImagePath(albumID: Long): String? {
        val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Albums.ALBUM_ART)
        val selection = MediaStore.Audio.Albums._ID + "=?"
        val args = arrayOf(albumID.toString())

        val cursor = context.contentResolver.query(uri, projection, selection, args, null)

        var albumPath: String? = null

        cursor?.let {
            if (it.moveToFirst()) albumPath =
                it.getCursorString(MediaStore.Audio.Albums.ALBUM_ART)
            it.close()
        }

        return albumPath
    }

    /**
     * Method used for obtaining artists from local storage
     * Artist -> Class which represents artist
     */
    override fun getArtists(): ArrayList<Artist> {
        val artists = ArrayList<Artist>()
        val uri: Uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )

        val sortOrder = "${MediaStore.Audio.Artists.ARTIST} ASC"
        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        cursor?.let {
            it.moveToFirst()
            while (!it.isAfterLast) {
                artists.add(
                    Artist(
                        id = it.getCursorString(MediaStore.Audio.Artists._ID).toLong(),
                        artist = it.getCursorString(MediaStore.Audio.Artists.ARTIST),
                        albumCount = it.getCursorString(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS),
                        trackCount = it.getCursorString(MediaStore.Audio.Artists.NUMBER_OF_TRACKS),
                    )
                )
                it.moveToNext()
            }
            it.close()
        }
        return artists
    }

    /**
     * Method used for obtaining genres from local storage
     * Genre -> Class which represents music genre
     */
    override fun getGenres(): ArrayList<Genre> {
        val genres = ArrayList<Genre>()
        val uri: Uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME
        )

        val sortOrder = "${MediaStore.Audio.Genres.NAME} ASC"
        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        cursor?.let {
            it.moveToFirst()
            while (!it.isAfterLast) {
                genres.add(
                    Genre(
                        id = it.getCursorString(MediaStore.Audio.Genres._ID).toLong(),
                        name = it.getCursorString(MediaStore.Audio.Genres.NAME)
                    )
                )
                it.moveToNext()
            }
            it.close()
        }
        return genres
    }

    /**
     * Extension for retrieving cursor string for given Media column index
     */
    @SuppressLint("Range")
    fun Cursor.getCursorString(column: String): String {
        return this.getString(this.getColumnIndex(column))
    }
}