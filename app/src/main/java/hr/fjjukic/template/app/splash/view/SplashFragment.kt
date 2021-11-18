package hr.fjjukic.template.app.splash.view

import android.os.Bundle
import android.view.View
import hr.fjjukic.template.app.R
import hr.fjjukic.template.app.databinding.FragmentSplashBinding
import hr.fjjukic.template.app.splash.view_model.SplashVM
import hr.fjjukic.template.app_common.fragment.AppFragment
import hr.fjjukic.template.app_common.utils.PermissionUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : AppFragment<SplashVM, FragmentSplashBinding>() {
    override val layoutId: Int = R.layout.fragment_splash
    override val appVM: SplashVM by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isPermissionGranted = PermissionUtil.isPermissionsGranted(requireContext())
        appVM.startLoadingDelay(isPermissionGranted)
    }
}
