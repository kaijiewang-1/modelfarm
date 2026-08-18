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
 */
class OrderRepository(private val context: Context) {

    private val orderApiService: OrderApiService = RetrofitClient.create(context, OrderApiService::class.java)

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

    fun getOrder(
        id: Int,
        callback: (Result<OrderWithMedias>) -> Unit
    ) {
        orderApiService.getOrder(id).enqueue(object : Callback<ApiResponse<OrderWithMedias>> {
            override fun onResponse(
                call: Call<ApiResponse<OrderWithMedias>>,
                response: Response<ApiResponse<OrderWithMedias>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { callback(Result.success(it)) }
                        ?: callback(Result.failure(Exception("工单不存在")))
                } else {
                    val errorMessage = response.body()?.message ?: "获取工单失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<OrderWithMedias>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun getAllOrders(
        callback: (Result<List<Order>>) -> Unit
    ) {
        orderApiService.getAllOrders().enqueue(object : Callback<ApiResponse<List<OrderWithMedias>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<OrderWithMedias>>>,
                response: Response<ApiResponse<List<OrderWithMedias>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    val orders = response.body()?.data?.map { it.order } ?: emptyList()
                    callback(Result.success(orders))
                } else {
                    val errorMessage = response.body()?.message ?: "获取工单列表失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<OrderWithMedias>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun queryOrders(
        request: OrderQueryRequest,
        callback: (Result<PageResponse<OrderWithMedias>>) -> Unit
    ) {
        orderApiService.queryOrders(request).enqueue(object : Callback<ApiResponse<PageResponse<OrderWithMedias>>> {
            override fun onResponse(
                call: Call<ApiResponse<PageResponse<OrderWithMedias>>>,
                response: Response<ApiResponse<PageResponse<OrderWithMedias>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    response.body()?.data?.let { callback(Result.success(it)) }
                        ?: callback(Result.failure(Exception("查询工单失败")))
                } else {
                    callback(Result.failure(Exception(response.body()?.message ?: "查询工单失败")))
                }
            }

            override fun onFailure(call: Call<ApiResponse<PageResponse<OrderWithMedias>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun updateOrder(
        request: UpdateOrderRequest,
        callback: (Result<Unit>) -> Unit
    ) {
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

    fun getOrdersByUserId(
        userId: Int,
        callback: (Result<List<Order>>) -> Unit
    ) {
        orderApiService.getOrdersByUserId(userId).enqueue(object : Callback<ApiResponse<List<OrderWithMedias>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<OrderWithMedias>>>,
                response: Response<ApiResponse<List<OrderWithMedias>>>
            ) {
                if (response.isSuccessful && response.body()?.code == 200) {
                    val orders = response.body()?.data?.map { it.order } ?: emptyList()
                    callback(Result.success(orders))
                } else {
                    val errorMessage = response.body()?.message ?: "获取用户工单列表失败"
                    callback(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<OrderWithMedias>>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}
