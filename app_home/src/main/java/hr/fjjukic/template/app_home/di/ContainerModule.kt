package hr.fjjukic.template.app_home.di

import hr.fjjukic.template.app_home.container.view_model.MainContainerVM
import hr.fjjukic.template.app_home.home.view_model.HomeVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { MainContainerVM(get(), get()) }
    viewModel { HomeVM(get(), get(), get()) }
}
