package hr.fjjukic.template.app_common.model

import android.widget.Toast.LENGTH_SHORT
import androidx.navigation.NavDirections

sealed class EventUI {
    data class ToastUI(val message: String, val duration: Int = LENGTH_SHORT) : EventUI()
    data class SnackbarUI(val message: String, val duration: Int = LENGTH_SHORT) : EventUI()
    data class LoaderUI(val isEnabled: Boolean) : EventUI()
    data class NavigateUI(val navDirections: NavDirections) : EventUI()
}
