package hr.fjjukic.template.app_common.adapter

import androidx.lifecycle.LifecycleOwner
import hr.fjjukic.template.app_common.delegate.EventDelegate
import hr.fjjukic.template.app_common.livedata.SingleLiveData
import hr.fjjukic.template.app_common.model.EventUI


/**
 * Class which contains LiveData variables used for Events on UI
 * Variables use LAZY initialization
 *
 * Method "observe" requires two parameters
 * @param LifecycleOwner is required because it is aware of Android Lifecycle to which will link our LiveData
 * @param EventDelegate interface provides methods when UI event occur
 *
 * Class contains properties used as UI events
 * @property loaderUI Used for observing show/hide loader
 * @property toastUI Used for showing Toasts
 * @property snackbarUI Used for showing Snackbar with message
 * @property navigationEvent Used for navigationEvent to navigate on new screen
 *
 */
open class ScreenAdapterImpl {
    val loaderUI: SingleLiveData<EventUI.LoaderUI> by lazy { SingleLiveData() }
    val toastUI: SingleLiveData<EventUI.ToastUI> by lazy { SingleLiveData() }
    val snackbarUI: SingleLiveData<EventUI.SnackbarUI> by lazy { SingleLiveData() }
    val navigationEvent: SingleLiveData<EventUI.NavigateUI> by lazy { SingleLiveData() }

    /**
     * Call this method from Fragment or Activity to start observing LiveData
     */
    fun observe(lifecycleOwner: LifecycleOwner, eventDelegate: EventDelegate) {
        observeLoader(lifecycleOwner, eventDelegate)
        observeToast(lifecycleOwner, eventDelegate)
        observeSnackbar(lifecycleOwner, eventDelegate)
        observeNavigation(lifecycleOwner, eventDelegate)
    }

    private fun observeNavigation(lifecycleOwner: LifecycleOwner, eventDelegate: EventDelegate) {
        navigationEvent.observe(lifecycleOwner, { eventDelegate.navigate(it.navDirections) })
    }

    private fun observeSnackbar(lifecycleOwner: LifecycleOwner, eventDelegate: EventDelegate) {
        snackbarUI.observe(lifecycleOwner, { eventDelegate.showSnackbar(it) })
    }

    private fun observeToast(lifecycleOwner: LifecycleOwner, eventDelegate: EventDelegate) {
        toastUI.observe(lifecycleOwner, {
            eventDelegate.showToast(it)
        })
    }

    private fun observeLoader(lifecycleOwner: LifecycleOwner, eventDelegate: EventDelegate) {
        loaderUI.observe(lifecycleOwner, {
            when (it.isEnabled) {
                true -> eventDelegate.showLoader()
                else -> eventDelegate.hideLoader()
            }
        })
    }
}
