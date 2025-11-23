package com.example.modelfarm.network.services

import com.example.modelfarm.network.models.*
import retrofit2.Call
import retrofit2.http.*

/**
 * 用户管理API接口
 */
interface UserApiService {
    
    /**
     * 用户登录
     */
    @POST("/user/login")
    fun login(@Body request: LoginRequest): Call<ApiResponse<LoginResponse>>
    
    /**
     * 用户登出
     */
    @POST("/user/logout")
    fun logout(): Call<ApiResponse<Nothing>>
    
    /**
     * 获取用户登录信息
     */
    @GET("/user/info")
    fun getUserInfo(): Call<ApiResponse<UserInfo>>
    
    /**
     * 获取用户列表
     */
    @GET("/user/list")
    fun getUserList(): Call<ApiResponse<List<User>>>
    
    /**
     * 获取当前用户信息
     */
    @GET("/user")
    fun getCurrentUser(): Call<ApiResponse<User>>
    
    /**
     * 用户注册
     */
    @POST("/user/register")
    fun register(@Body request: RegisterRequest): Call<ApiResponse<LoginResponse>>
    
    /**
     * 删除用户
     */
    @DELETE("/user/{id}")
    fun deleteUser(@Path("id") id: Int): Call<ApiResponse<Nothing>>
    
    /**
     * 更新用户信息
     */
    @PUT("/user")
    fun updateUser(@Body request: UpdateUserRequest): Call<ApiResponse<Nothing>>
    
    /**
     * 更新用户密码
     */
    @PUT("/user/password")
    fun updatePassword(@Body request: UpdatePasswordRequest): Call<ApiResponse<Nothing>>
    
    /**
     * 用户离开企业
     */
    @POST("/user/leave")
    fun leaveEnterprise(): Call<ApiResponse<Nothing>>
    
    /**
     * 用户加入企业
     */
    @POST("/user/join")
    fun joinEnterprise(@Body request: JoinEnterpriseRequest): Call<ApiResponse<Void>>
}

/**
 * 用户登录信息响应模型
 */
data class UserInfo(
    val userId: Int,
    val enterpriseId: Int
)
