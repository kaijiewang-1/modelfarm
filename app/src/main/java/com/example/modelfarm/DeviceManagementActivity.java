package com.example.modelfarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.CreateDeviceRequest;
import com.example.modelfarm.network.models.Device;
import com.example.modelfarm.network.models.DeviceTypeEnum;
import com.example.modelfarm.network.models.FarmSite;
import com.example.modelfarm.network.models.PageResponse;
import com.example.modelfarm.network.services.DeviceApiService;
import com.example.modelfarm.network.services.FarmApiService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import company.company_info;
import personal.profile;
import farm.farm_list;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceManagementActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvTotalDevices;
    private TextView tvOnlineDevices;
    private TextView tvOfflineDevices;
    private MaterialButton btnAddDevice;
    private RecyclerView rvDevices;
    private DeviceAdapter deviceAdapter;
    private List<Device> deviceList;
    
    // API相关
    private DeviceApiService deviceApi;
    private FarmApiService farmApiService;
    
    // 站点列表（用于创建设备时选择）
    private List<FarmSite> farmSiteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_management);

        initViews();
        initApiComponents();
        setupToolbar();
        setupRecyclerView();
        setupClickListeners();
        initBottomNavigation();
        loadDeviceData();
    }

    /**
     * 初始化API组件
     */
    private void initApiComponents() {
        deviceApi = RetrofitClient.create(this, DeviceApiService.class);
        farmApiService = RetrofitClient.create(this, FarmApiService.class);
    }
    
    /**
     * 设置点击监听器
     */
    private void setupClickListeners() {
        if (btnAddDevice != null) {
            btnAddDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCreateDeviceDialog();
                }
            });
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTotalDevices = findViewById(R.id.tv_total_devices);
        tvOnlineDevices = findViewById(R.id.tv_online_devices);
        tvOfflineDevices = findViewById(R.id.tv_offline_devices);
        btnAddDevice = findViewById(R.id.btn_add_device);
        rvDevices = findViewById(R.id.rv_devices);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (bottomNavigationView == null) {
            return;
        }

        bottomNavigationView.setSelectedItemId(R.id.menu_devices);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_devices) {
                return true;
            } else if (itemId == R.id.menu_dashboard) {
                startActivity(new Intent(DeviceManagementActivity.this, DashboardActivity.class));
            } else if (itemId == R.id.menu_farms) {
                startActivity(new Intent(DeviceManagementActivity.this, farm_list.class));
            } else if (itemId == R.id.menu_orders) {
                startActivity(new Intent(DeviceManagementActivity.this, OrderListActivity.class));
            } else if (itemId == R.id.menu_company) {
                startActivity(new Intent(DeviceManagementActivity.this, company_info.class));
            } else {
                return false;
            }
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        });
    }

    private void setupRecyclerView() {
        deviceList = new ArrayList<>();
        deviceAdapter = new DeviceAdapter(deviceList, new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onDeviceClick(Device device) {
                if (device.getType() == DeviceTypeEnum.CAMERA) {
                    openLiveStream(device);
                } else {
                    openDeviceDetail(device);
                }
            }

            @Override
            public void onDeviceDelete(Device device) {
                showDeleteDeviceDialog(device);
            }
        });
        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        rvDevices.setAdapter(deviceAdapter);
    }

    private void openLiveStream(Device device) {
        String pushName = device.getPushName();
        if (TextUtils.isEmpty(pushName)) {
            Toast.makeText(this, "该摄像头缺少推流名称，无法打开直播", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, LiveStreamActivity.class);
        intent.putExtra("device_id", device.getId());
        intent.putExtra("device_name", device.getName());
        intent.putExtra("push_name", pushName);
        if (!TextUtils.isEmpty(device.getUrl())) {
            intent.putExtra("stream_url", device.getUrl());
        }
        startActivity(intent);
    }

    private void openDeviceDetail(Device device) {
        Intent intent = new Intent(this, DeviceDetailActivity.class);
        intent.putExtra("device_id", device.getId());
        intent.putExtra("device_name", device.getName());
        intent.putExtra("device_type", device.getTypeText());
        intent.putExtra("device_status", device.getStatusText());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 返回时刷新一次
        loadDeviceData();
    }

    private void loadDeviceData() {
        deviceApi.getDeviceList(1, 100, null, null, null, null).enqueue(new retrofit2.Callback<com.example.modelfarm.network.models.ApiResponse<com.example.modelfarm.network.models.PageResponse<Device>>>() {
            @Override
            public void onResponse(retrofit2.Call<com.example.modelfarm.network.models.ApiResponse<com.example.modelfarm.network.models.PageResponse<Device>>> call, retrofit2.Response<com.example.modelfarm.network.models.ApiResponse<com.example.modelfarm.network.models.PageResponse<Device>>> response) {
                runOnUiThread(() -> {
                    deviceList.clear();
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        List<Device> apiList = response.body().getData().getRecords();
                        deviceList.addAll(apiList);
                    } else {
                        Toast.makeText(DeviceManagementActivity.this, "获取设备数据失败: " + (response.body()!=null?response.body().getMessage():"接口异常"), Toast.LENGTH_LONG).show();
                    }
                    deviceAdapter.notifyDataSetChanged();
                    updateDeviceStatistics();
                });
            }
            @Override
            public void onFailure(retrofit2.Call<com.example.modelfarm.network.models.ApiResponse<com.example.modelfarm.network.models.PageResponse<Device>>> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(DeviceManagementActivity.this, "获取设备数据失败: 网络异常", Toast.LENGTH_LONG).show();
                    deviceList.clear();
                    deviceAdapter.notifyDataSetChanged();
                    updateDeviceStatistics();
                });
            }
        });
    }
    
    /**
     * 更新设备统计信息
     */
    private void updateDeviceStatistics() {
        int total = deviceList.size();
        int online = 0;
        int offline = 0;

        for (Device device : deviceList) {
            if ("在线".equals(device.getStatusText())) {
                online++;
            } else {
                offline++;
            }
        }

        tvTotalDevices.setText(String.valueOf(total));
        tvOnlineDevices.setText(String.valueOf(online));
        tvOfflineDevices.setText(String.valueOf(offline));
    }

    /**
     * 显示创建设备对话框
     */
    private void showCreateDeviceDialog() {
        // 先加载所有农场的站点列表
        loadAllFarmSites(new Runnable() {
            @Override
            public void run() {
                android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_device, null);
                android.widget.EditText etDeviceName = dialogView.findViewById(R.id.et_device_name);
                android.widget.Spinner spinnerSite = dialogView.findViewById(R.id.spinner_site);
                android.widget.EditText etMac = dialogView.findViewById(R.id.et_mac);
                android.widget.Spinner spinnerType = dialogView.findViewById(R.id.spinner_type);
                android.widget.EditText etPushName = dialogView.findViewById(R.id.et_push_name);
                android.widget.LinearLayout llPushName = dialogView.findViewById(R.id.ll_push_name);

                // 设置站点下拉框
                java.util.List<String> siteNames = new ArrayList<>();
                for (FarmSite site : farmSiteList) {
                    siteNames.add(site.getName() + " (ID: " + site.getId() + ")");
                }
                android.widget.ArrayAdapter<String> siteAdapter = new android.widget.ArrayAdapter<>(
                    DeviceManagementActivity.this,
                    android.R.layout.simple_spinner_item,
                    siteNames
                );
                siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSite.setAdapter(siteAdapter);

                // 设置设备类型下拉框
                String[] deviceTypes = {"摄像头", "传感器", "控制器"};
                android.widget.ArrayAdapter<String> typeAdapter = new android.widget.ArrayAdapter<>(
                    DeviceManagementActivity.this,
                    android.R.layout.simple_spinner_item,
                    deviceTypes
                );
                typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(typeAdapter);

                // 监听设备类型变化，显示/隐藏推流名称输入框
                spinnerType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                        llPushName.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onNothingSelected(android.widget.AdapterView<?> parent) {
                    }
                });

                new androidx.appcompat.app.AlertDialog.Builder(DeviceManagementActivity.this)
                    .setTitle("添加设备")
                    .setView(dialogView)
                    .setPositiveButton("创建", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            String name = etDeviceName.getText().toString().trim();
                            String mac = etMac.getText().toString().trim();
                            int type = spinnerType.getSelectedItemPosition() + 1;
                            String pushName = etPushName.getText().toString().trim();

                            if (name.isEmpty()) {
                                Toast.makeText(DeviceManagementActivity.this, "请输入设备名称", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (farmSiteList.isEmpty() || spinnerSite.getSelectedItemPosition() < 0) {
                                Toast.makeText(DeviceManagementActivity.this, "请选择站点", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (mac.isEmpty()) {
                                Toast.makeText(DeviceManagementActivity.this, "请输入MAC地址", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (type == 1 && pushName.isEmpty()) {
                                Toast.makeText(DeviceManagementActivity.this, "摄像头设备必须填写推流名称", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            FarmSite selectedSite = farmSiteList.get(spinnerSite.getSelectedItemPosition());
                            CreateDeviceRequest request = new CreateDeviceRequest(
                                name,
                                selectedSite.getId(),
                                mac,
                                type,
                                type == 1 ? pushName : null
                            );

                            createDevice(request);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
            }
        });
    }

    /**
     * 加载所有农场的站点列表
     */
    private void loadAllFarmSites(Runnable onComplete) {
        // 先获取所有农场
        farmApiService.getEnterpriseFarms().enqueue(new Callback<ApiResponse<java.util.List<com.example.modelfarm.network.models.Farm>>>() {
            @Override
            public void onResponse(Call<ApiResponse<java.util.List<com.example.modelfarm.network.models.Farm>>> call, Response<ApiResponse<java.util.List<com.example.modelfarm.network.models.Farm>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    java.util.List<com.example.modelfarm.network.models.Farm> farms = response.body().getData();
                    farmSiteList.clear();
                    
                    // 为每个农场加载站点
                    final int[] completed = {0};
                    final int total = farms.size();
                    
                    if (total == 0) {
                        runOnUiThread(onComplete);
                        return;
                    }
                    
                    for (com.example.modelfarm.network.models.Farm farm : farms) {
                        farmApiService.getFarmSites(farm.getId()).enqueue(new Callback<ApiResponse<java.util.List<FarmSite>>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<java.util.List<FarmSite>>> call, Response<ApiResponse<java.util.List<FarmSite>>> response) {
                                synchronized (farmSiteList) {
                                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                                        java.util.List<FarmSite> sites = response.body().getData();
                                        if (sites != null) {
                                            farmSiteList.addAll(sites);
                                        }
                                    }
                                    completed[0]++;
                                    if (completed[0] == total) {
                                        runOnUiThread(onComplete);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<java.util.List<FarmSite>>> call, Throwable t) {
                                synchronized (farmSiteList) {
                                    completed[0]++;
                                    if (completed[0] == total) {
                                        runOnUiThread(onComplete);
                                    }
                                }
                            }
                        });
                    }
                } else {
                    runOnUiThread(onComplete);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<java.util.List<com.example.modelfarm.network.models.Farm>>> call, Throwable t) {
                runOnUiThread(onComplete);
            }
        });
    }

    /**
     * 创建设备
     */
    private void createDevice(CreateDeviceRequest request) {
        deviceApi.createDevice(request).enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        Toast.makeText(DeviceManagementActivity.this, "设备创建成功", Toast.LENGTH_SHORT).show();
                        loadDeviceData();
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "创建设备失败";
                        Toast.makeText(DeviceManagementActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(DeviceManagementActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * 显示删除设备确认对话框
     */
    private void showDeleteDeviceDialog(Device device) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("删除设备")
            .setMessage("确定要删除设备 \"" + device.getName() + "\" 吗？此操作不可恢复。")
            .setPositiveButton("删除", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    deleteDevice(device.getId());
                }
            })
            .setNegativeButton("取消", null)
            .show();
    }

    /**
     * 删除设备
     */
    private void deleteDevice(int deviceId) {
        deviceApi.deleteDevice(deviceId).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        Toast.makeText(DeviceManagementActivity.this, "设备删除成功", Toast.LENGTH_SHORT).show();
                        loadDeviceData();
                    } else {
                        String errorMsg = response.body() != null ? response.body().getMessage() : "删除设备失败";
                        Toast.makeText(DeviceManagementActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(DeviceManagementActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
