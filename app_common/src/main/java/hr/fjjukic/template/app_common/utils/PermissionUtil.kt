package hr.fjjukic.template.app_common.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale

class PermissionUtil {
    companion object {
        const val CODE = 1
        private const val TAG = "PermissionsUtils"

        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        fun isPermissionsGranted(context: Context): Boolean {
            permissions
                .filter { context.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
                .forEach { _ -> return false }
            return true
        }

        fun checkShouldShowPermissionRationale(activity: Activity): Boolean {
            permissions
                .filter { shouldShowRequestPermissionRationale(activity, it) }
                .forEach { _ -> return true }
            return false
        }
    }


}