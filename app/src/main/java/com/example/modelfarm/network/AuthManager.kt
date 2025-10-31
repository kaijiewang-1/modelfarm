package com.example.modelfarm.network

import android.content.Context
import android.content.SharedPreferences
import com.example.modelfarm.network.models.LoginResponse
import com.example.modelfarm.network.models.SaToken

/**
 * 认证管理器
 * 负责管理用户登录状态和token
 */
class AuthManager private constructor(context: Context) {
    
    companion object {
        private const val PREFS_NAME = "SmartFarmAuth"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_ENTERPRISE_ID = "enterprise_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        
        @Volatile
        private var INSTANCE: AuthManager? = null
        
        @JvmStatic
        fun getInstance(context: Context): AuthManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    /**
     * 保存登录信息
     */
    fun saveLoginInfo(loginResponse: LoginResponse) {
        prefs.edit().apply {
            putString(KEY_TOKEN, loginResponse.satoken.tokenValue)
            putInt(KEY_USER_ID, loginResponse.userId)
            putInt(KEY_ENTERPRISE_ID, loginResponse.enterpriseId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
        
        // 保存认证token到SharedPreferences
        // RetrofitClient会自动从AuthManager获取token
    }
    
    /**
     * 获取当前token
     */
    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }
    
    /**
     * 获取当前用户ID
     */
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }
    
    /**
     * 获取当前企业ID
     */
    fun getEnterpriseId(): Int {
        return prefs.getInt(KEY_ENTERPRISE_ID, -1)
    }
    
    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && getToken() != null
    }
    
    /**
     * 登出
     */
    fun logout() {
        prefs.edit().clear().apply()
        // RetrofitClient会自动从AuthManager获取token
    }
    
    /**
     * 更新token
     */
    fun updateToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
        // RetrofitClient会自动从AuthManager获取token
    }
}
