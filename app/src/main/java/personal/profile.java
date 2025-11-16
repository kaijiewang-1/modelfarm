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
import logins.login;

import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.UpdateUserRequest;
import com.example.modelfarm.network.models.UpdatePasswordRequest;
import com.example.modelfarm.network.models.User;
import com.example.modelfarm.network.services.UserApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

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

    private UserApiService userApiService;
    private User currentUser;

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
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnLogout = findViewById(R.id.btn_logout);
        MaterialButton btnLeaveEnterprise = findViewById(R.id.btn_leave_enterprise);
//        tvRawJson = findViewById(R.id.tv_raw_json);
//        btnCopyJson = findViewById(R.id.btn_copy_json);
        if (tvApiPath != null) {
            tvApiPath.setText("接口: GET /user");
        }
        
        userApiService = RetrofitClient.create(this, UserApiService.class);
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
                        currentUser = user; // 保存当前用户信息
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
        // 保存修改按钮
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateUserInfo();
                }
            });
        }

        // 修改密码按钮
        if (btnChangePassword != null) {
            btnChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChangePasswordDialog();
                }
            });
        }

        // 离开企业按钮
        MaterialButton btnLeaveEnterprise = findViewById(R.id.btn_leave_enterprise);
        if (btnLeaveEnterprise != null) {
            btnLeaveEnterprise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLeaveEnterpriseDialog();
                }
            });
        }

        // 退出登录按钮
        if (btnLogout != null) {
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLogoutDialog();
                }
            });
        }

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

    /**
     * 更新用户信息
     */
    private void updateUserInfo() {
        String username = etNickname != null ? etNickname.getText().toString().trim() : "";
        String phone = etPhone != null ? etPhone.getText().toString().trim() : "";

        if (TextUtils.isEmpty(username)) {
            android.widget.Toast.makeText(this, "请输入姓名", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        UpdateUserRequest request = new UpdateUserRequest(username, phone);
        userApiService.updateUser(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        android.widget.Toast.makeText(profile.this, "用户信息更新成功", android.widget.Toast.LENGTH_SHORT).show();
                        loadUserData(); // 重新加载用户数据
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "更新用户信息失败";
                        android.widget.Toast.makeText(profile.this, errorMsg, android.widget.Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    android.widget.Toast.makeText(profile.this, "网络错误: " + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * 显示修改密码对话框
     */
    private void showChangePasswordDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);
        EditText etOldPassword = dialogView.findViewById(R.id.et_old_password);
        EditText etNewPassword = dialogView.findViewById(R.id.et_new_password);
        EditText etConfirmPassword = dialogView.findViewById(R.id.et_confirm_password);

        new AlertDialog.Builder(this)
            .setTitle("修改密码")
            .setView(dialogView)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String oldPassword = etOldPassword.getText().toString().trim();
                    String newPassword = etNewPassword.getText().toString().trim();
                    String confirmPassword = etConfirmPassword.getText().toString().trim();

                    if (TextUtils.isEmpty(oldPassword)) {
                        android.widget.Toast.makeText(profile.this, "请输入旧密码", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(newPassword)) {
                        android.widget.Toast.makeText(profile.this, "请输入新密码", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newPassword.length() < 6) {
                        android.widget.Toast.makeText(profile.this, "新密码长度至少6位", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!newPassword.equals(confirmPassword)) {
                        android.widget.Toast.makeText(profile.this, "两次输入的新密码不一致", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }

                    updatePassword(oldPassword, newPassword);
                }
            })
            .setNegativeButton("取消", null)
            .show();
    }

    /**
     * 更新密码
     */
    private void updatePassword(String oldPassword, String newPassword) {
        UpdatePasswordRequest request = new UpdatePasswordRequest(oldPassword, newPassword);
        userApiService.updatePassword(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        android.widget.Toast.makeText(profile.this, "密码修改成功", android.widget.Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "密码修改失败";
                        android.widget.Toast.makeText(profile.this, errorMsg, android.widget.Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    android.widget.Toast.makeText(profile.this, "网络错误: " + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * 显示离开企业确认对话框
     */
    private void showLeaveEnterpriseDialog() {
        new AlertDialog.Builder(this)
            .setTitle("离开企业")
            .setMessage("确定要离开当前企业吗？离开后需要重新加入企业才能使用相关功能。")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    leaveEnterprise();
                }
            })
            .setNegativeButton("取消", null)
            .show();
    }

    /**
     * 离开企业
     */
    private void leaveEnterprise() {
        userApiService.leaveEnterprise().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        android.widget.Toast.makeText(profile.this, "已离开企业，请重新登录", android.widget.Toast.LENGTH_LONG).show();
                        // 离开企业后会自动注销登录，跳转到登录页面
                        logoutAndGoToLogin();
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "离开企业失败";
                        android.widget.Toast.makeText(profile.this, errorMsg, android.widget.Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    android.widget.Toast.makeText(profile.this, "网络错误: " + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * 显示退出登录确认对话框
     */
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("退出登录")
            .setMessage("确定要退出登录吗？")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    logout();
                }
            })
            .setNegativeButton("取消", null)
            .show();
    }

    /**
     * 退出登录
     */
    private void logout() {
        userApiService.logout().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        android.widget.Toast.makeText(profile.this, "已退出登录", android.widget.Toast.LENGTH_SHORT).show();
                        logoutAndGoToLogin();
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "退出登录失败";
                        android.widget.Toast.makeText(profile.this, errorMsg, android.widget.Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    android.widget.Toast.makeText(profile.this, "网络错误: " + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * 退出登录并跳转到登录页面
     */
    private void logoutAndGoToLogin() {
        // 清除本地存储的token等信息
        android.content.SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        prefs.edit().clear().apply();

        // 跳转到登录页面
        Intent intent = new Intent(profile.this, logins.login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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