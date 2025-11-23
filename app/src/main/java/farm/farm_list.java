package farm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.DashboardActivity;
import com.example.modelfarm.R;
import com.example.modelfarm.DeviceManagementActivity;
import com.example.modelfarm.OrderListActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import company.company_info;
import personal.profile;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.services.EnterpriseApiService;
import com.example.modelfarm.network.services.FarmApiService;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.CreateFarmRequest;
import com.example.modelfarm.network.models.EnterpriseFarm;
import com.example.modelfarm.network.models.EnterpriseUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class farm_list extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MaterialButton btnAddFarm;
    private MaterialButton btnShowJson;
    private RecyclerView rvFarms;
    private FarmAdapter farmAdapter;
    private List<Farm> farmList;
    private final Map<Integer, String> userIdToName = new HashMap<>();
    
    // API相关
    private EnterpriseApiService enterpriseApi;
    private FarmApiService farmApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farm_list);

        initViews();
        initApiComponents();
        setupToolbar();
        setupRecyclerView();
        setupClickListeners();
        initBottomNavigation();
        loadEnterpriseUsers();
    }
    
    /**
     * 设置点击监听器
     */
    private void setupClickListeners() {
        btnAddFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateFarmDialog();
            }
        });
    }

    /**
     * 初始化API组件
     */
    private void initApiComponents() {
        enterpriseApi = RetrofitClient.create(this, EnterpriseApiService.class);
        farmApiService = RetrofitClient.create(this, FarmApiService.class);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        btnAddFarm = findViewById(R.id.btn_add_farm);
        btnShowJson = findViewById(R.id.btn_show_farms_json);
        rvFarms = findViewById(R.id.rv_farms);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回主控制台
                Intent intent = new Intent(farm_list.this, DashboardActivity.class);
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

        bottomNavigationView.setSelectedItemId(R.id.menu_farms);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_farms) {
                return true;
            } else if (itemId == R.id.menu_dashboard) {
                startActivity(new Intent(farm_list.this, DashboardActivity.class));
            } else if (itemId == R.id.menu_devices) {
                startActivity(new Intent(farm_list.this, DeviceManagementActivity.class));
            } else if (itemId == R.id.menu_orders) {
                startActivity(new Intent(farm_list.this, OrderListActivity.class));
            } else if (itemId == R.id.menu_company) {
                startActivity(new Intent(farm_list.this, company_info.class));
            }  else {
                return false;
            }
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        });
    }

    private void setupRecyclerView() {
        farmList = new ArrayList<>();
        farmAdapter = new FarmAdapter(farmList, new FarmAdapter.OnFarmClickListener() {
            @Override
            public void onFarmClick(Farm farm) {
                Intent intent = new Intent(farm_list.this, farm_detail.class);
                intent.putExtra("farm_id", farm.getId());
                intent.putExtra("farm_name", farm.getName());
                intent.putExtra("farm_location", farm.getLocation());
                startActivity(intent);
            }

            @Override
            public void onFarmDelete(Farm farm) {
                showDeleteFarmDialog(farm);
            }
        });
        rvFarms.setLayoutManager(new LinearLayoutManager(this));
        rvFarms.setAdapter(farmAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (btnShowJson != null) {
            btnShowJson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFarmsJsonDialog();
                }
            });
        }
    }

    /**
     * 先加载企业用户，建立 supervisorId -> username 映射
     */
    private void loadEnterpriseUsers() {
        enterpriseApi.getEnterpriseUsers().enqueue(new Callback<ApiResponse<List<EnterpriseUser>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<EnterpriseUser>>> call, Response<ApiResponse<List<EnterpriseUser>>> response) {
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200 && response.body().getData()!=null) {
                    userIdToName.clear();
                    for (EnterpriseUser u: response.body().getData()) {
                        userIdToName.put(u.getId(), u.getUsername());
                    }
                }
                loadFarmData();
            }
            @Override
            public void onFailure(Call<ApiResponse<List<EnterpriseUser>>> call, Throwable t) {
                userIdToName.clear();
                loadFarmData();
            }
        });
    }

    private void loadFarmData() {
        enterpriseApi.getEnterpriseFarms().enqueue(new Callback<ApiResponse<List<EnterpriseFarm>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<EnterpriseFarm>>> call, Response<ApiResponse<List<EnterpriseFarm>>> response) {
                runOnUiThread(() -> {
                    farmList.clear();
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        List<EnterpriseFarm> apiList = response.body().getData();
                        for (EnterpriseFarm apiFarm : apiList) {
                            farmList.add(convertApiFarmToLocal(apiFarm));
                        }
                    } else {
                        Toast.makeText(farm_list.this, "获取农场数据失败: " + (response.body()!=null?response.body().getMessage():"接口异常"), Toast.LENGTH_LONG).show();
                    }
                    farmAdapter.notifyDataSetChanged();
                });
            }
            @Override
            public void onFailure(Call<ApiResponse<List<EnterpriseFarm>>> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(farm_list.this, "获取农场数据失败: 网络异常", Toast.LENGTH_LONG).show();
                    farmList.clear();
                    farmAdapter.notifyDataSetChanged();
                });
            }
        });
    }
    
    /**
     * 将API农场数据适配为本地Farm（适配你的原UI模型）
     */
    private Farm convertApiFarmToLocal(com.example.modelfarm.network.models.EnterpriseFarm apiFarm) {

        String type = getFarmTypeDisplay(1);
        String status = getFarmStatusDisplay(1);
        String supervisor = userIdToName.containsKey(apiFarm.getSupervisorId()) ? userIdToName.get(apiFarm.getSupervisorId()) : "-";
        return new Farm(
            apiFarm.getId(),
            apiFarm.getName(),
            apiFarm.getAddress(),
            "负责人: " + supervisor,
            type,
            status
        );
    }
    
    private String getFarmTypeDisplay(int type) {
        switch (type) {
            case 1: return "养殖场";
            case 2: return "种植园";
            case 3: return "混合农场";
            default: return "未知";
        }
    }
    
    private String getFarmStatusDisplay(int status) {
        switch (status) {
            case 1: return "正常运营";
            case 0: return "暂停运营";
            case -1: return "已关闭";
            default: return "未知状态";
        }
    }

    // 农场数据模型
    public static class Farm {
        private final int id;
        private final String name;
        private final String location;
        private final String description;
        private final String type;
        private final String status;


        public Farm(int id, String name, String location, String description,
                   String type, String status) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.description = description;
            this.type = type;
            this.status = status;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getLocation() { return location; }
        public String getDescription() { return description; }
        public String getType() { return type; }
        public String getStatus() { return status; }
    }

    private void showFarmsJsonDialog() {
        try {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(farmList);
            android.widget.ScrollView scroll = new android.widget.ScrollView(this);
            android.widget.TextView tv = new android.widget.TextView(this);
            tv.setTextIsSelectable(true);
            tv.setText(json);
            tv.setTextSize(12);
            int pad = (int) (getResources().getDisplayMetrics().density * 16);
            tv.setPadding(pad, pad, pad, pad);
            scroll.addView(tv);
            new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("农场原始JSON")
                .setView(scroll)
                .setNegativeButton("关闭", null)
                .setPositiveButton("复制", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        android.content.ClipboardManager cm = (android.content.ClipboardManager) getSystemService(android.content.Context.CLIPBOARD_SERVICE);
                        if (cm != null) {
                            cm.setPrimaryClip(android.content.ClipData.newPlainText("farms_json", json));
                            android.widget.Toast.makeText(farm_list.this, "已复制", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
        } catch (Exception e) {
            android.widget.Toast.makeText(this, "展示失败", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示创建农场对话框
     */
    private void showCreateFarmDialog() {
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_farm, null);
        android.widget.EditText etFarmName = dialogView.findViewById(R.id.et_farm_name);
        android.widget.EditText etFarmAddress = dialogView.findViewById(R.id.et_farm_address);
        android.widget.Spinner spinnerSupervisor = dialogView.findViewById(R.id.spinner_supervisor);

        // 设置负责人下拉框
        java.util.List<String> supervisorNames = new ArrayList<>();
        for (java.util.Map.Entry<Integer, String> entry : userIdToName.entrySet()) {
            supervisorNames.add(entry.getValue() + " (ID: " + entry.getKey() + ")");
        }
        android.widget.ArrayAdapter<String> supervisorAdapter = new android.widget.ArrayAdapter<>(
            farm_list.this,
            android.R.layout.simple_spinner_item,
            supervisorNames
        );
        supervisorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSupervisor.setAdapter(supervisorAdapter);

        new androidx.appcompat.app.AlertDialog.Builder(farm_list.this)
            .setTitle("添加农场")
            .setView(dialogView)
            .setPositiveButton("创建", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    String name = etFarmName.getText().toString().trim();
                    String address = etFarmAddress.getText().toString().trim();
                    
                    if (name.isEmpty()) {
                        Toast.makeText(farm_list.this, "请输入农场名称", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (userIdToName.isEmpty() || spinnerSupervisor.getSelectedItemPosition() < 0) {
                        Toast.makeText(farm_list.this, "请选择负责人", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // 获取选中的负责人ID
                    Integer[] supervisorIds = userIdToName.keySet().toArray(new Integer[0]);
                    int selectedSupervisorId = supervisorIds[spinnerSupervisor.getSelectedItemPosition()];
                    
                    CreateFarmRequest request = new CreateFarmRequest(
                        name,
                        address.isEmpty() ? null : address,
                        String.valueOf(selectedSupervisorId)
                    );

                    createFarm(request);
                }
            })
            .setNegativeButton("取消", null)
            .show();
    }

    /**
     * 创建农场
     */
    private void createFarm(CreateFarmRequest request) {
        farmApiService.createFarm(request).enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        Toast.makeText(farm_list.this, "农场创建成功", Toast.LENGTH_SHORT).show();
                        loadFarmData();
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "创建农场失败";
                        Toast.makeText(farm_list.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(farm_list.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * 显示删除农场确认对话框
     */
    private void showDeleteFarmDialog(Farm farm) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("删除农场")
            .setMessage("确定要删除农场 \"" + farm.getName() + "\" 吗？此操作不可恢复。")
            .setPositiveButton("删除", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    deleteFarm(farm.getId());
                }
            })
            .setNegativeButton("取消", null)
            .show();
    }

    /**
     * 删除农场
     */
    private void deleteFarm(int farmId) {
        farmApiService.deleteFarm(farmId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        Toast.makeText(farm_list.this, "农场删除成功", Toast.LENGTH_SHORT).show();
                        loadFarmData();
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "删除农场失败";
                        Toast.makeText(farm_list.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(farm_list.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}