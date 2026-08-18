package com.example.modelfarm.network

/**
 * 从 OBS 完整 URL 解析 objectKey（如 order/image/xxx.jpg）
 */
object OrderMediaUtil {
    @JvmStatic
    fun objectKeyFromUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        val idx = url.indexOf("/order/")
        return if (idx >= 0) url.substring(idx + 1) else null
    }
}
