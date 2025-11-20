
package logins;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.modelfarm.R;
import com.example.modelfarm.network.AuthManager;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.LoginResponse;
import com.example.modelfarm.network.models.RegisterRequest;
import com.example.modelfarm.network.services.UserApiService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import company.CompanyListActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户注册页面
 * 对接后端 /user/register 接口，注册成功后自动完成登录
 */
public class register extends AppCompatActivity {

    private TextInputLayout tilName;
    private TextInputLayout tilPhone;
    private TextInputLayout tilPwd;
    private TextInputLayout tilPwd2;
    private TextInputEditText etName;
    private TextInputEditText etPhone;
    private TextInputEditText etPwd;
    private TextInputEditText etPwd2;
    private MaterialButton btnRegister;
    private TextView tvToLogin;
    private Animation fadeInAnimation;

    private UserApiService userApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_register);
            initViews();
            initAnimations();
            initApi();
            setupClickListeners();
        } catch (Exception e) {
            android.util.Log.e("RegisterActivity", "初始化失败: " + e.getMessage(), e);
            Toast.makeText(this, "页面初始化失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {
        tilName = findViewById(R.id.tilName);
        tilPhone = findViewById(R.id.tilPhone);
        tilPwd = findViewById(R.id.tilPwd);
        tilPwd2 = findViewById(R.id.tilPwd2);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPwd = findViewById(R.id.etPwd);
        etPwd2 = findViewById(R.id.etPwd2);

        btnRegister = findViewById(R.id.btnRegister);
        tvToLogin = findViewById(R.id.tvToLogin);
    }

    private void initAnimations() {
        try {
            fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        } catch (Exception e) {
            fadeInAnimation = null;
        }
    }

    private void initApi() {
        userApiService = RetrofitClient.create(this, UserApiService.class);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> {
            if (fadeInAnimation != null) {
                v.startAnimation(fadeInAnimation);
            }
            attemptRegister();
        });

        tvToLogin.setOnClickListener(v -> {
            if (fadeInAnimation != null) {
                v.startAnimation(fadeInAnimation);
            }
            finish();
        });
    }

    private void attemptRegister() {
        clearErrors();

        String username = getText(etName);
        String phone = getText(etPhone);
        String password = getText(etPwd);
        String confirmPassword = getText(etPwd2);

        boolean isValid = true;

        if (TextUtils.isEmpty(username)) {
            setError(tilName, "请输入姓名");
            isValid = false;
        } else if (!isValidUsername(username)) {
            setError(tilName, "姓名应为2-20位，可包含中文、字母、数字");
            isValid = false;
        }

        if (TextUtils.isEmpty(phone)) {
            setError(tilPhone, "请输入手机号");
            isValid = false;
        } else if (!isValidPhone(phone)) {
            setError(tilPhone, "请输入正确的手机号");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            setError(tilPwd, "请输入密码");
            isValid = false;
        } else if (!isValidPassword(password)) {
            setError(tilPwd, "密码需6-20位，包含字母和数字");
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            setError(tilPwd2, "请再次输入密码");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            setError(tilPwd2, "两次密码输入不一致");
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(this, "请完善注册信息", Toast.LENGTH_SHORT).show();
            return;
        }

        performRegister(username, password, phone);
    }

    private void performRegister(String username, String password, String phone) {
        showLoadingState(true);

        RegisterRequest request = new RegisterRequest(username, password, phone);
        userApiService.register(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                runOnUiThread(() -> {
                    showLoadingState(false);
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        LoginResponse loginResponse = response.body().getData();
                        if (loginResponse != null) {
                            AuthManager.getInstance(register.this).saveLoginInfo(loginResponse);
                            Toast.makeText(register.this, "注册成功，已自动登录", Toast.LENGTH_SHORT).show();
                            navigateToCompanySelection();
                        } else {
                            Toast.makeText(register.this, "注册成功但未返回登录信息", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String message = response.body() != null ? response.body().getMessage() : "注册失败，请稍后重试";
                        Toast.makeText(register.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                runOnUiThread(() -> {
                    showLoadingState(false);
                    Toast.makeText(register.this, "网络异常: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void navigateToCompanySelection() {
        Intent intent = new Intent(this, CompanyListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void showLoadingState(boolean show) {
        btnRegister.setEnabled(!show);
        btnRegister.setText(show ? "注册中..." : "注册");
    }

    private void clearErrors() {
        setError(tilName, null);
        setError(tilPhone, null);
        setError(tilPwd, null);
        setError(tilPwd2, null);
    }

    private void setError(TextInputLayout layout, String error) {
        if (layout == null) return;
        layout.setError(error);
        layout.setErrorEnabled(!TextUtils.isEmpty(error));
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[\\u4e00-\\u9fa5A-Za-z0-9_]{2,20}$");
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^1[3-9]\\d{9}$");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$");
    }

    private String getText(TextInputEditText editText) {
        return editText == null ? "" : editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}