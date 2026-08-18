package com.example.modelfarm.network.services

import com.example.modelfarm.network.models.ApiResponse
import com.example.modelfarm.network.models.OssSignatureItem
import com.example.modelfarm.network.models.OssSignatureRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 对象存储预签名（SaToken 由 [com.example.modelfarm.network.AuthInterceptor] 注入）
 */
interface OssApiService {

    @POST("/oss/signature")
    fun getUploadSignatures(@Body request: OssSignatureRequest): Call<ApiResponse<List<OssSignatureItem>>>
}
