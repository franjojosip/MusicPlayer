package hr.fjjukic.template.app.permission.view_model

import androidx.lifecycle.viewModelScope
import hr.fjjukic.template.app.permission.view.PermissionFragmentDirections
import hr.fjjukic.template.app_common.adapter.ScreenAdapterImpl
import hr.fjjukic.template.app_common.router.AppRouter
import hr.fjjukic.template.app_common.router.NavDirectionsWrapper
import hr.fjjukic.template.app_common.view_model.AppVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PermissionVM(
    override val screenAdapter: ScreenAdapterImpl,
    override val router: AppRouter
) : AppVM() {
    fun navigateToMainContainer() {
        viewModelScope.launch(context = Dispatchers.Default) {
            router.navigationEvent.postValue(NavDirectionsWrapper(PermissionFragmentDirections.actionPermissionToMainContainer()))
        }
    }
}