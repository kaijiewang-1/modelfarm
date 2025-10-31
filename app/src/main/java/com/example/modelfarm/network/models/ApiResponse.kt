package com.example.modelfarm.network.models

/**
 * 通用API响应模型
 * 对应后端统一响应格式
 */
data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)

/**
 * 分页响应模型
 */
data class PageResponse<T>(
    val records: List<T>,
    val total: Int,
    val size: Int,
    val current: Int,
    val pages: Int
)

/**
 * 分页响应模型（设备数据专用）
 */
data class DeviceDataPageResponse<T>(
    val records: List<T>,
    val total: Int,
    val pageNum: Int,
    val pageSize: Int,
    val totalPages: Int
)
