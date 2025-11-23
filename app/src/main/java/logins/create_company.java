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
import com.example.modelfarm.network.models.CreateEnterpriseRequest;
import com.example.modelfarm.network.services.EnterpriseApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class create_company extends AppCompatActivity {

    private EditText etCompanyName;
    private EditText etCompanyAddress;
    private EditText etContactPerson;
    private EditText etContactPhone;
    private Button btnCreate;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_company);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etCompanyName = findViewById(R.id.etCompanyName);
        etCompanyAddress = findViewById(R.id.etCompanyAddress);
        etContactPerson = findViewById(R.id.etContactPerson);
        etContactPhone = findViewById(R.id.etContactPhone);
        btnCreate = findViewById(R.id.btnCreate);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName = etCompanyName.getText().toString().trim();
                String companyAddress = etCompanyAddress.getText().toString().trim();
                String contactPerson = etContactPerson.getText().toString().trim();
                String contactPhone = etContactPhone.getText().toString().trim();

                if (companyName.isEmpty() || companyAddress.isEmpty() || 
                    contactPerson.isEmpty() || contactPhone.isEmpty()) {
                    Toast.makeText(create_company.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 禁用按钮防止重复点击
                btnCreate.setEnabled(false);
                btnCreate.setText("创建中...");

                // 调用API创建企业
                createEnterprise(companyName, companyAddress, contactPerson, contactPhone);
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

    private void createEnterprise(String name, String address, String contactPerson, String contactPhone) {
        EnterpriseApiService api = RetrofitClient.create(this, EnterpriseApiService.class);
        CreateEnterpriseRequest request = new CreateEnterpriseRequest(name, address, contactPerson, contactPhone);
        
        api.createEnterprise(request).enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                runOnUiThread(() -> {
                    // 恢复按钮状态
                    btnCreate.setEnabled(true);
                    btnCreate.setText("创建企业");
                    
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        // 成功创建企业
                        Integer enterpriseId = response.body().getData();
                        String successMessage = "企业创建成功";
                        if (enterpriseId != null) {
                            successMessage += "，企业ID: " + enterpriseId;
                        }
                        Toast.makeText(create_company.this, successMessage, Toast.LENGTH_LONG).show();
                        
                        // 跳转到主控制台
                        Intent intent = new Intent(create_company.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // 处理错误响应
                        String errorMessage = "企业创建失败";
                        if (response.body() != null && response.body().getMessage() != null) {
                            errorMessage = response.body().getMessage();
                        } else if (response.errorBody() != null) {
                            try {
                                errorMessage = response.errorBody().string();
                            } catch (Exception e) {
                                errorMessage = "请求失败，错误码: " + response.code();
                            }
                        }
                        Toast.makeText(create_company.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                runOnUiThread(() -> {
                    // 恢复按钮状态
                    btnCreate.setEnabled(true);
                    btnCreate.setText("创建企业");
                    
                    Toast.makeText(create_company.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
