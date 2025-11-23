package logins;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.DashboardActivity;
import com.example.modelfarm.R;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.JoinEnterpriseRequest;
import com.example.modelfarm.network.services.UserApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class join_company extends AppCompatActivity {

    private EditText etInviteCode;
    private Button btnJoin;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_join_company);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etInviteCode = findViewById(R.id.etInviteCode);
        btnJoin = findViewById(R.id.btnJoin);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inviteCode = etInviteCode.getText().toString().trim();

                if (inviteCode.isEmpty()) {
                    Toast.makeText(join_company.this, "请输入邀请码", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 禁用按钮防止重复点击
                btnJoin.setEnabled(false);
                btnJoin.setText("加入中...");

                // 调用API加入企业
                joinEnterprise(inviteCode);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回企业选择页面
                finish();
            }
        });
    }

    private void joinEnterprise(String inviteCode) {
        UserApiService api = RetrofitClient.create(this, UserApiService.class);
        JoinEnterpriseRequest request = new JoinEnterpriseRequest(inviteCode);
        
        api.joinEnterprise(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                runOnUiThread(() -> {
                    // 恢复按钮状态
                    btnJoin.setEnabled(true);
                    btnJoin.setText("加入企业");
                    
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        // 成功加入企业
                        Toast.makeText(join_company.this, "成功加入企业，请重新登录", Toast.LENGTH_LONG).show();
                        
                        // 根据API说明，加入成功后需要重新登录
                        // 跳转到登录页面
                        Intent intent = new Intent(join_company.this, login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // 处理错误响应
                        String errorMessage = "加入企业失败";
                        if (response.body() != null && response.body().getMessage() != null) {
                            errorMessage = response.body().getMessage();
                        } else if (response.errorBody() != null) {
                            try {
                                errorMessage = response.errorBody().string();
                            } catch (Exception e) {
                                errorMessage = "请求失败，错误码: " + response.code();
                            }
                        }
                        Toast.makeText(join_company.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                runOnUiThread(() -> {
                    // 恢复按钮状态
                    btnJoin.setEnabled(true);
                    btnJoin.setText("加入企业");
                    
                    Toast.makeText(join_company.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}