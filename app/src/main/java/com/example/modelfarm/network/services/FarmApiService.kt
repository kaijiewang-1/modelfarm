package com.example.modelfarm.network.services

import com.example.modelfarm.network.models.*
import retrofit2.Call
import retrofit2.http.*

/**
 * 农场管理API接口
 */
interface FarmApiService {
    
    /**
     * 创建农场
     */
    @POST("/farm")
    fun createFarm(@Body request: CreateFarmRequest): Call<ApiResponse<Int>>
    
    /**
     * 获取农场信息
     */
    @GET("/farm")
    fun getFarm(@Query("farmId") farmId: Int): Call<ApiResponse<Farm>>
    
    /**
     * 更新农场信息
     */
    @PUT("/farm")
    fun updateFarm(@Body request: UpdateFarmRequest): Call<ApiResponse<Nothing>>
    
    /**
     * 删除农场
     */
    @DELETE("/farm")
    fun deleteFarm(@Query("farmId") farmId: Int): Call<ApiResponse<Nothing>>
    
    /**
     * 获取企业农场列表
     */
    @GET("/enterprise/farms")
    fun getEnterpriseFarms(): Call<ApiResponse<List<Farm>>>
    
    /**
     * 获取农场养殖点列表
     */
    @GET("/farm/sites")
    fun getFarmSites(@Query("farmId") farmId: Int): Call<ApiResponse<List<FarmSite>>>
}

/**
 * 养殖点管理API接口
 */
interface FarmSiteApiService {
    
    /**
     * 创建养殖点
     */
    @POST("/farmSite")
    fun createFarmSite(@Body request: CreateFarmSiteRequest): Call<ApiResponse<Int>>
    
    /**
     * 获取养殖点信息
     */
    @GET("/farmSite")
    fun getFarmSite(@Query("farmSiteId") farmSiteId: Int): Call<ApiResponse<FarmSite>>
    
    /**
     * 更新养殖点信息
     */
    @PUT("/farmSite")
    fun updateFarmSite(@Body request: UpdateFarmSiteRequest): Call<ApiResponse<Nothing>>
    
    /**
     * 删除养殖点
     */
    @DELETE("/farmSite")
    fun deleteFarmSite(@Query("farmSiteId") farmSiteId: Int): Call<ApiResponse<Void>>
    
    /**
     * 获取养殖点设备列表
     */
    @GET("/farmSite/devices")
    fun getFarmSiteDevices(@Query("farmSiteId") farmSiteId: Int): Call<ApiResponse<List<Device>>>
    
    /**
     * 获取养殖点摄像头列表
     */
    @GET("/farmSite/camera")
    fun getFarmSiteCameras(@Query("farmSiteId") farmSiteId: Int): Call<ApiResponse<List<Device>>>
    
    /**
     * 根据年份和养殖点获取动物出入库月度统计数据
     */
    @GET("/inout/yearSiteData")
    fun getYearSiteData(
        @Query("year") year: Int,
        @Query("siteId") siteId: Int
    ): Call<ApiResponse<List<MonthInoutData>>>
}
