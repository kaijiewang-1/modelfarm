package com.example.modelfarm.network.repositories

import android.content.Context
import com.example.modelfarm.network.RetrofitClient
import com.example.modelfarm.network.models.*
import com.example.modelfarm.network.services.OrderApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 工单数据仓库
 * 封装工单相关的API调用
 */
class OrderRepository(private val context: Context) {

    private val orderApiService: OrderApiService = RetrofitClient.create(context, OrderApiService::class.java)

    /**
     * 创建工单
     */
    fun createOrder(
        title: String,
        description: String,
        status: Int,
        callback: (Result<Int>) -> Unit
    ) {
        val request = CreateOrderRequest(title, description, status)
        orderApiService.createOrder(request).enqueue(object : Callback<ApiResponse<Int>> {
            override fun onResponse(
                call: Call<ApiResponse<Int>>,
                response: Response<ApiResponse<Int>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { orderId ->
                        callback(Result.success(orderId))
                    } ?: callback(Result.failure(Exception("工单创建失败")))
                } else {
                    val errorMessage = response.body()?.message ?: "工单创建失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    /**
     * 获取工单信息
     */
    fun getOrder(
        id: Int,
        callback: (Result<Order>) -> Unit
    ) {
        orderApiService.getOrder(id).enqueue(object : Callback<ApiResponse<Order>> {
            override fun onResponse(
                call: Call<ApiResponse<Order>>,
                response: Response<ApiResponse<Order>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { order ->
                        callback(Result.success(order))
                    } ?: callback(Result.failure(Exception("工单不存在")))
                } else {
                    val errorMessage = response.body()?.message ?: "获取工单失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Order>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    /**
     * 获取所有工单列表
     */
    fun getAllOrders(
        callback: (Result<List<Order>>) -> Unit
    ) {
        orderApiService.getAllOrders().enqueue(object : Callback<ApiResponse<List<Order>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Order>>>,
                response: Response<ApiResponse<List<Order>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { orders ->
                        callback(Result.success(orders))
                    } ?: callback(Result.success(emptyList()))
                } else {
                    val errorMessage = response.body()?.message ?: "获取工单列表失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Order>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    /**
     * 更新工单信息
     */
    fun updateOrder(
        id: Int,
        title: String,
        description: String,
        status: Int,
        callback: (Result<Unit>) -> Unit
    ) {
        val request = UpdateOrderRequest(id, title, description, status)
        orderApiService.updateOrder(request).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>,
                response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback(Result.success(Unit))
                } else {
                    val errorMessage = response.body()?.message ?: "更新工单失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    /**
     * 删除工单
     */
    fun deleteOrder(
        id: Int,
        callback: (Result<Unit>) -> Unit
    ) {
        orderApiService.deleteOrder(id).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>,
                response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback(Result.success(Unit))
                } else {
                    val errorMessage = response.body()?.message ?: "删除工单失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    /**
     * 标记工单为已完成
     */
    fun completeOrder(
        id: Int,
        callback: (Result<Unit>) -> Unit
    ) {
        orderApiService.completeOrder(id).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>,
                response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback(Result.success(Unit))
                } else {
                    val errorMessage = response.body()?.message ?: "工单标记失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    /**
     * 派单给指定用户
     */
    fun dispatchOrder(
        orderId: Int,
        userId: Int,
        callback: (Result<Unit>) -> Unit
    ) {
        val request = DispatchOrderRequest(orderId, userId)
        orderApiService.dispatchOrder(request).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>,
                response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback(Result.success(Unit))
                } else {
                    val errorMessage = response.body()?.message ?: "派单失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    /**
     * 用户认领工单
     */
    fun acceptOrder(
        orderId: Int,
        callback: (Result<Unit>) -> Unit
    ) {
        val request = AcceptOrderRequest(orderId)
        orderApiService.acceptOrder(request).enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>,
                response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    callback(Result.success(Unit))
                } else {
                    val errorMessage = response.body()?.message ?: "工单认领失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    /**
     * 根据用户ID获取认领的工单列表
     */
    fun getOrdersByUserId(
        userId: Int,
        callback: (Result<List<Order>>) -> Unit
    ) {
        orderApiService.getOrdersByUserId(userId).enqueue(object : Callback<ApiResponse<List<Order>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Order>>>,
                response: Response<ApiResponse<List<Order>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { orders ->
                        callback(Result.success(orders))
                    } ?: callback(Result.success(emptyList()))
                } else {
                    val errorMessage = response.body()?.message ?: "获取用户工单列表失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Order>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}