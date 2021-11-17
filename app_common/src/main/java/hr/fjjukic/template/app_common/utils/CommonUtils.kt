package hr.fjjukic.template.app_common.utils

import android.app.ActivityManager
import android.content.Context

/**
 * Utils used inside this application
 *
 * isRunning function is used to check if given service is still running
 */
class CommonUtils {
    companion object {
        fun isRunning(context: Context, serviceClass: Class<*>): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.name.equals(service.service.className)) {
                    return true
                }
            }
            return false
        }
    }
}