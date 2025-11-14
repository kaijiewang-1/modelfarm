package company;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import personal.profile;
import farm.farm_list;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.Enterprise;
import com.example.modelfarm.network.services.EnterpriseApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class company_info extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvCompanyName;
    private TextView tvAddress;
    private TextView tvContactPerson;
    private TextView tvContactPhone;
    private TextView tvInvitedCode;
    private TextView tvStatus;
    private TextView tvEnterpriseId;
    private TextView tvCreatedAt;
    private TextView tvUpdatedAt;
    private TextView tvDeletedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_info);

        initViews();
        setupToolbar();
        initBottomNavigation();
        loadCompanyData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvCompanyName = findViewById(R.id.tv_company_name);
        tvAddress = findViewById(R.id.tv_address);
        tvContactPerson = findViewById(R.id.tv_contact_person);
        tvContactPhone = findViewById(R.id.tv_contact_phone);
        tvInvitedCode = findViewById(R.id.tv_invited_code);
        tvStatus = findViewById(R.id.tv_status);
        tvEnterpriseId = findViewById(R.id.tv_enterprise_id);
        tvCreatedAt = findViewById(R.id.tv_created_at);
        tvUpdatedAt = findViewById(R.id.tv_updated_at);
        tvDeletedAt = findViewById(R.id.tv_deleted_at);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回主控制台
                Intent intent = new Intent(company_info.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (bottomNavigationView == null) {
            return;
        }

        bottomNavigationView.setSelectedItemId(R.id.menu_company);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_company) {
                return true;
            } else if (itemId == R.id.menu_dashboard) {
                startActivity(new Intent(company_info.this, DashboardActivity.class));
            } else if (itemId == R.id.menu_devices) {
                startActivity(new Intent(company_info.this, DeviceManagementActivity.class));
            } else if (itemId == R.id.menu_farms) {
                startActivity(new Intent(company_info.this, farm_list.class));
            } else if (itemId == R.id.menu_orders) {
                startActivity(new Intent(company_info.this, OrderListActivity.class));
            }  else {
                return false;
            }
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        });
    }

    private void loadCompanyData() {
        EnterpriseApiService api = RetrofitClient.create(this, EnterpriseApiService.class);
        api.getEnterprise().enqueue(new Callback<ApiResponse<Enterprise>>() {
            @Override
            public void onResponse(Call<ApiResponse<Enterprise>> call, Response<ApiResponse<Enterprise>> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200 && response.body().getData()!=null) {
                        Enterprise e = response.body().getData();
                        if (tvCompanyName != null) tvCompanyName.setText(e.getName());
                        if (tvAddress != null) tvAddress.setText(e.getAddress());
                        if (tvContactPerson != null) tvContactPerson.setText(e.getContactPerson());
                        if (tvContactPhone != null) tvContactPhone.setText(e.getContactPhone());
                        if (tvInvitedCode != null) tvInvitedCode.setText(e.getInvitedCode());
                        if (tvStatus != null) tvStatus.setText(mapStatus(e.getStatus()));
                        if (tvEnterpriseId != null) tvEnterpriseId.setText(String.valueOf(e.getId()));
                        if (tvCreatedAt != null) tvCreatedAt.setText(e.getCreatedAt());
                        if (tvUpdatedAt != null) tvUpdatedAt.setText(e.getUpdatedAt());
                        if (tvDeletedAt != null) tvDeletedAt.setText(e.getDeletedAt()!=null?e.getDeletedAt():"-");
                    } else {
                        String msg;
                        try {
                            msg = response.errorBody()!=null ? response.errorBody().string() : (response.body()!=null?response.body().getMessage():"请求失败");
                        } catch (Exception ex) { msg = "请求失败"; }
                        android.widget.Toast.makeText(company_info.this, "获取企业信息失败: code=" + response.code() + " " + msg, android.widget.Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Enterprise>> call, Throwable t) {
                android.widget.Toast.makeText(company_info.this, "网络错误:" + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            }
        });
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
        return status;
    }
}