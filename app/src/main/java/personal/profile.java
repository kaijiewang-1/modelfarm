package personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.DashboardActivity;
import com.example.modelfarm.R;
import com.example.modelfarm.DeviceManagementActivity;
import com.example.modelfarm.OrderListActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import company.company_info;
import farm.farm_list;

import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.User;
import com.example.modelfarm.network.services.UserApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profile extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvUserName;
    private TextInputEditText etNickname;
    private TextInputEditText etPhone;
    private TextView tvUserId;
    private TextView tvEnterpriseId;
    private TextView tvUserStatus;
    private TextView tvCreatedAt;
    private TextView tvUpdatedAt;
    private TextView tvDeletedAt;
    private TextView tvApiPath;
    private MaterialButton btnEditProfile;
    private MaterialButton btnChangePassword;
    private MaterialButton btnLogout;
    private TextView tvRawJson;
    private com.google.android.material.button.MaterialButton btnCopyJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        initViews();
        setupToolbar();
        loadUserData();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvUserName = findViewById(R.id.tv_user_name);
        etNickname = findViewById(R.id.et_nickname);
        etPhone = findViewById(R.id.et_phone);
        tvUserId = findViewById(R.id.tv_user_id);
        tvEnterpriseId = findViewById(R.id.tv_enterprise_id);
        tvUserStatus = findViewById(R.id.tv_user_status);
        tvCreatedAt = findViewById(R.id.tv_created_at);
        tvUpdatedAt = findViewById(R.id.tv_updated_at);
        tvDeletedAt = findViewById(R.id.tv_deleted_at);
        tvApiPath = findViewById(R.id.tv_api_path);
//        tvRawJson = findViewById(R.id.tv_raw_json);
//        btnCopyJson = findViewById(R.id.btn_copy_json);
        if (tvApiPath != null) {
            tvApiPath.setText("接口: GET /user");
        }
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回主控制台
                Intent intent = new Intent(profile.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void loadUserData() {
        UserApiService api = RetrofitClient.create(this, UserApiService.class);
        api.getCurrentUser().enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200 && response.body().getData() != null) {
                        User user = response.body().getData();
                        if (tvUserName != null) tvUserName.setText(user.getUsername());
                        if (etNickname != null) etNickname.setText(user.getUsername());
                        if (etPhone != null) etPhone.setText(user.getPhone());
                        if (tvUserId != null) tvUserId.setText(String.valueOf(user.getId()));
                        if (tvEnterpriseId != null) tvEnterpriseId.setText(String.valueOf(user.getEnterpriseId()));
                        if (tvUserStatus != null) tvUserStatus.setText(mapStatus(user.getStatus()));
                        if (tvCreatedAt != null) tvCreatedAt.setText(user.getCreatedAt());
                        if (tvUpdatedAt != null) tvUpdatedAt.setText(user.getUpdatedAt());
                        if (tvDeletedAt != null) tvDeletedAt.setText(user.getDeletedAt()!=null?user.getDeletedAt():"-");
                        try {
                            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
                            if (tvRawJson != null) tvRawJson.setText(gson.toJson(user));
                        } catch (Exception ignore) {}
                    } else {
                        String msg;
                        try {
                            msg = response.errorBody()!=null ? response.errorBody().string() : (response.body()!=null?response.body().getMessage():"请求失败");
                        } catch (Exception e) { msg = "请求失败"; }
                        android.widget.Toast.makeText(profile.this, "获取用户信息失败: code=" + response.code() + " " + msg, android.widget.Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                android.widget.Toast.makeText(profile.this, "网络错误:" + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 返回页面时刷新一次
        loadUserData();
    }

    private void setupClickListeners() {
        if (btnCopyJson != null) {
            btnCopyJson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String txt = tvRawJson != null ? tvRawJson.getText().toString() : "";
                        android.content.ClipboardManager cm = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        if (cm != null) {
                            cm.setPrimaryClip(android.content.ClipData.newPlainText("user_json", txt));
                            android.widget.Toast.makeText(profile.this, "已复制 JSON", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        android.widget.Toast.makeText(profile.this, "复制失败", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private String mapStatus(String status) {
        if (status == null) return "-";
        String s = status.trim().toLowerCase();
        if ("1".equals(s) || "active".equals(s) || "enabled".equals(s) || "正常".equals(s)) {
            return "正常";
        }
        if ("0".equals(s) || "inactive".equals(s) || "disabled".equals(s) || "禁用".equals(s)) {
            return "禁用";
        }
        return status; // 回显原值，便于观察
    }
}