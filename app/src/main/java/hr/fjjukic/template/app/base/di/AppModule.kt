package hr.fjjukic.template.app.base.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hr.fjjukic.template.app.BuildConfig
import hr.fjjukic.template.app.base.navigation.AppNavigationController
import hr.fjjukic.template.app.base.shared_pref.AppSharedPreferenceImpl
import hr.fjjukic.template.app_common.fragment.AppFragment
import hr.fjjukic.template.app_common.router.AppRouter
import hr.fjjukic.template.app_common.router.NavigationController
import hr.fjjukic.template.app_common.shared_pref.AppSharedPreference
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<Gson> { GsonBuilder().create() }
    single<AppSharedPreference> { AppSharedPreferenceImpl(get(), get()) }
    single { provideRetrofit() }
    factory<NavigationController> { (fragment: AppFragment<*, *>) ->
        AppNavigationController(fragment)
    }

    factory { AppRouter() }
}

private fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient().newBuilder().build())
        .build()
}
