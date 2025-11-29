package com.example.modelfarm.network.services

import com.example.modelfarm.network.models.*
import retrofit2.Call
import retrofit2.http.*

/**
 * 工单管理API接口
 */
interface OrderApiService {

    /**
     * 创建工单
     */
    @POST("/order")
    fun createOrder(@Body request: CreateOrderRequest): Call<ApiResponse<Int>>

    /**
     * 获取工单信息
     */
    @GET("/order")
    fun getOrder(@Query("id") id: Int): Call<ApiResponse<Order>>

    /**
     * 获取所有工单列表
     */
    @GET("/order/list")
    fun getAllOrders(): Call<ApiResponse<List<Order>>>

    /**
     * 更新工单信息
     */
    @PUT("/order")
    fun updateOrder(@Body request: UpdateOrderRequest): Call<ApiResponse<Nothing>>

    /**
     * 删除工单
     */
    @DELETE("/order")
    fun deleteOrder(@Query("id") id: Int): Call<ApiResponse<Void>>

    /**
     * 标记工单为已完成
     */
    @POST("/order/complete")
    fun completeOrder(@Query("id") id: Int): Call<ApiResponse<Void>>

    /**
     * 派单给指定用户
     */
    @POST("/order/dispatch")
    fun dispatchOrder(@Body request: DispatchOrderRequest): Call<ApiResponse<Void>>

    /**
     * 用户认领工单
     */
    @POST("/order/accept")
    fun acceptOrder(@Body request: AcceptOrderRequest): Call<ApiResponse<Void>>

    /**
     * 根据用户ID获取认领的工单列表
     */
    @GET("/order/user")
    fun getOrdersByUserId(@Query("userId") userId: Int): Call<ApiResponse<List<Order>>>

    /**
     * 工单打卡
     */
    @POST("/order/checkin")
    fun checkInOrder(@Body request: CheckInOrderRequest): Call<ApiResponse<Void>>
}