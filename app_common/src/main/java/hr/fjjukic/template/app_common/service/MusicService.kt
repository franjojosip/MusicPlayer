package hr.fjjukic.template.app_common.service

import android.app.Notification

/**
 * Interface with functions to handle showing music player in foreground or stopping foreground service
 */
interface MusicService {
    fun showNotificationForeground(notification: Notification)
    fun stopService()
}