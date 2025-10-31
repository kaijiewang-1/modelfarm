package com.example.modelfarm.utils;

import com.example.modelfarm.network.models.Device;
import com.example.modelfarm.network.models.Farm;
import com.example.modelfarm.network.models.FarmSite;
import com.example.modelfarm.network.models.Notification;
import com.example.modelfarm.network.models.User;

/**
 * 数据库结构适配器
 * 用于处理数据库记录到Android应用显示的数据映射
 */
public class DatabaseStructureAdapter {
    
    /**
     * 根据数据库记录创建设备显示信息
     */
    public static String createDeviceDisplayInfo(Device device) {
        StringBuilder info = new StringBuilder();
        info.append("设备名称: ").append(device.getName()).append("\n");
        info.append("设备类型: ").append(device.getTypeText()).append("\n");
        info.append("设备状态: ").append(device.getStatusText()).append("\n");
        info.append("MAC地址: ").append(device.getMac()).append("\n");
        
        if (device.getUrl() != null && !device.getUrl().isEmpty()) {
            info.append("推流地址: ").append(device.getUrl()).append("\n");
        }
        
        if (device.getProperties() != null && !device.getProperties().isEmpty()) {
            info.append("设备属性: ").append(device.getProperties().toString()).append("\n");
        }
        
        return info.toString();
    }
    
    /**
     * 根据数据库记录创建农场显示信息
     */
    public static String createFarmDisplayInfo(Farm farm) {
        StringBuilder info = new StringBuilder();
        info.append("农场名称: ").append(farm.getName()).append("\n");
        info.append("农场地址: ").append(farm.getAddress()).append("\n");
        info.append("企业ID: ").append(farm.getEnterpriseId()).append("\n");
        info.append("负责人ID: ").append(farm.getSupervisorId()).append("\n");
        info.append("创建时间: ").append(farm.getCreatedAt()).append("\n");
        return info.toString();
    }
    
    /**
     * 根据数据库记录创建养殖点显示信息
     */
    public static String createFarmSiteDisplayInfo(FarmSite farmSite) {
        StringBuilder info = new StringBuilder();
        info.append("养殖点名称: ").append(farmSite.getName()).append("\n");
        info.append("养殖点ID: ").append(farmSite.getId()).append("\n");
        info.append("所属农场ID: ").append(farmSite.getFarmId()).append("\n");
        info.append("企业ID: ").append(farmSite.getEnterpriseId()).append("\n");
        info.append("数量: ").append(farmSite.getSum()).append("\n");
        
        if (farmSite.getProperties() != null && !farmSite.getProperties().isEmpty()) {
            info.append("属性信息: ").append(farmSite.getProperties()).append("\n");
        }
        
        info.append("创建时间: ").append(farmSite.getCreatedAt()).append("\n");
        return info.toString();
    }
    
    /**
     * 根据数据库记录创建通知显示信息
     */
    public static String createNotificationDisplayInfo(Notification notification) {
        StringBuilder info = new StringBuilder();
        info.append("通知标题: ").append(notification.getTitle()).append("\n");
        info.append("通知内容: ").append(notification.getContent()).append("\n");
        info.append("通知类型: ").append(notification.getTypeText()).append("\n");
        info.append("优先级: ").append(notification.getPriority()).append("\n");
        info.append("是否已读: ").append(notification.isReadStatus() ? "是" : "否").append("\n");
        info.append("创建时间: ").append(notification.getCreatedAt()).append("\n");
        return info.toString();
    }
    
    /**
     * 根据数据库记录创建用户显示信息
     */
    public static String createUserDisplayInfo(User user) {
        StringBuilder info = new StringBuilder();
        info.append("用户ID: ").append(user.getId()).append("\n");
        info.append("用户名: ").append(user.getUsername()).append("\n");
        info.append("手机号: ").append(user.getPhone()).append("\n");
        info.append("企业ID: ").append(user.getEnterpriseId()).append("\n");
        info.append("状态: ").append(user.getStatus()).append("\n");
        info.append("创建时间: ").append(user.getCreatedAt()).append("\n");
        return info.toString();
    }
    
    /**
     * 获取设备状态颜色
     */
    public static int getDeviceStatusColor(Device device) {
        switch (device.getStatus()) {
            case 1: return 0xFF4CAF50; // 绿色 - 在线
            case 2: return 0xFFF44336; // 红色 - 离线
            case 3: return 0xFFFF9800; // 橙色 - 故障
            default: return 0xFF9E9E9E; // 灰色 - 未知
        }
    }
    
    /**
     * 获取通知优先级颜色
     */
    public static int getNotificationPriorityColor(Notification notification) {
        switch (notification.getType()) {
            case 1: return 0xFF2196F3; // 蓝色 - 系统通知
            case 2: return 0xFFF44336; // 红色 - 警告通知
            case 3: return 0xFFFF9800; // 橙色 - 消息通知
            default: return 0xFF9E9E9E; // 灰色 - 未知
        }
    }
    
    /**
     * 获取农场状态颜色
     */
    public static int getFarmStatusColor(Farm farm) {
        // 根据农场的创建时间和更新时间判断状态
        String createdAt = farm.getCreatedAt();
        String updatedAt = farm.getUpdatedAt();
        
        if (createdAt != null && updatedAt != null) {
            if (createdAt.equals(updatedAt)) {
                return 0xFF4CAF50; // 绿色 - 新建
            } else {
                return 0xFF2196F3; // 蓝色 - 活跃
            }
        }
        
        return 0xFF9E9E9E; // 灰色 - 未知
    }
    
    /**
     * 检查设备是否在线
     */
    public static boolean isDeviceOnline(Device device) {
        return device.getStatus() == 1;
    }
    
    /**
     * 检查通知是否已读
     */

    
    /**
     * 检查农场是否活跃
     */
    public static boolean isFarmActive(Farm farm) {
        String createdAt = farm.getCreatedAt();
        String updatedAt = farm.getUpdatedAt();
        
        if (createdAt != null && updatedAt != null) {
            return !createdAt.equals(updatedAt);
        }
        
        return false;
    }
}
