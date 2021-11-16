package hr.fjjukic.template.app_common.delegate

import androidx.navigation.NavDirections
import hr.fjjukic.template.app_common.model.EventUI

interface EventDelegate {
    fun showToast(toastUI: EventUI.ToastUI) {}
    fun showSnackbar(snackbar: EventUI.SnackbarUI) {}
    fun showLoader() {}
    fun hideLoader() {}
    fun hideKeyboard() {}
    fun navigate(navDirections: NavDirections) {}
}
