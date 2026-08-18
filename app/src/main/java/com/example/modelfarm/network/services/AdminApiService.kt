package com.example.modelfarm.network.services

import com.example.modelfarm.network.models.AdminGetUsers
import com.example.modelfarm.network.models.ApiResponse
import com.example.modelfarm.network.models.JoinEnterpriseRequest
import com.example.modelfarm.network.models.LoginRequest
import com.example.modelfarm.network.models.LoginResponse
import com.example.modelfarm.network.models.RegisterRequest
import com.example.modelfarm.network.models.SaToken
import com.example.modelfarm.network.models.UpdatePasswordRequest
import com.example.modelfarm.network.models.UpdateUserRequest
import com.example.modelfarm.network.models.User
import com.example.modelfarm.network.models.UserWitnRole
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdminApiService {
    @GET("/admin/check")
    fun checkAdminPermission(): Call<ApiResponse<Void>>
    /**
     * 检查管理员权限
     */
    @GET("/admin/check")
    fun check(): Call<ApiResponse<Any>>

    /**
     * 获取用户列表
     */
    @GET("/admin/users")
    fun getAllUsers(): Call<ApiResponse<List<UserWitnRole>>>

    @DELETE("/admin/users/{userId}")
    fun deleteUser(@Path("userId") userId: Long): Call<ApiResponse<Any>>

}
