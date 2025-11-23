package com.example.modelfarm.network.repositories

import android.content.Context
import com.example.modelfarm.network.RetrofitClient
import com.example.modelfarm.network.models.*
import com.example.modelfarm.network.services.FarmApiService
import com.example.modelfarm.network.services.FarmSiteApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 农场数据仓库
 * 封装农场相关的API调用
 */
class FarmRepository(private val context: Context) {
    
    private val farmApiService: FarmApiService = RetrofitClient.create(context, FarmApiService::class.java)
    private val farmSiteApiService: FarmSiteApiService = RetrofitClient.create(context, FarmSiteApiService::class.java)
    
    /**
     * 创建农场回调接口
     */
    interface CreateFarmCallback {
        fun onSuccess(farmId: Int)
        fun onError(errorMessage: String)
    }
    
    /**
     * 获取农场信息回调接口
     */
    interface FarmInfoCallback {
        fun onSuccess(farm: Farm)
        fun onError(errorMessage: String)
    }
    
    /**
     * 获取农场列表回调接口
     */
    interface FarmListCallback {
        fun onSuccess(farms: List<Farm>)
        fun onError(errorMessage: String)
    }
    
    /**
     * 获取养殖点列表回调接口
     */
    interface FarmSiteListCallback {
        fun onSuccess(farmSites: List<FarmSite>)
        fun onError(errorMessage: String)
    }
    
    /**
     * 获取养殖点信息回调接口
     */
    interface FarmSiteInfoCallback {
        fun onSuccess(farmSite: FarmSite)
        fun onError(errorMessage: String)
    }
    
    /**
     * 创建农场
     */
    fun createFarm(
        name: String,
        address: String,
        supervisorId: String,
        callback: CreateFarmCallback
    ) {
        val request = CreateFarmRequest(name, address, supervisorId)
        farmApiService.createFarm(request).enqueue(object : Callback<ApiResponse<Int>> {
            override fun onResponse(
                call: Call<ApiResponse<Int>>,
                response: Response<ApiResponse<Int>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { farmId ->
                        callback.onSuccess(farmId)
                    } ?: callback.onError("创建农场响应数据为空")
                } else {
                    val errorMessage = response.body()?.message ?: "创建农场失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 获取农场信息
     */
    fun getFarm(
        farmId: Int,
        callback: FarmInfoCallback
    ) {
        farmApiService.getFarm(farmId).enqueue(object : Callback<ApiResponse<Farm>> {
            override fun onResponse(
                call: Call<ApiResponse<Farm>>,
                response: Response<ApiResponse<Farm>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { farm ->
                        callback.onSuccess(farm)
                    } ?: callback.onError("农场信息为空")
                } else {
                    val errorMessage = response.body()?.message ?: "获取农场信息失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Farm>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 获取企业农场列表
     */
    fun getEnterpriseFarms(callback: FarmListCallback) {
        farmApiService.getEnterpriseFarms().enqueue(object : Callback<ApiResponse<List<Farm>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Farm>>>,
                response: Response<ApiResponse<List<Farm>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { farms ->
                        callback.onSuccess(farms)
                    } ?: callback.onError("企业农场列表为空")
                } else {
                    val errorMessage = response.body()?.message ?: "获取企业农场列表失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<List<Farm>>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 更新农场信息
     */
    fun updateFarm(
        id: Int,
        name: String?,
        address: String?,
        supervisorId: Int?,
        callback: CreateFarmCallback
    ) {
        val request = UpdateFarmRequest(id, name, address, supervisorId)
        farmApiService.updateFarm(request).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess(id)
                } else {
                    val errorMessage = response.body()?.message ?: "更新农场信息失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 删除农场
     */
    fun deleteFarm(
        farmId: Int,
        callback: CreateFarmCallback
    ) {
        farmApiService.deleteFarm(farmId).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess(farmId)
                } else {
                    val errorMessage = response.body()?.message ?: "删除农场失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 获取农场养殖点列表
     */
    fun getFarmSites(
        farmId: Int,
        callback: FarmSiteListCallback
    ) {
        farmApiService.getFarmSites(farmId).enqueue(object : Callback<ApiResponse<List<FarmSite>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<FarmSite>>>,
                response: Response<ApiResponse<List<FarmSite>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { farmSites ->
                        callback.onSuccess(farmSites)
                    } ?: callback.onError("农场养殖点列表为空")
                } else {
                    val errorMessage = response.body()?.message ?: "获取农场养殖点列表失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<List<FarmSite>>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 创建养殖点
     */
    fun createFarmSite(
        farmId: Int,
        name: String,
        sum: Int,
        callback: CreateFarmCallback
    ) {
        val request = CreateFarmSiteRequest(farmId, name, sum)
        farmSiteApiService.createFarmSite(request).enqueue(object : Callback<ApiResponse<Int>> {
            override fun onResponse(
                call: Call<ApiResponse<Int>>,
                response: Response<ApiResponse<Int>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { siteId ->
                        callback.onSuccess(siteId)
                    } ?: callback.onError("创建养殖点响应数据为空")
                } else {
                    val errorMessage = response.body()?.message ?: "创建养殖点失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 获取养殖点信息
     */
    fun getFarmSite(
        farmSiteId: Int,
        callback: FarmSiteInfoCallback
    ) {
        farmSiteApiService.getFarmSite(farmSiteId).enqueue(object : Callback<ApiResponse<FarmSite>> {
            override fun onResponse(
                call: Call<ApiResponse<FarmSite>>,
                response: Response<ApiResponse<FarmSite>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { farmSite ->
                        callback.onSuccess(farmSite)
                    } ?: callback.onError("养殖点信息为空")
                } else {
                    val errorMessage = response.body()?.message ?: "获取养殖点信息失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<FarmSite>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 更新养殖点信息
     */
    fun updateFarmSite(
        farmId: Int?,
        siteId: Int?,
        name: String?,
        sum: Int?,
        properties: Map<String, Any>?,
        callback: CreateFarmCallback
    ) {
        val request = UpdateFarmSiteRequest(farmId, siteId, name, sum, properties)
        farmSiteApiService.updateFarmSite(request).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess(siteId ?: 0)
                } else {
                    val errorMessage = response.body()?.message ?: "更新养殖点信息失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 删除养殖点
     */
    fun deleteFarmSite(
        farmSiteId: Int,
        callback: CreateFarmCallback
    ) {
        farmSiteApiService.deleteFarmSite(farmSiteId).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>,
                response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess(farmSiteId)
                } else {
                    val errorMessage = response.body()?.message ?: "删除养殖点失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
}
