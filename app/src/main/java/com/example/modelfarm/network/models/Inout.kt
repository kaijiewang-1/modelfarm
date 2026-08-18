package com.example.modelfarm.network.models

/**
 * 动物出入库月度统计数据模型
 */
data class MonthInoutData(
    val month: Int,
    val sum: Int,
    val inCount: Int,
    val outCount: Int
)

