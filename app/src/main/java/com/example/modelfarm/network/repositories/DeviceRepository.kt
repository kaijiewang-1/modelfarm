package com.example.modelfarm.network.repositories

import android.content.Context
import com.example.modelfarm.network.RetrofitClient
import com.example.modelfarm.network.models.*
import com.example.modelfarm.network.services.DeviceApiService
import com.example.modelfarm.network.services.DeviceDataApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 设备数据仓库
 * 封装设备相关的API调用
 */
class DeviceRepository(private val context: Context) {
    
    private val deviceApiService: DeviceApiService = RetrofitClient.create(context, DeviceApiService::class.java)
    private val deviceDataApiService: DeviceDataApiService = RetrofitClient.create(context, DeviceDataApiService::class.java)
    
    /**
     * 创建设备回调接口
     */
    interface CreateDeviceCallback {
        fun onSuccess(deviceId: Int)
        fun onError(errorMessage: String)
    }
    
    /**
     * 获取设备列表回调接口
     */
    interface DeviceListCallback {
        fun onSuccess(devices: List<Device>)
        fun onError(errorMessage: String)
    }
    
    /**
     * 获取设备信息回调接口
     */
    interface DeviceInfoCallback {
        fun onSuccess(device: Device)
        fun onError(errorMessage: String)
    }
    
    /**
     * 创建设备数据回调接口
     */
    interface CreateDeviceDataCallback {
        fun onSuccess()
        fun onError(errorMessage: String)
    }
    
    /**
     * 获取设备数据回调接口
     */
    interface DeviceDataCallback {
        fun onSuccess(deviceData: List<DeviceData>)
        fun onError(errorMessage: String)
    }
    
    /**
     * 获取最新设备数据回调接口
     */
    interface LatestDeviceDataCallback {
        fun onSuccess(deviceData: DeviceData)
        fun onError(errorMessage: String)
    }
    
    /**
     * 创建设备
     */
    fun createDevice(
        request: CreateDeviceRequest,
        callback: CreateDeviceCallback
    ) {
        deviceApiService.createDevice(request).enqueue(object : Callback<ApiResponse<Int>> {
            override fun onResponse(
                call: Call<ApiResponse<Int>>,
                response: Response<ApiResponse<Int>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { deviceId ->
                        callback.onSuccess(deviceId)
                    } ?: callback.onError("创建设备响应数据为空")
                } else {
                    val errorMessage = response.body()?.message ?: "创建设备失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 获取设备信息
     */
    fun getDevice(
        deviceId: Int,
        callback: DeviceInfoCallback
    ) {
        deviceApiService.getDevice(deviceId).enqueue(object : Callback<ApiResponse<Device>> {
            override fun onResponse(
                call: Call<ApiResponse<Device>>,
                response: Response<ApiResponse<Device>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { device ->
                        callback.onSuccess(device)
                    } ?: callback.onError("设备信息为空")
                } else {
                    val errorMessage = response.body()?.message ?: "获取设备信息失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Device>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 获取设备列表
     */
    fun getDeviceList(
        pageNum: Int = 1,
        pageSize: Int = 10,
        name: String? = null,
        siteId: Int? = null,
        type: Int? = null,
        url: String? = null,
        callback: DeviceListCallback
    ) {
        deviceApiService.getDeviceList(pageNum, pageSize, name, siteId, type, url)
            .enqueue(object : Callback<ApiResponse<PageResponse<Device>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PageResponse<Device>>>,
                    response: Response<ApiResponse<PageResponse<Device>>>
                ) {
                    if (response.isSuccessful && response.body()?.code == 200) {
                        response.body()?.data?.let { pageResponse ->
                            callback.onSuccess(pageResponse.records)
                        } ?: callback.onError("设备列表为空")
                    } else {
                        val errorMessage = response.body()?.message ?: "获取设备列表失败"
                        callback.onError(errorMessage)
                    }
                }
                
                override fun onFailure(call: Call<ApiResponse<PageResponse<Device>>>, t: Throwable) {
                    callback.onError(t.message ?: "网络请求失败")
                }
            })
    }
    
    /**
     * 更新设备
     */
    fun updateDevice(
        request: UpdateDeviceRequest,
        callback: CreateDeviceCallback
    ) {
        deviceApiService.updateDevice(request).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess(request.deviceId)
                } else {
                    val errorMessage = response.body()?.message ?: "更新设备失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 删除设备
     */
    fun deleteDevice(
        deviceId: Int,
        callback: CreateDeviceCallback
    ) {
        deviceApiService.deleteDevice(deviceId).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess(deviceId)
                } else {
                    val errorMessage = response.body()?.message ?: "删除设备失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 创建设备数据
     */
    fun createDeviceData(
        request: CreateDeviceDataRequest,
        callback: CreateDeviceDataCallback
    ) {
        deviceDataApiService.createDeviceData(request).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess()
                } else {
                    val errorMessage = response.body()?.message ?: "创建设备数据失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 获取设备数据
     */
    fun getDeviceData(
        deviceId: Int,
        startTime: String? = null,
        pageNum: Int = 1,
        pageSize: Int = 10,
        callback: DeviceDataCallback
    ) {
        deviceDataApiService.getDeviceData(deviceId, startTime, pageNum, pageSize)
            .enqueue(object : Callback<ApiResponse<DeviceDataPageResponse<DeviceData>>> {
                override fun onResponse(
                    call: Call<ApiResponse<DeviceDataPageResponse<DeviceData>>>,
                    response: Response<ApiResponse<DeviceDataPageResponse<DeviceData>>>
                ) {
                    if (response.isSuccessful && response.body()?.code == 200) {
                        response.body()?.data?.let { pageResponse ->
                            callback.onSuccess(pageResponse.records)
                        } ?: callback.onError("设备数据为空")
                    } else {
                        val errorMessage = response.body()?.message ?: "获取设备数据失败"
                        callback.onError(errorMessage)
                    }
                }
                
                override fun onFailure(call: Call<ApiResponse<DeviceDataPageResponse<DeviceData>>>, t: Throwable) {
                    callback.onError(t.message ?: "网络请求失败")
                }
            })
    }
    
    /**
     * 获取设备最新数据
     */
    fun getLatestDeviceData(
        deviceId: Int,
        callback: LatestDeviceDataCallback
    ) {
        deviceDataApiService.getLatestDeviceData(deviceId).enqueue(object : Callback<ApiResponse<DeviceData>> {
            override fun onResponse(
                call: Call<ApiResponse<DeviceData>>,
                response: Response<ApiResponse<DeviceData>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { deviceData ->
                        callback.onSuccess(deviceData)
                    } ?: callback.onError("设备最新数据为空")
                } else {
                    val errorMessage = response.body()?.message ?: "获取设备最新数据失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<DeviceData>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
}
