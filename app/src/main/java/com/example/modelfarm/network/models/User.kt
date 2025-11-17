package com.example.modelfarm.network.models

/**
 * 用户登录请求模型
 */
data class LoginRequest(
    val phone: String,
    val password: String
)

/**
 * 用户注册请求模型
 */
data class RegisterRequest(
    val username: String,
    val password: String,
    val phone: String
)

/**
 * 用户登录响应模型
 */
data class LoginResponse(
    val userId: Int,
    val enterpriseId: Int,
    val satoken: SaToken
)
data class RegisterResponse(
    val userId: Long,
    val enterpriseId: Long,
    val satoken: SaToken
)

/**
 * Sa-Token模型
 */
data class SaToken(
    val tokenName: String,
    val tokenValue: String,
    val isLogin: Boolean,
    val loginId: Int,
    val loginType: String,
    val tokenTimeout: Long
)

/**
 * 用户信息模型
 */
data class User(
    val id: Int,
    val enterpriseId: Int,
    val username: String,
    val phone: String,
    // Backend returns status as string (e.g., "active"/"inactive" or "1"/"0").
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)

/**
 * 用户信息更新请求模型
 */
data class UpdateUserRequest(
    val username: String?,
    val phone: String?
)

/**
 * 密码更新请求模型
 */
data class UpdatePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)

/**
 * 加入企业请求模型
 */
data class JoinEnterpriseRequest(
    val invitedCode: String
)
