package com.example.modelfarm.network.repositories

import android.content.Context
import com.example.modelfarm.network.RetrofitClient
import com.example.modelfarm.network.models.*
import com.example.modelfarm.network.services.EnterpriseApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 企业数据仓库
 * 封装企业相关的API调用
 */
class EnterpriseRepository(private val context: Context) {
    
    private val enterpriseApiService: EnterpriseApiService = RetrofitClient.create(context, EnterpriseApiService::class.java)
    
    /**
     * 创建企业
     */
    fun createEnterprise(
        name: String,
        address: String,
        contactPerson: String,
        contactPhone: String,
        callback: (Result<Int>) -> Unit
    ) {
        val request = CreateEnterpriseRequest(name, address, contactPerson, contactPhone)
        enterpriseApiService.createEnterprise(request).enqueue(object : Callback<ApiResponse<Int>> {
            override fun onResponse(
                call: Call<ApiResponse<Int>>,
                response: Response<ApiResponse<Int>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { enterpriseId ->
                        callback(Result.success(enterpriseId))
                    } ?: callback(Result.failure(Exception("创建企业响应数据为空")))
                } else {
                    val errorMessage = response.body()?.message ?: "创建企业失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 获取企业信息
     */
    fun getEnterprise(callback: (Result<Enterprise>) -> Unit) {
        enterpriseApiService.getEnterprise().enqueue(object : Callback<ApiResponse<Enterprise>> {
            override fun onResponse(
                call: Call<ApiResponse<Enterprise>>,
                response: Response<ApiResponse<Enterprise>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { enterprise ->
                        callback(Result.success(enterprise))
                    } ?: callback(Result.failure(Exception("企业信息为空")))
                } else {
                    val errorMessage = response.body()?.message ?: "获取企业信息失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Enterprise>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 更新企业信息
     */
    fun updateEnterprise(
        name: String?,
        address: String?,
        contactPerson: String?,
        contactPhone: String?,
        callback: (Result<Unit>) -> Unit
    ) {
        val request = UpdateEnterpriseRequest(name, address, contactPerson, contactPhone)
        enterpriseApiService.updateEnterprise(request).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback(Result.success(Unit))
                } else {
                    val errorMessage = response.body()?.message ?: "更新企业信息失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 获取企业统计数据
     */
    fun getEnterpriseStats(callback: (Result<EnterpriseStats>) -> Unit) {
        enterpriseApiService.getEnterpriseStats().enqueue(object : Callback<ApiResponse<EnterpriseStats>> {
            override fun onResponse(
                call: Call<ApiResponse<EnterpriseStats>>,
                response: Response<ApiResponse<EnterpriseStats>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { stats ->
                        callback(Result.success(stats))
                    } ?: callback(Result.failure(Exception("企业统计数据为空")))
                } else {
                    val errorMessage = response.body()?.message ?: "获取企业统计数据失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<EnterpriseStats>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 获取企业农场列表
     */
    fun getEnterpriseFarms(callback: (Result<List<EnterpriseFarm>>) -> Unit) {
        enterpriseApiService.getEnterpriseFarms().enqueue(object : Callback<ApiResponse<List<EnterpriseFarm>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<EnterpriseFarm>>>,
                response: Response<ApiResponse<List<EnterpriseFarm>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { farms ->
                        callback(Result.success(farms))
                    } ?: callback(Result.failure(Exception("企业农场列表为空")))
                } else {
                    val errorMessage = response.body()?.message ?: "获取企业农场列表失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<List<EnterpriseFarm>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 获取企业用户列表
     */
    fun getEnterpriseUsers(callback: (Result<List<EnterpriseUser>>) -> Unit) {
        enterpriseApiService.getEnterpriseUsers().enqueue(object : Callback<ApiResponse<List<EnterpriseUser>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<EnterpriseUser>>>,
                response: Response<ApiResponse<List<EnterpriseUser>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { users ->
                        callback(Result.success(users))
                    } ?: callback(Result.failure(Exception("企业用户列表为空")))
                } else {
                    val errorMessage = response.body()?.message ?: "获取企业用户列表失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<List<EnterpriseUser>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 生成企业邀请码
     */
    fun generateInvitedCode(callback: (Result<String>) -> Unit) {
        enterpriseApiService.generateInvitedCode().enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { code ->
                        callback(Result.success(code))
                    } ?: callback(Result.failure(Exception("邀请码生成失败")))
                } else {
                    val errorMessage = response.body()?.message ?: "生成邀请码失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
    
    /**
     * 获取最新企业邀请码
     */
    fun getLatestInvitedCode(callback: (Result<String>) -> Unit) {
        enterpriseApiService.getLatestInvitedCode().enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(
                call: Call<ApiResponse<String>>,
                response: Response<ApiResponse<String>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { code ->
                        callback(Result.success(code))
                    } ?: callback(Result.failure(Exception("获取邀请码失败")))
                } else {
                    val errorMessage = response.body()?.message ?: "获取邀请码失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}
