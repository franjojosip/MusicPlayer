package hr.fjjukic.template.app_common.playlist

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import hr.fjjukic.template.app_common.model.Playlist
import hr.fjjukic.template.app_common.model.Track

class PlaylistManagerImpl(private val context: Context) : PlaylistManager {
    override fun getPlaylist(): ArrayList<Playlist> {
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

    override fun createPlaylist(name: String) {
        val resolver = context.contentResolver
        val values = ContentValues(1)
        values.put(MediaStore.Audio.Playlists.NAME, name)
        val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        resolver.insert(uri, values)
    }

    override fun deletePlaylist(id: Long) {
        val playlistId = id.toString()
        val resolver = context.contentResolver
        val where = MediaStore.Audio.Playlists._ID + "=?"
        val whereValue = arrayOf(playlistId)
        resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, where, whereValue)
    }

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

    @SuppressLint("Range")
    fun Cursor.getCursorString(column: String): String {
        return this.getString(this.getColumnIndex(column))
    }
}