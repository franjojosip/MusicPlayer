package hr.fjjukic.template.app.base.navigation

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import hr.fjjukic.template.app.R
import hr.fjjukic.template.app_common.fragment.AppFragment
import hr.fjjukic.template.app_common.router.NavigationController

class AppNavigationController(fragment: AppFragment<*, *>) : NavigationController {
    override val activityNavController: NavController =
        Navigation.findNavController(fragment.requireActivity(), R.id.nav_host_fragment)

    override val fragmentNavController: NavController = fragment.findNavController()
}
