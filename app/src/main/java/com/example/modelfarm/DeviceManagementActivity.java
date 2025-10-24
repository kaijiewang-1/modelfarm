package com.example.modelfarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.services.DeviceApiService;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.Device;
import com.example.modelfarm.network.models.PageResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class DeviceManagementActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvTotalDevices;
    private TextView tvOnlineDevices;
    private TextView tvOfflineDevices;
    private MaterialButton btnAddDevice;
    private MaterialButton btnShowJson;
    private RecyclerView rvDevices;
    private DeviceAdapter deviceAdapter;
    private List<Device> deviceList;
    
    // API相关
    private DeviceApiService deviceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_management);

        initViews();
        initApiComponents();
        setupToolbar();
        setupRecyclerView();
        loadDeviceData();
    }

    /**
     * 初始化API组件
     */
    private void initApiComponents() {
        deviceApi = RetrofitClient.create(this, DeviceApiService.class);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTotalDevices = findViewById(R.id.tv_total_devices);
        tvOnlineDevices = findViewById(R.id.tv_online_devices);
        tvOfflineDevices = findViewById(R.id.tv_offline_devices);
        btnAddDevice = findViewById(R.id.btn_add_device);
        btnShowJson = findViewById(R.id.btn_show_devices_json);
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

    private void setupRecyclerView() {
        deviceList = new ArrayList<>();
        deviceAdapter = new DeviceAdapter(deviceList, new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onDeviceClick(Device device) {
                Toast.makeText(DeviceManagementActivity.this, "点击了设备: " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        rvDevices.setAdapter(deviceAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 返回时刷新一次
        loadDeviceData();
        if (btnShowJson != null) {
            btnShowJson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDevicesJsonDialog();
                }
            });
        }
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
            if ("在线".equals(device.getStatus())) {
                online++;
            } else {
                offline++;
            }
        }

        tvTotalDevices.setText(String.valueOf(total));
        tvOnlineDevices.setText(String.valueOf(online));
        tvOfflineDevices.setText(String.valueOf(offline));
    }

    private void showDevicesJsonDialog() {
        try {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(deviceList);
            android.widget.ScrollView scroll = new android.widget.ScrollView(this);
            android.widget.TextView tv = new android.widget.TextView(this);
            tv.setTextIsSelectable(true);
            tv.setText(json);
            tv.setTextSize(12);
            int pad = (int) (getResources().getDisplayMetrics().density * 16);
            tv.setPadding(pad, pad, pad, pad);
            scroll.addView(tv);
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("设备原始JSON")
                    .setView(scroll)
                    .setNegativeButton("关闭", null)
                    .setPositiveButton("复制", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            android.content.ClipboardManager cm = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            if (cm != null) {
                                cm.setPrimaryClip(android.content.ClipData.newPlainText("devices_json", json));
                                Toast.makeText(DeviceManagementActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .show();
        } catch (Exception e) {
            Toast.makeText(this, "展示失败", Toast.LENGTH_SHORT).show();
        }
    }
}
