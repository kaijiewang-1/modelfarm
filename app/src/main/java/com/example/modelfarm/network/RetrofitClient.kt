package com.example.modelfarm.network

import android.content.Context
import com.example.modelfarm.network.models.ApiResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit客户端配置
 */
object RetrofitClient {

    @Volatile
    private var INSTANCE: Retrofit? = null

    fun getClient(context: Context): Retrofit {
        return INSTANCE ?: synchronized(this) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()

            Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
                .also { INSTANCE = it }
        }
    }

    @JvmStatic
    fun <T> create(context: Context, serviceClass: Class<T>): T {
        return getClient(context).create(serviceClass)
    }
}
