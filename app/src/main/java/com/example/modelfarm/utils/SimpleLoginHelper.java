package com.example.modelfarm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

/**
 * 简化的登录助手
 * 使用本地存储进行登录验证，避免复杂的网络依赖
 */
public class SimpleLoginHelper {
    
    private Context context;
    private SharedPreferences prefs;
    
    public SimpleLoginHelper(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences("SmartFarmPrefs", Context.MODE_PRIVATE);
    }
    
    /**
     * 登录回调接口
     */
    public interface LoginCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }
    
    /**
     * 注册回调接口
     */
    public interface RegisterCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }
    
    /**
     * 简化的登录验证
     * 使用本地存储的用户数据
     */
    public void performLogin(String phone, String password, LoginCallback callback) {
        // 模拟网络延迟
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (validateLogin(phone, password)) {
                    // 保存登录状态
                    saveLoginState(phone);
                    callback.onSuccess("登录成功，欢迎使用智慧养殖系统");
                } else {
                    callback.onError("用户名或密码错误，请重试");
                }
            }
        }, 1000);
    }
    
    /**
     * 简化的用户注册
     */
    public void registerUser(String username, String phone, String password, RegisterCallback callback) {
        // 模拟网络延迟
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 检查用户是否已存在
                if (isUserExists(phone)) {
                    callback.onError("该手机号已注册，请直接登录");
                    return;
                }
                
                // 保存用户信息
                saveUserInfo(username, phone, password);
                callback.onSuccess("注册成功，请使用新账号登录");
            }
        }, 1000);
    }
    
    /**
     * 验证登录信息
     */
    private boolean validateLogin(String phone, String password) {
        // 检查用户是否存在
        if (!isUserExists(phone)) {
            return false;
        }
        
        // 验证密码
        String savedPassword = prefs.getString("user_password_" + phone, "");
        return password.equals(savedPassword);
    }
    
    /**
     * 检查用户是否存在
     */
    private boolean isUserExists(String phone) {
        return prefs.getBoolean("user_exists_" + phone, false);
    }
    
    /**
     * 保存用户信息
     */
    private void saveUserInfo(String username, String phone, String password) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("user_exists_" + phone, true);
        editor.putString("user_username_" + phone, username);
        editor.putString("user_password_" + phone, password);
        editor.apply();
    }
    
    /**
     * 保存登录状态
     */
    private void saveLoginState(String phone) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putString("current_user_phone", phone);
        editor.putString("current_user_username", prefs.getString("user_username_" + phone, ""));
        editor.apply();
    }
    
    /**
     * 验证手机号格式
     */
    public boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // 简单的手机号验证：11位数字
        return phone.matches("^1[3-9]\\d{9}$");
    }
    
    /**
     * 验证密码格式
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        // 密码至少6位
        return password.length() >= 6;
    }
    
    /**
     * 验证用户名格式
     */
    public boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        // 用户名至少2位
        return username.length() >= 2;
    }
    
    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        return prefs.getBoolean("is_logged_in", false);
    }
    
    /**
     * 登出
     */
    public void logout() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("is_logged_in", false);
        editor.remove("current_user_phone");
        editor.remove("current_user_username");
        editor.apply();
    }
    
    /**
     * 获取当前用户手机号
     */
    public String getCurrentUserPhone() {
        return prefs.getString("current_user_phone", "");
    }
    
    /**
     * 获取当前用户名
     */
    public String getCurrentUsername() {
        return prefs.getString("current_user_username", "");
    }
}
