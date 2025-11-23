package farm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.CreateFarmSiteRequest;
import com.example.modelfarm.network.models.FarmSite;
import com.example.modelfarm.network.services.FarmApiService;
import com.example.modelfarm.network.services.FarmSiteApiService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class farm_detail extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvFarmName;
    private TextView tvFarmLocation;
    private TextView tvFarmArea;
    private TextView tvPointCount;
    private TextView tvDeviceCount;
    private MaterialButton btnAddPoint;
    private RecyclerView rvPoints;
    private FarmPointAdapter pointAdapter;
    private List<FarmSite> farmSiteList;
    private int farmId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farm_detail);
        initViews();
        setupToolbar();
        setupRecyclerView();
        loadFarmData();
        loadPointData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvFarmName = findViewById(R.id.tv_farm_name);
        tvFarmLocation = findViewById(R.id.tv_farm_location);
        tvFarmArea = findViewById(R.id.tv_farm_area);
        tvPointCount = findViewById(R.id.tv_point_count);
        tvDeviceCount = findViewById(R.id.tv_device_count);
        btnAddPoint = findViewById(R.id.btn_add_point);
        rvPoints = findViewById(R.id.rv_points);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回农场列表
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        farmSiteList = new ArrayList<>();
        pointAdapter = new FarmPointAdapter(farmSiteList, new FarmPointAdapter.OnPointClickListener() {
            @Override
            public void onPointClick(FarmSite farmSite) {
                // 点击农场点，跳转到农场点详情
                Intent intent = new Intent(farm_detail.this, farm_point_detail.class);
                intent.putExtra("farm_site_id", farmSite.getId());
                intent.putExtra("point_name", farmSite.getName());
                startActivity(intent);
            }

            @Override
            public void onPointDelete(FarmSite farmSite) {
                // 删除养殖点
                deleteFarmSite(farmSite);
            }
        });
        rvPoints.setLayoutManager(new LinearLayoutManager(this));
        rvPoints.setAdapter(pointAdapter);
        
        // 设置添加养殖点按钮点击事件
        btnAddPoint.setOnClickListener(v -> showAddFarmSiteDialog());
    }

    private void loadFarmData() {
        Intent intent = getIntent();
        String farmName = intent.getStringExtra("farm_name");
        String farmLocation = intent.getStringExtra("farm_location");
        farmId = intent.getIntExtra("farm_id", -1);
        if (farmName != null) tvFarmName.setText(farmName);
        if (farmLocation != null) tvFarmLocation.setText(farmLocation);
        tvFarmArea.setText("-");
    }

    private void loadPointData() {
        if (farmId <= 0) {
            farmSiteList.clear();
            pointAdapter.notifyDataSetChanged();
            tvPointCount.setText("0");
            tvDeviceCount.setText("0");
            return;
        }
        FarmApiService api = RetrofitClient.create(this, FarmApiService.class);
        api.getFarmSites(farmId).enqueue(new Callback<ApiResponse<java.util.List<FarmSite>>>() {
            @Override
            public void onResponse(Call<ApiResponse<java.util.List<FarmSite>>> call, Response<ApiResponse<java.util.List<FarmSite>>> response) {
                farmSiteList.clear();
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200 && response.body().getData()!=null) {
                    farmSiteList.addAll(response.body().getData());
                }
                pointAdapter.notifyDataSetChanged();
                tvPointCount.setText(String.valueOf(farmSiteList.size()));
                tvDeviceCount.setText("-");
            }
            @Override
            public void onFailure(Call<ApiResponse<java.util.List<FarmSite>>> call, Throwable t) {
                farmSiteList.clear();
                pointAdapter.notifyDataSetChanged();
                tvPointCount.setText("0");
                tvDeviceCount.setText("0");
            }
        });
    }

    private void showAddFarmSiteDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_farm_site, null);
        TextInputEditText etFarmSiteName = dialogView.findViewById(R.id.et_farm_site_name);
        TextInputEditText etCapacity = dialogView.findViewById(R.id.et_capacity);
        TextInputEditText etType = dialogView.findViewById(R.id.et_type);

        new MaterialAlertDialogBuilder(this)
                .setTitle("添加养殖点")
                .setView(dialogView)
                .setPositiveButton("确定", (dialog, which) -> {
                    String name = etFarmSiteName.getText() != null ? etFarmSiteName.getText().toString().trim() : "";
                    String capacityStr = etCapacity.getText() != null ? etCapacity.getText().toString().trim() : "";
                    String type = etType.getText() != null ? etType.getText().toString().trim() : "";

                    if (name.isEmpty()) {
                        Toast.makeText(this, "请输入养殖点名称", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Convert capacity to integer (0 if empty)
                    int capacity = 0;
                    if (!capacityStr.isEmpty()) {
                        try {
                            capacity = Integer.parseInt(capacityStr);
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "容量必须是数字", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    createFarmSite(name, capacity);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void createFarmSite(String name, int sum) {
        FarmSiteApiService api = RetrofitClient.create(this, FarmSiteApiService.class);
        
        // Create request object using the proper model
        CreateFarmSiteRequest request = new CreateFarmSiteRequest(farmId, name, sum);

        api.createFarmSite(request).enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Toast.makeText(farm_detail.this, "养殖点创建成功", Toast.LENGTH_SHORT).show();
                    loadPointData(); // Refresh the list
                } else {
                    String errorMsg = response.body() != null ? response.body().getMessage() : "创建养殖点失败";
                    Toast.makeText(farm_detail.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                Toast.makeText(farm_detail.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteFarmSite(FarmSite farmSite) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除养殖点 \"" + farmSite.getName() + "\" 吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    FarmSiteApiService api = RetrofitClient.create(this, FarmSiteApiService.class);
                    api.deleteFarmSite(farmSite.getId()).enqueue(new Callback<ApiResponse<Void>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                                Toast.makeText(farm_detail.this, "养殖点删除成功", Toast.LENGTH_SHORT).show();
                                loadPointData(); // Refresh the list
                            } else {
                                String errorMsg = response.body() != null ? response.body().getMessage() : "删除养殖点失败";
                                Toast.makeText(farm_detail.this, errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                            Toast.makeText(farm_detail.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
