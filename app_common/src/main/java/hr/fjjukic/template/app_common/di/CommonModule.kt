package hr.fjjukic.template.app_common.di

import hr.fjjukic.template.app_common.repository.ResourceRepository
import hr.fjjukic.template.app_common.repository.ResourceRepositoryImpl
import hr.fjjukic.template.app_common.rest_interface.ApiRestInterface
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

val commonModule = module {
    single<ApiRestInterface> { get<Retrofit>().create(ApiRestInterface::class.java) }
    single<ResourceRepository> { ResourceRepositoryImpl(androidContext()) }
}
