package com.example.modelfarm.network.models

/**
 * 通知信息模型
 */
data class Notification(
    val id: Int,
    val enterpriseId: Int,
    val title: String,
    val content: String,
    val type: Int,
    val isRead: Int,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
) {
    /**
     * 获取通知类型文本
     */
    fun getTypeText(): String {
        return when (type) {
            1 -> "系统通知"
            2 -> "警告通知"
            3 -> "消息通知"
            else -> "未知"
        }
    }
    
    /**
     * 获取通知优先级
     */
    fun getPriority(): String {
        return when (type) {
            1 -> "low"    // 系统通知
            2 -> "high"   // 警告通知
            3 -> "medium" // 消息通知
            else -> "low"
        }
    }
    
    /**
     * 是否已读
     */
    fun isReadStatus(): Boolean {
        return isRead == 1
    }
    
    /**
     * 获取时间差显示
     */
    fun getTimeAgo(): String {
        // 这里可以实现时间差计算逻辑
        // 暂时返回简单的时间显示
        return "刚刚"
    }
}

/**
 * 创建通知请求模型
 */
data class CreateNotificationRequest(
    val enterpriseId: Int,
    val title: String,
    val content: String,
    val type: Int
)

/**
 * 通知类型枚举
 */
object NotificationType {
    const val SYSTEM = 1
    const val WARNING = 2
    const val MESSAGE = 3
}

/**
 * 通知已读状态枚举
 */
object NotificationReadStatus {
    const val UNREAD = 0
    const val READ = 1
}
