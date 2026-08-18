package com.example.modelfarm.network.models

import com.google.gson.annotations.SerializedName

/**
 * 工单信息模型（与后端 Order 字段一致；status 为字符串 "1"/"2"/"3"）
 */
data class Order(
    val id: Int,
    val enterpriseId: Int,
    val title: String,
    val description: String,
    @SerializedName("status")
    private val statusRaw: String,
    val creatorId: Int,
    val solvedId: Int?,
    val acceptedId: Int?,
    val isAccepted: Int,
    @SerializedName("compeletedAt")
    val completedAt: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    @SerializedName("solutionDescription")
    val solutionDescription: String? = null,
    @SerializedName("situationDescription")
    val situationDescription: String? = null
) {
    /** 供 Java 调用的工单状态：1 待处理 2 已完成 3 紧急 */
    fun getStatus(): Int = statusRaw.toIntOrNull() ?: 1
}

/**
 * 工单 + 媒体 URL（GET 工单详情/列表等接口返回）
 */
data class OrderWithMedias(
    val order: Order,
    val mediaUrls: Map<String, List<String>>? = null
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
 * 更新工单请求模型（solutionUrls / situationUrls 为 objectKey 列表）
 */
data class UpdateOrderRequest(
    val id: Int,
    val title: String,
    val description: String,
    val status: Int,
    val solution: String? = null,
    val solutionUrls: List<String>? = null,
    val situation: String? = null,
    val situationUrls: List<String>? = null
)

/**
 * 工单分页查询请求（POST /order/query）
 */
data class OrderQueryRequest(
    val pageNum: Int,
    val pageSize: Int,
    val title: String? = null,
    val description: String? = null,
    val status: Int? = null,
    val creatorId: Int? = null,
    val solvedId: Int? = null
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
 * 工单状态枚举
 */
object OrderStatus {
    const val PENDING = 1
    const val COMPLETED = 2
    const val URGENT = 3
    const val CLAIMED = 4
}
