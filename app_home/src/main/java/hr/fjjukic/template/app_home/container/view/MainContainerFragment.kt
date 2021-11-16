package hr.fjjukic.template.app_home.container.view

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import hr.fjjukic.template.app_common.fragment.AppFragment
import hr.fjjukic.template.app_home.R
import hr.fjjukic.template.app_home.container.view_model.MainContainerVM
import hr.fjjukic.template.app_home.databinding.FragmentMainContainerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainContainerFragment : AppFragment<MainContainerVM, FragmentMainContainerBinding>() {
    override val layoutId: Int = R.layout.fragment_main_container
    override val appVM: MainContainerVM by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNav()
    }

    private fun setupBottomNav() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}
