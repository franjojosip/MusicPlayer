package hr.fjjukic.template.app.permission.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import hr.fjjukic.template.app.R
import hr.fjjukic.template.app.databinding.FragmentPermissionBinding
import hr.fjjukic.template.app.permission.view_model.PermissionVM
import hr.fjjukic.template.app_common.fragment.AppFragment
import hr.fjjukic.template.app_common.utils.PermissionUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class PermissionFragment : AppFragment<PermissionVM, FragmentPermissionBinding>() {
    override val layoutId: Int = R.layout.fragment_permission
    override val appVM: PermissionVM by viewModel()

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
            if (PermissionUtil.isPermissionsGranted(requireContext())) {
                appVM.navigateToMainContainer()
            } else {
                requestAllPermissions()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        setUpClickEvents()
    }

    private fun checkPermissions() {
        if (PermissionUtil.isPermissionsGranted(requireContext())) {
            appVM.navigateToMainContainer()
        }
    }

    private fun setUpClickEvents() {
        binding.tvRequestPermissions.setOnClickListener {
            requestAllPermissions()
        }
    }

    private fun requestAllPermissions() {
        if (PermissionUtil.checkShouldShowPermissionRationale(requireActivity())) {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.request_permission_title))
                .setMessage(getString(R.string.request_permission_text))
                .setPositiveButton(
                    getString(R.string.continue_text)
                ) { _, _ ->
                    requestMultiplePermissions.launch(PermissionUtil.permissions)
                }
                .setNegativeButton(
                    getString(R.string.cancel)
                ) { dialog, _ -> dialog.dismiss() }.create()
                .show()
        } else {
            requestMultiplePermissions.launch(PermissionUtil.permissions)
        }
    }
}