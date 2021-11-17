package hr.fjjukic.template.app_common.service

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import android.util.Log
import hr.fjjukic.template.app_common.enums.MusicManagerAction
import hr.fjjukic.template.app_common.manager.music.MusicManager
import hr.fjjukic.template.app_common.manager.music.MusicManagerImpl
import hr.fjjukic.template.app_common.utils.BytesUtil
import hr.fjjukic.template.app_common.utils.CommonUtils

/**
 * Class used for handling foreground music playback and actions if playing is cancelled
 *
 * AudioManager provides access to volume and ringer mode control.
 *
 * This class uses AudioManager.OnAudioFocusChangeListener which is used when needed handling
 * on audio focus change
 * This can occur when pausing, lowering sound etc.
 *
 * Service class is used when application is desired for longer run like playing music while player is in a foreground as a notification etc.
 */
class MusicServiceImpl(private val context: Context, private val musicManager: MusicManager) :
    Service(), MusicService,
    AudioManager.OnAudioFocusChangeListener {

    companion object {
        private const val TAG = "AppMusicService"

        fun getStartIntent(context: Context): Intent {
            return Intent(context, MusicServiceImpl::class.java)
        }

        fun start(context: Context) {
            if (!CommonUtils.isRunning(context, MusicServiceImpl::class.java)) {
                Log.d(TAG, "Service started")
                val starter = Intent(context, MusicServiceImpl::class.java)
                context.startService(starter)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, MusicServiceImpl::class.java))
        }
    }

    /**
     * Method used for showing music player as a notification on mobile phone
     * @param notification Notification object representing Status bar notification
     *
     * startForeground function is called to start foreground notification as service
     */
    override fun showNotificationForeground(notification: Notification) {
        startForeground(1337, notification)
    }

    /**
     * Add service to Music Manager and play track
     */
    override fun onCreate() {
        super.onCreate()
        musicManager.apply {
            setService(this@MusicServiceImpl)
            playTrack()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIncomingActions(intent)
        return START_NOT_STICKY
    }

    /**
     * Method used for stopping service in foreground
     * Occurs when user cancel music player or removes from stack
     *
     * stop with context parameter -> Function for stopping service in application
     * stopForeground -> Function for removing notification
     * onDestroy -> Lifecycle function for cleaning resources that this service was using
     */
    override fun stopService() {
        stop(context)
        stopForeground(true)
        onDestroy()
    }

    /**
     * Method used for handling focus change
     * Logging current focus change
     *
     * @param focusState Integer type of focus change (Focus GAIN, LOSS etc.)
     */
    override fun onAudioFocusChange(focusState: Int) {
        when (focusState) {
            AudioManager.AUDIOFOCUS_GAIN -> Log.d(TAG, "GAIN")
            AudioManager.AUDIOFOCUS_LOSS -> Log.d(TAG, "LOSS")
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> Log.d(TAG, "LOSS_TRANSIENT")
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> Log.d(TAG, "LOSS_TRANSIENT_CAN_DUCK")
        }
    }

    /**
     * Return the communication channel to the service
     * Null if clients can not bind to the service
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    /**
     * Handle intent action for Music Manager
     */
    private fun handleIncomingActions(intent: Intent?) {
        if (intent != null) {
            val extra = intent.getByteArrayExtra(MusicManagerImpl.KEY_ACTION)
            if (extra != null) {
                val action = BytesUtil.toObject<MusicManagerAction>(extra)
                musicManager.makeAction(action)
            }
        }
    }
}