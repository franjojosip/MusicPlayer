package hr.fjjukic.template.app_common.view_model

import androidx.lifecycle.ViewModel
import hr.fjjukic.template.app_common.adapter.ScreenAdapterImpl
import hr.fjjukic.template.app_common.enums.ResultWrapper
import hr.fjjukic.template.app_common.router.AppRouter

abstract class AppVM() : ViewModel() {
    abstract val screenAdapter: ScreenAdapterImpl
    abstract val router: AppRouter

    fun ResultWrapper.withErrorHandling(action: (Any?) -> Unit) {
        when (this) {
            is ResultWrapper.GenericError -> {
                // TODO Handle general error
            }
            is ResultWrapper.NetworkError -> {
                // TODO Handle network error
            }
            is ResultWrapper.Success<*> -> action(this.value)
        }
    }
}
