package com.example.modelfarm.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 认证拦截器
 * 自动添加satoken到请求头（从AuthManager动态获取）
 */
class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = AuthManager.getInstance(context).getToken()
        val newRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .addHeader("satoken", token)
                .build()
        } else {
            originalRequest
        }
        return chain.proceed(newRequest)
    }
}
