package com.example.modelfarm.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.modelfarm.network.AuthManager;
import com.example.modelfarm.network.models.LoginResponse;
import com.example.modelfarm.network.repositories.UserRepository;

/**
 * 登录集成助手
 * 提供登录功能的API集成
 */
public class LoginIntegrationHelper {
    
    private Context context;
    private UserRepository userRepository;
    private AuthManager authManager;
    
    public LoginIntegrationHelper(Context context) {
        this.context = context;
        this.userRepository = new UserRepository(context);
        this.authManager = AuthManager.Companion.getInstance(context);
    }
    
    /**
     * 执行登录操作
     */
    public void performLogin(String phone, String password, LoginCallback callback) {
        // 输入验证
        if (phone == null || phone.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            callback.onError("请输入完整的登录信息");
            return;
        }
        
        // 验证手机号格式
        if (!isValidPhone(phone) && !isValidUsername(phone)) {
            callback.onError("请输入正确的手机号或用户名");
            return;
        }
        
        // 调用API进行登录
        userRepository.login(phone.trim(), password.trim(), new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(LoginResponse loginResponse) {
                // 保存登录信息
                authManager.saveLoginInfo(loginResponse);
                callback.onSuccess("登录成功，欢迎使用数智化养殖软件平台");
            }
            
            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }
    
    /**
     * 检查用户是否已登录
     */
    public boolean isUserLoggedIn() {
        return authManager.isLoggedIn();
    }
    
    /**
     * 获取当前用户ID
     */
    public int getCurrentUserId() {
        return authManager.getUserId();
    }
    
    /**
     * 获取当前企业ID
     */
    public int getCurrentEnterpriseId() {
        return authManager.getEnterpriseId();
    }
    
    /**
     * 用户登出
     */
    public void logout() {
        authManager.logout();
    }
    
    /**
     * 验证手机号格式
     */
    private boolean isValidPhone(String phone) {
        return phone.matches("^1[3-9]\\d{9}$");
    }
    
    /**
     * 验证用户名格式
     */
    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    /**
     * 登录回调接口
     */
    public interface LoginCallback {
        void onSuccess(String message);
        void onError(String errorMessage);
    }
    
}
