package com.example.modelfarm.network.models

import com.example.modelfarm.R

/**
 * 设备信息模型
 */
data class Device(
    val id: Int,
    val enterpriseId: Int,
    val name: String,
    val pushName: String?,
    val siteId: Int,
    val mac: String,
    val type: Int,
    val status: Int,
    val url: String?,
    val properties: Map<String, Any>?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
) {
    /**
     * 获取设备状态文本
     */
    fun getStatusText(): String {
        return when (status) {
            1 -> "在线"
            2 -> "离线"
            3 -> "故障"
            else -> "未知"
        }
    }
    
    /**
     * 获取设备类型文本
     */
    fun getTypeText(): String {
        return when (type) {
            1 -> "摄像头"
            2 -> "传感器"
            3 -> "控制器"
            else -> "其他"
        }
    }
    
    /**
     * 获取设备图标资源
     */
    fun getIconResource(): Int {
        return when (type) {
            1 -> R.drawable.ic_camera
            2 -> R.drawable.ic_thermometer
            3 -> R.drawable.ic_control
            else -> R.drawable.ic_device
        }
    }
    
    /**
     * 获取设备显示值
     */
    fun getDisplayValue(): String {
        return when (type) {
            1 -> if (url != null && url.isNotEmpty()) "已连接" else "无信号"
            2 -> if (status == 1) "正常" else "无数据"
            3 -> if (status == 1) "运行中" else "停止"
            else -> "未知"
        }
    }
}

/**
 * 创建设备请求模型
 */
data class CreateDeviceRequest(
    val name: String,
    val siteId: Int,
    val mac: String,
    val type: Int,
    val pushName: String?
)

/**
 * 更新设备请求模型
 */
data class UpdateDeviceRequest(
    val deviceId: Int,
    val siteId: Int,
    val name: String,
    val pushName: String?,
    val type: Int,
    val status: Int,
    val properties: Map<String, Any>?
)

/**
 * 设备类型模型
 */
data class DeviceType(
    val id: Int,
    val typeId: Int,
    val typeName: String,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)

/**
 * 创建设备类型请求模型
 */
data class CreateDeviceTypeRequest(
    val typeId: Int,
    val typeName: String
)

/**
 * 更新设备类型请求模型
 */
data class UpdateDeviceTypeRequest(
    val typeId: Int,
    val typeName: String
)

/**
 * 设备数据模型
 */
data class DeviceData(
    val id: String,
    val deviceId: Int,
    val data: Map<String, Any>,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?
)

/**
 * 创建设备数据请求模型
 */
data class CreateDeviceDataRequest(
    val deviceId: Int,
    val data: Map<String, Any>?
)

/**
 * 设备状态枚举
 */
object DeviceStatus {
    const val ONLINE = 1
    const val OFFLINE = 2
    const val FAULT = 3
}

/**
 * 设备类型枚举
 */
object DeviceTypeEnum {
    const val CAMERA = 1
    const val SENSOR = 2
    const val OTHER = 3
}
