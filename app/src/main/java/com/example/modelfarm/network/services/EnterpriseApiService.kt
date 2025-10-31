package com.example.modelfarm.network.services

import com.example.modelfarm.network.models.*
import retrofit2.Call
import retrofit2.http.*

/**
 * 企业管理API接口
 */
interface EnterpriseApiService {
    
    /**
     * 创建企业
     */
    @POST("/enterprise")
    fun createEnterprise(@Body request: CreateEnterpriseRequest): Call<ApiResponse<Int>>
    
    /**
     * 获取企业信息
     */
    @GET("/enterprise")
    fun getEnterprise(): Call<ApiResponse<Enterprise>>
    
    /**
     * 更新企业信息
     */
    @PUT("/enterprise")
    fun updateEnterprise(@Body request: UpdateEnterpriseRequest): Call<ApiResponse<Nothing>>
    
    /**
     * 删除企业（注销）
     */
    @DELETE("/enterprise")
    fun deleteEnterprise(): Call<ApiResponse<Nothing>>
    
    /**
     * 获取企业农场列表
     */
    @GET("/enterprise/farms")
    fun getEnterpriseFarms(): Call<ApiResponse<List<EnterpriseFarm>>>
    
    /**
     * 获取企业用户列表
     */
    @GET("/enterprise/user")
    fun getEnterpriseUsers(): Call<ApiResponse<List<EnterpriseUser>>>
    
    /**
     * 生成企业邀请码
     */
    @GET("/enterprise/invitedCode")
    fun generateInvitedCode(): Call<ApiResponse<String>>
    
    /**
     * 获取最新企业邀请码
     */
    @GET("/enterprise/latestInvitedCode")
    fun getLatestInvitedCode(): Call<ApiResponse<String>>
    
    /**
     * 获取企业统计数据
     */
    @GET("/enterprise/count")
    fun getEnterpriseStats(): Call<ApiResponse<EnterpriseStats>>
}
