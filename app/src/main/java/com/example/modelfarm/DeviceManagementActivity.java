package com.example.modelfarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class DeviceManagementActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvTotalDevices;
    private TextView tvOnlineDevices;
    private TextView tvOfflineDevices;
    private MaterialButton btnAddDevice;
    private RecyclerView rvDevices;
    private DeviceAdapter deviceAdapter;
    private List<Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_management);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadDeviceData();
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

    private void setupRecyclerView() {
        deviceList = new ArrayList<>();
        deviceAdapter = new DeviceAdapter(deviceList);
        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        rvDevices.setAdapter(deviceAdapter);
    }

    private void loadDeviceData() {
        // 模拟设备数据
        deviceList.clear();
        deviceList.add(new Device("温度传感器-001", "在线", "25.5°C", R.drawable.ic_thermometer));
        deviceList.add(new Device("湿度传感器-002", "在线", "65%", R.drawable.ic_humidity));
        deviceList.add(new Device("光照传感器-003", "离线", "无数据", R.drawable.ic_light));
        deviceList.add(new Device("通风设备-004", "在线", "运行中", R.drawable.ic_fan));
        deviceList.add(new Device("加热设备-005", "在线", "待机", R.drawable.ic_heater));
        deviceList.add(new Device("监控摄像头-006", "离线", "无信号", R.drawable.ic_camera));

        deviceAdapter.notifyDataSetChanged();

        // 更新统计信息
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

    // 设备数据模型
    public static class Device {
        private String name;
        private String status;
        private String value;
        private int iconRes;

        public Device(String name, String status, String value, int iconRes) {
            this.name = name;
            this.status = status;
            this.value = value;
            this.iconRes = iconRes;
        }

        public String getName() { return name; }
        public String getStatus() { return status; }
        public String getValue() { return value; }
        public int getIconRes() { return iconRes; }
    }
}
