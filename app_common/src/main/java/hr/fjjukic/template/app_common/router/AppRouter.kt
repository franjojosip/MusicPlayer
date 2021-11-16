package hr.fjjukic.template.app_common.router

import androidx.lifecycle.LifecycleOwner
import hr.fjjukic.template.app_common.livedata.SingleLiveData

open class AppRouter {
    val backCallback: SingleLiveData<Unit> = SingleLiveData()
    val navigationEvent: SingleLiveData<NavDirectionsWrapper> = SingleLiveData()

    fun observe(lifecycleOwner: LifecycleOwner, controller: NavigationController) {
        setBackNavigationObserver(lifecycleOwner, controller)
        setNavigationObserver(lifecycleOwner, controller)
    }

    private fun setNavigationObserver(
        lifecycleOwner: LifecycleOwner,
        controller: NavigationController
    ) {
        navigationEvent.observe(lifecycleOwner, {
            controller.navigate(it)
        })
    }

    private fun setBackNavigationObserver(
        lifecycleOwner: LifecycleOwner,
        controller: NavigationController
    ) {
        backCallback.observe(lifecycleOwner, {
            controller.popFromStack()
        })
    }
}
