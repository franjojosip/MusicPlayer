package hr.fjjukic.template.app_common.playlist

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import hr.fjjukic.template.app_common.model.Playlist
import hr.fjjukic.template.app_common.model.Track

/**
 * PlaylistManager class used for manipulating playlists and tracks in it
 *
 * @param context Used to get contentResolver to obtain data from local storage
 *
 * MediaStore -> Class which provides access to media types like Audio, Video or Images*
 * uri -> Contains Uri to the external volume of device
 * projection -> List of columns which will be searched with query
 * selection -> Filter specific Media
 * cursor -> Pointer for passing through searched files
 *
 * This manager use MediaStore.Audio.Playlists which is DEPRECATED from API 31
 */
class PlaylistManagerImpl(private val context: Context) : PlaylistManager {

    /**
     * Method used for retrieving playlists from storage
     * Playlist -> Class which represents music playlist with id and name
     */
    override fun getPlaylists(): ArrayList<Playlist> {
        val playlists = ArrayList<Playlist>()
        val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Playlists._ID,
            MediaStore.Audio.Playlists.NAME
        )
        val sortOrder = "${MediaStore.Audio.Playlists.NAME} ASC"
        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        cursor?.let {
            it.moveToFirst()
            while (!it.isAfterLast) {
                playlists.add(
                    Playlist(
                        id = it.getCursorString(MediaStore.Audio.Playlists._ID).toLong(),
                        name = it.getCursorString(MediaStore.Audio.Playlists.NAME)
                    )
                )
                it.moveToNext()
            }

            it.close()
        }
        return playlists
    }

    /**
     * Method used for creating a playlist with given name
     */
    override fun createPlaylist(name: String) {
        val values = ContentValues(1).apply { put(MediaStore.Audio.Playlists.NAME, name) }
        val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        context.contentResolver.insert(uri, values)
    }

    /**
     * Method used for deleting a playlist with given playlist ID
     */
    override fun deletePlaylist(id: Long) {
        val whereFilter = MediaStore.Audio.Playlists._ID + "=?"
        context.contentResolver.delete(
            MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
            whereFilter,
            arrayOf(id.toString())
        )
    }

    /**
     * Method used for adding array of tracks to given playlist ID
     */
    override fun addTracksToPlaylist(id: Long, tracks: ArrayList<Track>) {
        val count = getPlaylistSize(id)
        val values = arrayOfNulls<ContentValues>(tracks.size)

        for (i in tracks.indices) {
            values[i] = ContentValues()
            values[i]?.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, i + count + 1)
            values[i]?.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, tracks[i].id)
        }


        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)
        val resolver = context.contentResolver
        resolver.bulkInsert(uri, values)
        resolver.notifyChange(Uri.parse("content://media"), null)
    }

    /**
     * Method used for retrieving number of tracks for given playlist ID
     */
    private fun getPlaylistSize(id: Long): Int {
        var count = 0
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)
        val projection = arrayOf(MediaStore.Audio.Playlists.Members._ID)
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, selection, null, null)

        cursor?.let {
            it.moveToFirst()
            while (!it.isAfterLast) {
                count++
                it.moveToNext()
            }

            cursor.close()
        }
        return count
    }

    /**
     * Method used for retrieving tracks for given playlist ID
     */
    override fun getPlaylistTracks(id: Long): ArrayList<Track> {
        val tracks = ArrayList<Track>()
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}  != 0"
        val sortOrder = "${MediaStore.Audio.Playlists.Members.PLAY_ORDER} ASC"
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
                            it.getCursorString(MediaStore.Audio.Media.DURATION)
                                .toLong()
                        ),
                        albumId = it.getCursorString(MediaStore.Audio.Media.ALBUM_ID).toLong()
                    )
                )

                it.moveToNext()
            }

            it.close()
        }
        return tracks
    }

    /**
     * Method used for deleting track from given playlist ID
     */
    override fun deletePlaylistTrack(playlistId: Long, trackId: Long) {
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId)
        val where = MediaStore.Audio.Playlists.Members._ID + "=?"
        val whereValue = arrayOf(trackId.toString())
        context.contentResolver.delete(uri, where, whereValue)
    }

    override fun playlistItemReorder(playlistId: Long, oldPosition: Int, newPosition: Int) {
        MediaStore.Audio.Playlists.Members.moveItem(
            context.contentResolver,
            playlistId,
            oldPosition,
            newPosition
        )
    }

    /**
     * Extension for retrieving cursor string for given Media column index
     */
    @SuppressLint("Range")
    fun Cursor.getCursorString(column: String): String {
        return this.getString(this.getColumnIndex(column))
    }
}