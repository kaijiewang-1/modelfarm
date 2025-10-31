package com.example.modelfarm.network.services

import com.example.modelfarm.network.models.*
import retrofit2.Call
import retrofit2.http.*

/**
 * 设备管理API接口
 */
interface DeviceApiService {
    
    /**
     * 创建设备
     */
    @POST("/device")
    fun createDevice(@Body request: CreateDeviceRequest): Call<ApiResponse<Int>>
    
    /**
     * 获取设备信息
     */
    @GET("/device")
    fun getDevice(@Query("deviceId") deviceId: Int): Call<ApiResponse<Device>>
    
    /**
     * 删除设备
     */
    @DELETE("/device")
    fun deleteDevice(@Query("deviceId") deviceId: Int): Call<ApiResponse<Nothing>>
    
    /**
     * 更新设备
     */
    @PUT("/device")
    fun updateDevice(@Body request: UpdateDeviceRequest): Call<ApiResponse<Nothing>>
    
    /**
     * 获取设备列表（分页）
     */
    @GET("/device/page")
    fun getDeviceList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
        @Query("name") name: String? = null,
        @Query("siteId") siteId: Int? = null,
        @Query("type") type: Int? = null,
        @Query("url") url: String? = null
    ): Call<ApiResponse<PageResponse<Device>>>
}

/**
 * 设备数据管理API接口
 */
interface DeviceDataApiService {
    
    /**
     * 创建设备数据
     */
    @POST("/device-data")
    fun createDeviceData(@Body request: CreateDeviceDataRequest): Call<ApiResponse<Nothing>>
    
    /**
     * 获取设备数据（分页）
     */
    @GET("/device-data")
    fun getDeviceData(
        @Query("deviceId") deviceId: Int,
        @Query("startTime") startTime: String? = null,
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): Call<ApiResponse<DeviceDataPageResponse<DeviceData>>>
    
    /**
     * 获取设备所有数据
     */
    @GET("/device-data/allById")
    fun getAllDeviceData(@Query("deviceId") deviceId: Int): Call<ApiResponse<List<DeviceData>>>
    
    /**
     * 获取设备最新数据
     */
    @GET("/device-data/latest")
    fun getLatestDeviceData(@Query("deviceId") deviceId: Int): Call<ApiResponse<DeviceData>>
}

/**
 * 设备类型管理API接口
 */
interface DeviceTypeApiService {
    
    /**
     * 创建设备类型
     */
    @POST("/device-type")
    fun createDeviceType(@Body request: CreateDeviceTypeRequest): Call<ApiResponse<Nothing>>
    
    /**
     * 获取所有设备类型
     */
    @GET("/device-type")
    fun getAllDeviceTypes(): Call<ApiResponse<List<DeviceType>>>
    
    /**
     * 删除设备类型
     */
    @DELETE("/device-type")
    fun deleteDeviceType(@Query("typeId") typeId: Int): Call<ApiResponse<Nothing>>
    
    /**
     * 更新设备类型
     */
    @PUT("/device-type")
    fun updateDeviceType(@Body request: UpdateDeviceTypeRequest): Call<ApiResponse<Nothing>>
}
