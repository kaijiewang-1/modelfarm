package com.example.modelfarm.network.models

import com.google.gson.annotations.SerializedName

/**
 * 工单信息模型
 */
data class Order(
    val id: Int,
    val enterpriseId: Int,
    val title: String,
    val description: String,
    val status: Int,
    val creatorId: Int,
    val solvedId: Int?,
    val acceptedId: Int?,
    val isAccepted: Int,
    @SerializedName("compeletedAt") // 后端字段名拼写错误，使用注解映射
    val completedAt: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)

/**
 * 创建工单请求模型
 */
data class CreateOrderRequest(
    val title: String,
    val description: String,
    val status: Int
)

/**
 * 更新工单请求模型
 */
data class UpdateOrderRequest(
    val id: Int,
    val title: String,
    val description: String,
    val status: Int
)

/**
 * 派单请求模型
 */
data class DispatchOrderRequest(
    val orderId: Int,
    val userId: Int
)

/**
 * 认领工单请求模型
 */
data class AcceptOrderRequest(
    val orderId: Int
)

/**
 * 工单打卡请求模型
 */
data class CheckInOrderRequest(
    val orderId: Int
)

/**
 * 工单状态枚举
 */
object OrderStatus {
    const val PENDING = 1
    const val COMPLETED = 2
    const val URGENT = 3
    const val CLAIMED = 4
}