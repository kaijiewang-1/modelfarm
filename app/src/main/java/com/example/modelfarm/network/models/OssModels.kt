package com.example.modelfarm.network.models

/**
 * POST /oss/signature 请求体
 */
data class OssSignatureRequest(
    val count: Int,
    val mediaProperties: List<OssMediaProperty>
)

data class OssMediaProperty(
    val fileType: String,
    val mimeType: String,
    val fileSize: Long? = null,
    val fileName: String
)

/**
 * 预签名上传单项
 */
data class OssSignatureItem(
    val uploadUrl: String,
    val contentType: String,
    val fileName: String,
    val objectKey: String
)
