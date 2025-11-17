package com.example.modelfarm.network.models

/**
 * 企业信息模型
 */
data class Enterprise(
    val id: Int,
    val name: String,
    val address: String,
    val contactPhone: String,
    val contactPerson: String,
    // Backend returns status as string (e.g., "active"/"inactive" or "1"/"0").
    val status: String,
    val invitedCode: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)

/**
 * 创建企业请求模型
 */
data class CreateEnterpriseRequest(
    val name: String,
    val address: String,
    val contactPerson: String,
    val contactPhone: String
)

/**
 * 更新企业信息请求模型
 */
data class UpdateEnterpriseRequest(
    val name: String?,
    val address: String?,
    val contactPerson: String?,
    val contactPhone: String?
)

/**
 * 企业统计数据模型
 */
data class EnterpriseStats(
    val onlineDeviceCount: Int,
    val pendingOrderCount: Int,
    val userCount: Int,
    val deviceCount: Int,
    val farmCount: Int,
    val faultDeviceCount: Int
)

/**
 * 企业农场模型
 */
data class EnterpriseFarm(
    val id: Int,
    val enterpriseId: Int,
    val supervisorId: Int,
    val name: String,
    val address: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)

/**
 * 企业用户模型
 */
data class EnterpriseUser(
    val id: Int,
    val enterpriseId: Int,
    val username: String,
    val phone: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)
