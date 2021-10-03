package com.bykea.casestudy.app

import android.app.Application
import com.bykea.core.helper.Constants
import com.bykea.core.helper.HttpRequestHelper
import com.bykea.core.networkInterface.IBaseNetwork
import com.bykea.core.networkInterface.INetworkApi
import com.bykea.core.networkInterface.ISearchMusicResultNetwork
import com.bykea.core.serviceInterface.ISearchMusicResultService
import com.bykea.network.BaseNetworkImpl
import com.bykea.network.SearchMusicResultNetworkImpl
import com.bykea.service.SearchMusicResultServiceImpl
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.java.KoinAndroidApplication.create
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class BykeaCaseStudyApp : Application() {


    private val koinModules = module {
        single { makeRetrofitService() }
        single { HttpRequestHelper() }
        single { BaseNetworkImpl() }
        single<IBaseNetwork> { BaseNetworkImpl() }
        factory<ISearchMusicResultNetwork> { SearchMusicResultNetworkImpl() }
        factory<ISearchMusicResultService> { SearchMusicResultServiceImpl() }
    }

    override fun onCreate() {
        super.onCreate()

        //initialising the koin application
        val koin = create(this)
            .modules(
                koinModules
            )
        startKoin(koin)
    }


    private fun makeRetrofitService(): INetworkApi {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .cache(null)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build().create(INetworkApi::class.java)
    }

}