package com.example.modelfarm.network.services

import com.example.modelfarm.network.models.*
import retrofit2.Call
import retrofit2.http.*

/**
 * 工单管理API接口（STBackend v2.0）
 */
interface OrderApiService {

    @POST("/order")
    fun createOrder(@Body request: CreateOrderRequest): Call<ApiResponse<Int>>

    @GET("/order")
    fun getOrder(@Query("id") id: Int): Call<ApiResponse<OrderWithMedias>>

    @GET("/order/list")
    fun getAllOrders(): Call<ApiResponse<List<OrderWithMedias>>>

    @POST("/order/query")
    fun queryOrders(@Body request: OrderQueryRequest): Call<ApiResponse<PageResponse<OrderWithMedias>>>

    @PUT("/order")
    fun updateOrder(@Body request: UpdateOrderRequest): Call<ApiResponse<Void>>

    @DELETE("/order")
    fun deleteOrder(@Query("id") id: Int): Call<ApiResponse<Void>>

    @POST("/order/complete")
    fun completeOrder(@Query("id") id: Int): Call<ApiResponse<Void>>

    @POST("/order/dispatch")
    fun dispatchOrder(@Body request: DispatchOrderRequest): Call<ApiResponse<Void>>

    @POST("/order/accept")
    fun acceptOrder(@Body request: AcceptOrderRequest): Call<ApiResponse<Void>>

    @GET("/order/user")
    fun getOrdersByUserId(@Query("userId") userId: Int): Call<ApiResponse<List<OrderWithMedias>>>
}
