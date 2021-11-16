package hr.fjjukic.template.app_common.router

import androidx.navigation.NavController

interface NavigationController {
    val activityNavController: NavController
    val fragmentNavController: NavController

    fun navigate(navDirections: NavDirectionsWrapper) {
        val navController = getNavController(navDirections)
        navController.navigate(navDirections.navDirections)
    }

    private fun getNavController(navDirections: NavDirectionsWrapper): NavController {
        return when (navDirections.isNewScreen) {
            true -> activityNavController
            else -> fragmentNavController
        }
    }

    fun popFromStack() {
        when {
            fragmentNavController.previousBackStackEntry != null -> {
                fragmentNavController.popBackStack()
            }
            else -> activityNavController.popBackStack()
        }
    }
}
