package hr.fjjukic.template.app.splash.view_model

import androidx.lifecycle.viewModelScope
import hr.fjjukic.template.app.splash.view.SplashFragmentDirections
import hr.fjjukic.template.app_common.adapter.ScreenAdapterImpl
import hr.fjjukic.template.app_common.router.AppRouter
import hr.fjjukic.template.app_common.router.NavDirectionsWrapper
import hr.fjjukic.template.app_common.view_model.AppVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashVM(
    override val screenAdapter: ScreenAdapterImpl,
    override val router: AppRouter
) : AppVM() {
    fun startLoadingDelay() {
        viewModelScope.launch(context = Dispatchers.Default) {
            delay(2000)
            router.navigationEvent.postValue(NavDirectionsWrapper(SplashFragmentDirections.actionSplashToMainContainer()))
        }
    }
}
