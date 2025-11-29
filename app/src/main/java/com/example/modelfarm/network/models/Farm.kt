package com.example.modelfarm.network.models

/**
 * 农场信息模型
 */
data class Farm(
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
 * 创建农场请求模型
 */
data class CreateFarmRequest(
    val name: String,
    val address: String,
    val supervisorId: String
)

/**
 * 更新农场请求模型
 */
data class UpdateFarmRequest(
    val id: Int,
    val name: String?,
    val address: String?,
    val supervisorId: Int?
)

/**
 * 养殖点（农场站点）模型
 */
data class FarmSite(
    val id: Int,
    val enterpriseId: Int,
    val farmId: Int,
    val name: String,
    var sum: Int,
    val properties: Map<String, Any>?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)

/**
 * 创建养殖点请求模型
 */
data class CreateFarmSiteRequest(
    val farmId: Int,
    val name: String,
    val sum: Int
)

/**
 * 更新养殖点请求模型
 */
data class UpdateFarmSiteRequest(
    val farmId: Int?,
    val siteId: Int?,
    val name: String?,
    val sum: Int?,
    val properties: Map<String, Any>?
)
