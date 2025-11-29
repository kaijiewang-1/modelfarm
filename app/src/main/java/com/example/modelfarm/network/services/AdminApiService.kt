package com.example.modelfarm.network.services

import com.example.modelfarm.network.models.ApiResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * 管理员API接口
 */
interface AdminApiService {
    
    /**
     * 检查企业管理员权限
     */
    @GET("/admin/check")
    fun checkAdminPermission(): Call<ApiResponse<Void>>
}

