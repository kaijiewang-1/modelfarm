
package logins;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.modelfarm.R;
import com.example.modelfarm.network.AuthManager;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.*;
import com.example.modelfarm.network.services.UserApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
// import com.example.modelfarm.utils.DatabaseLoginHelper; // 删除 DatabaseLoginHelper 相关声明变量、初始化代码、registerUser 等调用。
// 建议后续如需支持注册流程时，仿照 login.java 使用 Retrofit+UserApiService 完成注册接口对接。
import com.example.modelfarm.network.models.LoginResponse;
import com.example.modelfarm.network.models.ApiResponse;
public class register extends AppCompatActivity {

    private EditText etPhone;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etUsername;
    private Button btnRegister;
    private TextView tvGoLogin;
    
    // 删除 DatabaseLoginHelper 相关声明变量、初始化代码、registerUser 等调用。
    // 建议后续如需支持注册流程时，仿照 login.java 使用 Retrofit+UserApiService 完成注册接口对接。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_register);

            initViews();
            setupClickListeners();
        } catch (Exception e) {
            android.util.Log.e("RegisterActivity", "初始化失败: " + e.getMessage(), e);
            Toast.makeText(this, "页面初始化失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        // etUsername = findViewById(R.id.etUsername); // 暂时注释掉不存在的资源引用
        // 暂时注释掉不存在的资源引用
        // etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        // tvGoLogin = findViewById(R.id.tvGoLogin);
        
        // 删除 DatabaseLoginHelper 相关声明变量、初始化代码、registerUser 等调用。
        // 建议后续如需支持注册流程时，仿照 login.java 使用 Retrofit+UserApiService 完成注册接口对接。
    }
    private void navigateToLogin(boolean withAnimation) {
        Intent intent = new Intent(register.this, login.class);
        startActivity(intent);
        if (withAnimation) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        finish();
    }
    private void setupClickListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername=findViewById(R.id.etName);

                String username = etUsername.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                etPassword=findViewById(R.id.etPwd);
                String password = etPassword.getText().toString().trim();

                // 输入验证
                if (username.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(register.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                UserApiService userApi = RetrofitClient.create(register.this, UserApiService.class);
                RegisterRequest request=new RegisterRequest(username,password,phone);

                userApi.register(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                        runOnUiThread(() -> {

                            if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                                Toast.makeText(register.this, "注册成功", Toast.LENGTH_LONG).show();

                                // 进入主界面
                                new android.os.Handler().postDelayed(() -> {
                                    navigateToLogin(true);
                                }, 1000);
                            } else {
                                Toast.makeText(register.this, "注册失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                        Toast.makeText(register.this, "注册失败", Toast.LENGTH_LONG).show();
                    }
                });
                // 验证用户名格式
                // 删除 DatabaseLoginHelper 相关声明变量、初始化代码、registerUser 等调用。
                // 建议后续如需支持注册流程时，仿照 login.java 使用 Retrofit+UserApiService 完成注册接口对接。

                // 验证手机号格式
                // 删除 DatabaseLoginHelper 相关声明变量、初始化代码、registerUser 等调用。
                // 建议后续如需支持注册流程时，仿照 login.java 使用 Retrofit+UserApiService 完成注册接口对接。

                // 验证密码格式
                // 删除 DatabaseLoginHelper 相关声明变量、初始化代码、registerUser 等调用。
                // 建议后续如需支持注册流程时，仿照 login.java 使用 Retrofit+UserApiService 完成注册接口对接。

                // 使用数据库注册
                // 删除 DatabaseLoginHelper 相关声明变量、初始化代码、registerUser 等调用。
                // 建议后续如需支持注册流程时，仿照 login.java 使用 Retrofit+UserApiService 完成注册接口对接。
            }
        });

        // 暂时注释掉不存在的按钮点击事件
        // tvGoLogin.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         // 返回登录页面
        //         Intent intent = new Intent(register.this, login.class);
        //         startActivity(intent);
        //         finish();
        //     }
        // });
    }
}