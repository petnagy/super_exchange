package com.petnagy.superexchange.inject.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.petnagy.superexchange.SuperExchange
import com.petnagy.superexchange.inject.AppContext
import com.petnagy.superexchange.network.FixerIoEndpoint
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/***
 * Application level injection.
 */
@Module
class DaggerApplicationModule {

    companion object {
        private const val TIME_OUT_IN_SEC: Long = 60
        private const val API_BASE_URL = "http://data.fixer.io/api/"
    }

    @Provides
    @AppContext
    internal fun provideAppContext(application: SuperExchange): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
            .serializeNulls()
            .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .readTimeout(TIME_OUT_IN_SEC, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT_IN_SEC, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideEndpoint(retrofit: Retrofit): FixerIoEndpoint = retrofit.create<FixerIoEndpoint>(FixerIoEndpoint::class.java)
}