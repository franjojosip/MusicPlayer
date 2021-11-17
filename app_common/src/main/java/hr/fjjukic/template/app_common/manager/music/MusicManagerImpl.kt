package hr.fjjukic.template.app_common.manager.music

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hr.fjjukic.template.app_common.enums.MusicManagerAction
import hr.fjjukic.template.app_common.manager.audio_focus.AudioFocusManager
import hr.fjjukic.template.app_common.manager.media.MediaManager
import hr.fjjukic.template.app_common.model.Track
import hr.fjjukic.template.app_common.service.MusicService
import hr.fjjukic.template.app_common.utils.BytesUtil

class MusicManagerImpl(
    private val context: Context,
    private val mediaManager: MediaManager,
    private val audioFocusManager: AudioFocusManager
) : MusicManager, MediaPlayer.OnCompletionListener {
    companion object {
        const val TAG = "AppMusicManager"
        const val KEY_ACTION = "key_action"
    }

    private var channelId = "music_channel_id"
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var tracks = ArrayList<Track>()
    private var currentTrackPosition = 0
    private var resumePosition = 0
    private var musicService: MusicService? = null

    private val _currentTrack: MutableLiveData<Track> by lazy { MutableLiveData() }
    val currentTrack: LiveData<Track> = _currentTrack

    private val _actionIsPlaying: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val actionIsPlaying: LiveData<Boolean> = _actionIsPlaying


    init {
        initMediaPlayer()
        audioFocusManager.setMusicManager(this)
    }

    private fun initMediaPlayer() {
        Log.d(TAG, "Init Media Player")
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setAudioAttributes(audioAttributes)
        audioFocusManager.requestAudioFocus()
    }

    override fun setService(service: MusicService) {
        this.musicService = service
    }

    override fun playTrack() {
        audioFocusManager.requestAudioFocus()
        mediaPlayer.apply {
            stop()
            reset()
            setDataSource(tracks[currentTrackPosition].data)
            prepare()
            start()
        }
        _currentTrack.postValue(tracks[currentTrackPosition])
        _actionIsPlaying.postValue(true)
        buildNotification()
    }

    override fun resumePause() {
        when (isPlaying()) {
            true -> pauseTrack()
            false -> resumeTrack()
        }
    }

    override fun pauseTrack() {
        resumePosition = mediaPlayer.currentPosition
        mediaPlayer.pause()
        _actionIsPlaying.postValue(false)
        buildNotification()
    }

    override fun resumeTrack() {
        audioFocusManager.requestAudioFocus()
        mediaPlayer.apply {
            seekTo(resumePosition)
            start()
        }
        _actionIsPlaying.postValue(true)
        buildNotification()
    }

    override fun updateTracks(tracks: ArrayList<Track>, currentTrackPosition: Int) {
        this.tracks = tracks
        this.currentTrackPosition = currentTrackPosition
    }

    override fun previousTrack() {
        when (currentTrackPosition - 1) {
            -1 -> currentTrackPosition = tracks.size - 1
            else -> currentTrackPosition--
        }
        playTrack()
    }

    override fun nextTrack() {
        when (currentTrackPosition + 1) {
            tracks.size -> currentTrackPosition = 0
            else -> currentTrackPosition++
        }
        playTrack()
    }

    override fun buildNotification() {
        //TODO Implement notification build when layout, ids and drawables imported
        /*
        val view = RemoteViews(context.packageName, R.layout.notification_music_player)
        val currentTrack = getCurrentTrack()

        view.setTextViewText(R.id.trackTitle, currentTrack.title)
        view.setTextViewText(R.id.trackArtist, currentTrack.artist)

        when (val bitmap =
            BitmapFactory.decodeFile(dataManager.getAlbumImagePath(currentTrack.albumId))) {
            null -> view.setImageViewResource(R.id.cover, R.drawable.no_music)
            else -> view.setImageViewBitmap(R.id.cover, bitmap)
        }


        view.setOnClickPendingIntent(R.id.next, handleActions(MusicManagerAction.NEXT))
        view.setOnClickPendingIntent(R.id.previous, handleActions(MusicManagerAction.PREVIOUS))
        view.setOnClickPendingIntent(R.id.close, handleActions(MusicManagerAction.CLOSE))

        when (isPlaying()) {
            true -> view.setImageViewResource(R.id.playPause, R.drawable.pause)
            false -> view.setImageViewResource(R.id.playPause, R.drawable.play)
        }

        view.setOnClickPendingIntent(R.id.playPause, handleActions(MusicManagerAction.RESUMEPAUSE))

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK


        val pIntent = PendingIntent.getActivity(context, 0, intent, 0)

        channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel() else ""

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.music)
            .setContent(view)
            .setContentIntent(pIntent)
            .build()

        musicService?.showNotificationForeground(notification)
         */
    }

    override fun getTracksSize(): Int = tracks.size

    override fun closeMusicPlayer() {
        audioFocusManager.abandonAudioFocus()
        _actionIsPlaying.postValue(false)
        mediaPlayer.stop()
        musicService?.stopService()
    }

    override fun isPlaying(): Boolean = mediaPlayer.isPlaying

    private fun handleActions(action: MusicManagerAction): PendingIntent? {
        val intent = Intent(context, MusicService::class.java)
        val bundle = BytesUtil.toByteArray(action)
        intent.putExtra(KEY_ACTION, bundle)
        return PendingIntent.getService(context, action.value, intent, 0)
    }

    override fun getCurrentTrack(): Track = tracks[currentTrackPosition]

    override fun onCompletion(p0: MediaPlayer?) {
        nextTrack()
    }

    override fun setVolume(leftVolume: Float, rightVolume: Float) {
        mediaPlayer.setVolume(leftVolume, rightVolume)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "android_music_service"
        val channelName = "Android Music Service"
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_HIGH
        )
        chan.lightColor = Color.BLUE
        chan.importance = NotificationManager.IMPORTANCE_NONE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun makeAction(action: MusicManagerAction) {
        if (tracks.size > 0) {
            when (action) {
                MusicManagerAction.PLAY -> playTrack()
                MusicManagerAction.RESUME_PAUSE -> resumePause()
                MusicManagerAction.PREVIOUS -> previousTrack()
                MusicManagerAction.NEXT -> nextTrack()
                else -> closeMusicPlayer()
            }
        }
    }
}