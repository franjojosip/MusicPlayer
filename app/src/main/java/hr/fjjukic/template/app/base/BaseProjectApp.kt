package hr.fjjukic.template.app.base

import android.app.Application
import hr.fjjukic.template.app.base.di.appModule
import hr.fjjukic.template.app.permission.di.permissionModule
import hr.fjjukic.template.app_common.di.commonModule
import hr.fjjukic.template.app_home.di.homeModule
import hr.fjjukic.template.app.splash.di.splashModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class MusicPlayerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@MusicPlayerApp)
            modules(getKoinModules())
        }
    }

    private fun getKoinModules(): List<Module> {
        return listOf(
            appModule,
            commonModule,
            splashModule,
            homeModule,
            permissionModule
        )
    }
}
