package com.example.modelfarm.utils;

import com.example.modelfarm.network.models.Farm;
import com.example.modelfarm.network.models.FarmSite;

/**
 * 农场数据展示器
 * 用于处理农场和养殖点数据的显示逻辑
 */
public class FarmDataDisplay {
    
    /**
     * 获取农场显示信息
     */
    public static String getFarmDisplayInfo(Farm farm) {
        StringBuilder info = new StringBuilder();
        info.append("农场名称: ").append(farm.getName()).append("\n");
        info.append("农场地址: ").append(farm.getAddress()).append("\n");
        info.append("创建时间: ").append(farm.getCreatedAt()).append("\n");
        return info.toString();
    }
    
    /**
     * 获取养殖点显示信息
     */
    public static String getFarmSiteDisplayInfo(FarmSite farmSite) {
        StringBuilder info = new StringBuilder();
        info.append("养殖点名称: ").append(farmSite.getName()).append("\n");
        info.append("养殖点ID: ").append(farmSite.getId()).append("\n");
        info.append("所属农场ID: ").append(farmSite.getFarmId()).append("\n");
        info.append("创建时间: ").append(farmSite.getCreatedAt()).append("\n");
        
        // 解析properties信息
        if (farmSite.getProperties() != null && !farmSite.getProperties().isEmpty()) {
            info.append("备注信息: ").append(farmSite.getProperties()).append("\n");
        }
        
        return info.toString();
    }
    
    /**
     * 获取农场状态显示
     */
    public static String getFarmStatusDisplay(Farm farm) {
        // 根据农场的创建时间和更新时间判断状态
        String createdAt = farm.getCreatedAt();
        String updatedAt = farm.getUpdatedAt();
        
        if (createdAt != null && updatedAt != null) {
            if (createdAt.equals(updatedAt)) {
                return "新建";
            } else {
                return "活跃";
            }
        }
        
        return "未知";
    }
    
    /**
     * 获取养殖点状态显示
     */
    public static String getFarmSiteStatusDisplay(FarmSite farmSite) {
        // 根据养殖点的创建时间和更新时间判断状态
        String createdAt = farmSite.getCreatedAt();
        String updatedAt = farmSite.getUpdatedAt();
        
        if (createdAt != null && updatedAt != null) {
            if (createdAt.equals(updatedAt)) {
                return "新建";
            } else {
                return "活跃";
            }
        }
        
        return "未知";
    }
    
    /**
     * 获取农场面积显示（模拟数据）
     */
    public static String getFarmAreaDisplay(Farm farm) {
        // 这里可以根据农场的实际数据计算面积
        // 暂时返回模拟数据
        return "未知";
    }
    
    /**
     * 获取农场类型显示
     */
    public static String getFarmTypeDisplay(Farm farm) {
        // 根据农场名称或地址判断类型
        String name = farm.getName();
        String address = farm.getAddress();
        
        if (name != null) {
            if (name.contains("养殖")) {
                return "养殖场";
            } else if (name.contains("种植")) {
                return "种植场";
            } else if (name.contains("温室")) {
                return "温室大棚";
            }
        }
        
        return "智慧农场";
    }
    
    /**
     * 获取养殖点数量显示
     */
    public static String getFarmSiteCountDisplay(int count) {
        return count + "个养殖点";
    }
    
    /**
     * 获取设备数量显示
     */
    public static String getDeviceCountDisplay(int count) {
        return count + "台设备";
    }
}
