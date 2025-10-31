// 这是一个示例文件，展示如何在现有的login.java中集成API
// 请将以下代码添加到您的login.java文件中

package login;

import com.example.modelfarm.utils.LoginIntegrationHelper;

public class login extends AppCompatActivity {
    
    // 在类的顶部添加
    private LoginIntegrationHelper loginHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_login);
            
            // 初始化登录助手
            loginHelper = new LoginIntegrationHelper(this);
            
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
    
    /**
     * 执行登录操作 - 使用真实API
     */
    private void performLogin() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // 显示加载状态
        showLoadingState(true);
        
        // 使用登录助手进行API调用
        loginHelper.performLogin(phone, password, new LoginIntegrationHelper.LoginCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    showSuccessMessage(message);
                    
                    // 延迟跳转，让用户看到成功消息
                    new android.os.Handler().postDelayed(() -> {
                        Intent intent = new Intent(login.this, company.CompanyListActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }, 1000);
                    
                    showLoadingState(false);
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    showErrorWithAnimation(errorMessage);
                    showLoadingState(false);
                });
            }
        });
    }
    
    /**
     * 检查用户登录状态
     */
    private void checkLoginStatus() {
        if (loginHelper.isUserLoggedIn()) {
            // 用户已登录，直接跳转到主页面
            Intent intent = new Intent(this, company.CompanyListActivity.class);
            startActivity(intent);
            finish();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // 检查登录状态
        checkLoginStatus();
        
        // 页面恢复时重新验证输入（延迟执行确保UI已初始化）
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                validateInputFormat();
            }
        }, 100);
    }
}
