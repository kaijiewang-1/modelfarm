package com.example.modelfarm.network.repositories

import android.content.Context
import com.example.modelfarm.network.RetrofitClient
import com.example.modelfarm.network.models.*
import com.example.modelfarm.network.services.UserApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 用户数据仓库
 * 封装用户相关的API调用
 */
class UserRepository(private val context: Context) {
    
    private val userApiService: UserApiService = RetrofitClient.create(context, UserApiService::class.java)
    
    /**
     * 登录回调接口
     */
    interface LoginCallback {
        fun onSuccess(loginResponse: LoginResponse)
        fun onError(errorMessage: String)
    }
    
    /**
     * 注册回调接口
     */
    interface RegisterCallback {
        fun onSuccess(message: String)
        fun onError(errorMessage: String)
    }
    
    /**
     * 用户登录
     */
    fun login(
        phone: String,
        password: String,
        callback: LoginCallback
    ) {
        val request = LoginRequest(phone, password)
        userApiService.login(request).enqueue(object : Callback<ApiResponse<LoginResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<LoginResponse>>,
                response: Response<ApiResponse<LoginResponse>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { loginResponse ->
                        callback.onSuccess(loginResponse)
                    } ?: callback.onError("登录响应数据为空")
                } else {
                    val errorMessage = response.body()?.message ?: "登录失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<LoginResponse>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 用户注册
     */
    fun register(
        username: String,
        password: String,
        phone: String,
        callback: RegisterCallback
    ) {
        val request = RegisterRequest(username, password, phone)
        userApiService.register(request).enqueue(object : Callback<ApiResponse<LoginResponse>> {
            override fun onResponse(
                call: Call<ApiResponse<LoginResponse>>,
                response: Response<ApiResponse<LoginResponse>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess("注册成功")
                } else {
                    val errorMessage = response.body()?.message ?: "注册失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<LoginResponse>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 获取当前用户信息
     */
    fun getCurrentUser(callback: (Result<User>) -> Unit) {
        userApiService.getCurrentUser().enqueue(object : Callback<ApiResponse<User>> {
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { user ->
                        callback(Result.success(user))
                    } ?: callback(Result.failure(Exception("用户信息为空")))
                } else {
                    val errorMessage = response.body()?.message ?: "获取用户信息失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 用户登出
     */
    fun logout(callback: (Result<Unit>) -> Unit) {
        userApiService.logout().enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback(Result.success(Unit))
                } else {
                    val errorMessage = response.body()?.message ?: "登出失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 更新用户信息
     */
    fun updateUser(
        username: String?,
        phone: String?,
        callback: (Result<Unit>) -> Unit
    ) {
        val request = UpdateUserRequest(username, phone)
        userApiService.updateUser(request).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback(Result.success(Unit))
                } else {
                    val errorMessage = response.body()?.message ?: "更新用户信息失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 更新密码
     */
    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        callback: (Result<Unit>) -> Unit
    ) {
        val request = UpdatePasswordRequest(oldPassword, newPassword)
        userApiService.updatePassword(request).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback(Result.success(Unit))
                } else {
                    val errorMessage = response.body()?.message ?: "更新密码失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 加入企业
     */
    fun joinEnterprise(
        invitedCode: String,
        callback: (Result<Unit>) -> Unit
    ) {
        val request = JoinEnterpriseRequest(invitedCode)
        userApiService.joinEnterprise(request).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback(Result.success(Unit))
                } else {
                    val errorMessage = response.body()?.message ?: "加入企业失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}
