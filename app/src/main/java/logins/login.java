package logins;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.DashboardActivity;
import com.example.modelfarm.R;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.AuthManager;
import com.example.modelfarm.network.models.LoginRequest;
import com.example.modelfarm.network.models.LoginResponse;
import com.example.modelfarm.network.services.UserApiService;
import com.example.modelfarm.network.models.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 智慧养殖系统登录页面
 * 支持手机号/用户名登录，密码登录
 * 集成企业选择功能
 * 增强用户体验和人性化交互
 */
public class login extends AppCompatActivity {

    // UI组件
    private EditText etPhone;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvGoRegister;
    private TextView tvReset;
    private TextView tvForgotPassword;
    private ProgressBar progressBar;
    private ImageView ivLogo;
    private TextView tvWelcome;
    
    // 数据存储
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "SmartFarmPrefs";
    private static final String KEY_LAST_USERNAME = "last_username";
    private static final String KEY_REMEMBER_PASSWORD = "remember_password";
    
    // 动画
    private Animation shakeAnimation;
    private Animation fadeInAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_login);

            initViews();
            initAnimations();
            loadSavedCredentials();
            setupClickListeners();
            setupUI();
            setupTextWatchers();
        } catch (Exception e) {
            android.util.Log.e("LoginActivity", "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "应用初始化失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void initViews() {
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoRegister = findViewById(R.id.tvGoRegister);
        tvReset = findViewById(R.id.tvReset);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressBar = findViewById(R.id.progressBar);
        ivLogo = findViewById(R.id.ivLogo);
        // tvWelcome = findViewById(R.id.tvWelcome); // 暂时注释掉，布局中没有这个ID
        
        // 初始化SharedPreferences
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        // 初始化数据库登录助手
        // databaseLoginHelper = new DatabaseLoginHelper(this);
    }

    private void initAnimations() {
        try {
            // 初始化动画
            shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
            fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        } catch (Exception e) {
            // 如果动画加载失败，创建简单的动画
            android.util.Log.e("LoginActivity", "Failed to load animations: " + e.getMessage());
            shakeAnimation = null;
            fadeInAnimation = null;
        }
    }

    private void loadSavedCredentials() {
        // 加载保存的用户名
        String lastUsername = preferences.getString(KEY_LAST_USERNAME, "");
        if (!lastUsername.isEmpty()) {
            etPhone.setText(lastUsername);
            etPhone.setSelection(lastUsername.length());
        }
    }

    private void setupUI() {
        // 设置输入框提示
        etPhone.setHint("请输入手机号或用户名");
        etPassword.setHint("请输入密码");
        
        // 设置登录按钮文本
        btnLogin.setText("登录智慧养殖系统");
        
        // 设置欢迎文本
        // tvWelcome.setText("欢迎使用智慧养殖系统"); // 暂时注释掉
        
        // 初始状态隐藏进度条
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setupTextWatchers() {
        // 手机号输入监听
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 实时验证输入格式
                validateInputFormat();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 密码输入监听
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 实时验证输入格式
                validateInputFormat();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加按钮点击动画
                if (fadeInAnimation != null) {
                    v.startAnimation(fadeInAnimation);
                }
                performLogin();
            }
        });

        tvGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 添加点击动画
                    if (fadeInAnimation != null) {
                        v.startAnimation(fadeInAnimation);
                    }
                    Intent intent = new Intent(login.this, register.class);
                    startActivity(intent);
                    // 添加页面切换动画
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } catch (Exception e) {
                    android.util.Log.e("LoginActivity", "跳转到注册页面失败: " + e.getMessage(), e);
                    Toast.makeText(login.this, "跳转失败，请重试", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加点击动画
                if (fadeInAnimation != null) {
                    v.startAnimation(fadeInAnimation);
                }
                clearInputs();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加点击动画
                if (fadeInAnimation != null) {
                    v.startAnimation(fadeInAnimation);
                }
                showForgotPasswordDialog();
            }
        });
    }

    /**
     * 执行登录操作
     */
    private void performLogin() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 输入验证
        if (phone.isEmpty() || password.isEmpty()) {
            showErrorWithAnimation("请输入完整的登录信息");
            return;
        }
        if (!isValidPhone(phone) && !isValidUsername(phone)) {
            showErrorWithAnimation("请输入正确的手机号或用户名");
            return;
        }
        showLoadingState(true);
        // -------- 新增：Retrofit 登录请求 --------
        UserApiService userApi = RetrofitClient.create(this, UserApiService.class);
        LoginRequest request = new LoginRequest(phone, password);
        userApi.login(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                runOnUiThread(() -> {
                    showLoadingState(false);
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        showSuccessMessage("登录成功，欢迎使用智慧养殖系统");
                        // 保存 token、userId 信息
                        LoginResponse loginData = response.body().getData();
                        if (loginData != null) {
                            // 统一走 AuthManager，供拦截器自动携带 satoken
                            AuthManager.getInstance(login.this).saveLoginInfo(loginData);
                            // 兼容保留本地偏好（可选）
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("auth_token", loginData.getSatoken().getTokenValue());
                            editor.putInt("current_user_id", loginData.getUserId());
                            editor.putInt("enterprise_id", loginData.getEnterpriseId());
                            editor.apply();
                        }
                        saveCredentials(phone);
                        // 进入主界面
                        new android.os.Handler().postDelayed(() -> {
                            Intent intent = new Intent(login.this, company.CompanyListActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }, 1000);
                    } else {
                        String msg = response.body()!=null ? response.body().getMessage() : "登录失败，请重试";
                        showErrorWithAnimation(msg);
                    }
                });
            }
            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                runOnUiThread(() -> {
                    showLoadingState(false);
                    showErrorWithAnimation("网络错误: " + t.getMessage());
                });
            }
        });
    }

    /**
     * 验证登录信息
     */
    private boolean validateLogin(String phone, String password) {
        // 模拟用户数据验证
        return (phone.equals("13800138000") && password.equals("123456")) ||
               (phone.equals("admin") && password.equals("admin123")) ||
               (phone.equals("farmer") && password.equals("farmer123"));
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
     * 显示忘记密码对话框
     */
    private void showForgotPasswordDialog() {
        Toast.makeText(login.this, "请联系管理员重置密码", Toast.LENGTH_LONG).show();
    }

    /**
     * 实时验证输入格式
     */
    private void validateInputFormat() {
        if (etPhone == null || etPassword == null || btnLogin == null) {
            return; // 如果UI组件未初始化，直接返回
        }
        
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // 动态启用/禁用登录按钮
        boolean canLogin = !phone.isEmpty() && !password.isEmpty() && 
                          (isValidPhone(phone) || isValidUsername(phone));
        btnLogin.setEnabled(canLogin);
        btnLogin.setAlpha(canLogin ? 1.0f : 0.6f);
    }

    /**
     * 显示错误信息并添加动画
     */
    private void showErrorWithAnimation(String message) {
        Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
        // 添加震动动画
        if (shakeAnimation != null && etPhone != null) {
            etPhone.startAnimation(shakeAnimation);
        }
    }

    /**
     * 显示成功消息
     */
    private void showSuccessMessage(String message) {
        Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示/隐藏加载状态
     */
    private void showLoadingState(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        btnLogin.setEnabled(!show);
        btnLogin.setText(show ? "登录中..." : "登录智慧养殖系统");
    }

    /**
     * 保存用户凭据
     */
    private void saveCredentials(String username) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LAST_USERNAME, username);
        editor.apply();
    }

    /**
     * 清空输入框（增强版）
     */
    private void clearInputs() {
        etPhone.setText("");
        etPassword.setText("");
        etPhone.clearFocus();
        etPassword.clearFocus();
        
        // 添加清空动画
        if (fadeInAnimation != null) {
            etPhone.startAnimation(fadeInAnimation);
            etPassword.startAnimation(fadeInAnimation);
        }
        
        Toast.makeText(login.this, "输入信息已清空", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 页面恢复时重新验证输入（延迟执行确保UI已初始化）
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                validateInputFormat();
            }
        }, 100);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 页面暂停时隐藏键盘
        if (getCurrentFocus() != null) {
            android.view.inputmethod.InputMethodManager imm = 
                (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}