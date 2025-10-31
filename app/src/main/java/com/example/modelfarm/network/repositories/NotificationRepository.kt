package com.example.modelfarm.network.repositories

import android.content.Context
import com.example.modelfarm.network.RetrofitClient
import com.example.modelfarm.network.models.*
import com.example.modelfarm.network.services.NotificationApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 通知数据仓库
 * 封装通知相关的API调用
 */
class NotificationRepository(private val context: Context) {
    
    private val notificationApiService: NotificationApiService = RetrofitClient.create(context, NotificationApiService::class.java)
    
    /**
     * 获取通知列表回调接口
     */
    interface NotificationListCallback {
        fun onSuccess(notifications: List<Notification>)
        fun onError(errorMessage: String)
    }
    
    /**
     * 创建通知回调接口
     */
    interface CreateNotificationCallback {
        fun onSuccess()
        fun onError(errorMessage: String)
    }
    
    /**
     * 获取所有通知
     */
    fun getAllNotifications(callback: NotificationListCallback) {
        notificationApiService.getAllNotifications().enqueue(object : Callback<ApiResponse<List<Notification>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Notification>>>,
                response: Response<ApiResponse<List<Notification>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { notifications ->
                        callback.onSuccess(notifications)
                    } ?: callback.onError("通知列表为空")
                } else {
                    val errorMessage = response.body()?.message ?: "获取通知列表失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<List<Notification>>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 获取未读通知
     */
    fun getUnreadNotifications(callback: NotificationListCallback) {
        notificationApiService.getUnreadNotifications().enqueue(object : Callback<ApiResponse<List<Notification>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Notification>>>,
                response: Response<ApiResponse<List<Notification>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { notifications ->
                        callback.onSuccess(notifications)
                    } ?: callback.onError("未读通知列表为空")
                } else {
                    val errorMessage = response.body()?.message ?: "获取未读通知列表失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<List<Notification>>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 创建通知
     */
    fun createNotification(
        enterpriseId: Int,
        title: String,
        content: String,
        type: Int,
        callback: CreateNotificationCallback
    ) {
        val request = CreateNotificationRequest(enterpriseId, title, content, type)
        notificationApiService.createNotification(request).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess()
                } else {
                    val errorMessage = response.body()?.message ?: "创建通知失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 标记所有通知为已读
     */
    fun markAllAsRead(callback: CreateNotificationCallback) {
        notificationApiService.markAllAsRead().enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess()
                } else {
                    val errorMessage = response.body()?.message ?: "标记所有通知为已读失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 标记单个通知为已读
     */
    fun markAsRead(
        notificationId: Int,
        callback: CreateNotificationCallback
    ) {
        notificationApiService.markAsRead(notificationId).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess()
                } else {
                    val errorMessage = response.body()?.message ?: "标记通知为已读失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
    
    /**
     * 删除通知
     */
    fun deleteNotification(
        notificationId: Int,
        callback: CreateNotificationCallback
    ) {
        notificationApiService.deleteNotification(notificationId).enqueue(object : Callback<ApiResponse<Nothing>> {
            override fun onResponse(
                call: Call<ApiResponse<Nothing>>,
                response: Response<ApiResponse<Nothing>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback.onSuccess()
                } else {
                    val errorMessage = response.body()?.message ?: "删除通知失败"
                    callback.onError(errorMessage)
                }
            }
            
            override fun onFailure(call: Call<ApiResponse<Nothing>>, t: Throwable) {
                callback.onError(t.message ?: "网络请求失败")
            }
        })
    }
}
