package com.example.modelfarm.network.models

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
    val compeletedAt: String?,
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
 * 工单状态枚举
 */
object OrderStatus {
    const val PENDING = 1
    const val COMPLETED = 2
    const val URGENT = 3
}
