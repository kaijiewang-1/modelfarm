package com.example.modelfarm.utils;

import com.example.modelfarm.network.models.User;

/**
 * 用户数据展示器
 * 用于处理用户信息的显示逻辑
 */
public class UserDataDisplay {
    
    /**
     * 获取用户显示名称
     */
    public static String getUserDisplayName(User user) {
        if (!user.getUsername().isEmpty()) {
            return user.getUsername();
        }
        
        if (!user.getPhone().isEmpty()) {
            return user.getPhone();
        }
        
        return "未知用户";
    }
    
    /**
     * 获取用户状态显示
     */
    public static String getUserStatusDisplay(User user) {
        String status = user.getStatus();
        if (status == null) return "未知";
        String s = status.trim().toLowerCase();
        if ("1".equals(s) || "active".equals(s) || "enabled".equals(s) || "正常".equals(s)) {
            return "活跃";
        }
        if ("0".equals(s) || "inactive".equals(s) || "disabled".equals(s) || "禁用".equals(s)) {
            return "非活跃";
        }
        if ("-1".equals(s) || "paused".equals(s) || "suspended".equals(s)) {
            return "已暂停";
        }
        return status;
    }
    
    /**
     * 获取用户角色显示
     */
    public static String getUserRoleDisplay(User user) {
        // 根据用户ID或企业ID判断角色
        if (user.getEnterpriseId() > 0) {
            return "企业用户";
        }
        
        return "普通用户";
    }
    
    /**
     * 获取用户创建时间显示
     */
    public static String getUserCreateTimeDisplay(User user) {
        if (user.getCreatedAt() != null && !user.getCreatedAt().isEmpty()) {
            return user.getCreatedAt();
        }
        
        return "未知时间";
    }
    
    /**
     * 获取用户最后更新时间显示
     */
    public static String getUserUpdateTimeDisplay(User user) {
        if (user.getUpdatedAt() != null && !user.getUpdatedAt().isEmpty()) {
            return user.getUpdatedAt();
        }
        
        return "未知时间";
    }
    
    /**
     * 获取用户完整信息显示
     */
    public static String getUserFullInfoDisplay(User user) {
        StringBuilder info = new StringBuilder();
        info.append("用户名: ").append(getUserDisplayName(user)).append("\n");
        info.append("手机号: ").append(user.getPhone() != null ? user.getPhone() : "未知").append("\n");
        info.append("状态: ").append(getUserStatusDisplay(user)).append("\n");
        info.append("角色: ").append(getUserRoleDisplay(user)).append("\n");
        info.append("创建时间: ").append(getUserCreateTimeDisplay(user)).append("\n");
        return info.toString();
    }
    
    /**
     * 检查用户是否有效
     */
    public static boolean isValidUser(User user) {
        return user != null && 
               user.getUsername() != null && 
               !user.getUsername().isEmpty() &&
               user.getPhone() != null && 
               !user.getPhone().isEmpty();
    }
    
    /**
     * 获取用户权限级别
     */
    public static String getUserPermissionLevel(User user) {
        if (user.getEnterpriseId() > 0) {
            return "企业管理员";
        }
        
        return "普通用户";
    }
}
