package hr.fjjukic.template.app.permission.di

import hr.fjjukic.template.app.permission.view_model.PermissionVM
import hr.fjjukic.template.app_common.adapter.ScreenAdapterImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val permissionModule = module {
    viewModel { PermissionVM(get(), get()) }
}
