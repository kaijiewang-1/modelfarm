package com.example.modelfarm.network.services

import com.example.modelfarm.network.models.*
import retrofit2.Call
import retrofit2.http.*

/**
 * 通知管理API接口
 */
interface NotificationApiService {
    
    /**
     * 创建通知
     */
    @POST("/notification")
    fun createNotification(@Body request: CreateNotificationRequest): Call<ApiResponse<Nothing>>
    
    /**
     * 标记所有通知为已读
     */
    @PUT("/notification/readAll")
    fun markAllAsRead(): Call<ApiResponse<Nothing>>
    
    /**
     * 标记单个通知为已读
     */
    @PUT("/notification/readOne")
    fun markAsRead(@Query("notificationId") notificationId: Int): Call<ApiResponse<Nothing>>
    
    /**
     * 删除通知
     */
    @DELETE("/notification")
    fun deleteNotification(@Query("notificationId") notificationId: Int): Call<ApiResponse<Nothing>>
    
    /**
     * 获取所有未删除通知
     */
    @GET("/notification/allNotDelete")
    fun getAllNotifications(): Call<ApiResponse<List<Notification>>>
    
    /**
     * 获取所有未删除且未读通知
     */
    @GET("/notification/allNotDeleteAndNotRead")
    fun getUnreadNotifications(): Call<ApiResponse<List<Notification>>>
}
