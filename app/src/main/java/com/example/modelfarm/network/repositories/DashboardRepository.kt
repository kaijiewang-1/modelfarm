package com.example.modelfarm.network.repositories

import android.content.Context
import com.example.modelfarm.network.RetrofitClient
import com.example.modelfarm.network.models.EnterpriseStats
import com.example.modelfarm.network.services.EnterpriseApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 仪表板数据仓库
 * 封装仪表板相关的API调用
 */
class DashboardRepository(private val context: Context) {
    
    private val enterpriseApiService: EnterpriseApiService = RetrofitClient.create(context, EnterpriseApiService::class.java)
    
    /**
     * 获取企业统计数据回调接口
     */
    interface EnterpriseStatsCallback {
        fun onSuccess(stats: EnterpriseStats)
        fun onError(errorMessage: String)
    }
    
    /**
     * 获取企业统计数据
     */
    fun getEnterpriseStats(callback: EnterpriseStatsCallback) {
        enterpriseApiService.getEnterpriseStats().enqueue(object : Callback<com.example.modelfarm.network.models.ApiResponse<EnterpriseStats>> {
            override fun onResponse(
                call: Call<com.example.modelfarm.network.models.ApiResponse<EnterpriseStats>>,
                response: Response<com.example.modelfarm.network.models.ApiResponse<EnterpriseStats>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { stats ->
                        callback.onSuccess(stats)
                    } ?: callback.onError("企业统计数据为空")
                } else {
                    val errorMessage = response.body()?.message ?: "获取企业统计数据失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<com.example.modelfarm.network.models.ApiResponse<EnterpriseStats>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
}
