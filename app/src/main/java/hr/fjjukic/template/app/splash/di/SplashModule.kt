package hr.fjjukic.template.app.splash.di

import hr.fjjukic.template.app.splash.view_model.SplashVM
import hr.fjjukic.template.app_common.adapter.ScreenAdapterImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    factory { ScreenAdapterImpl() }
    viewModel { SplashVM(get(), get()) }
}
