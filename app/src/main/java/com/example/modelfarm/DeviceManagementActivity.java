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
import com.example.modelfarm.utils.SimpleApiHelper;

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
    
    // API相关
    private SimpleApiHelper simpleApiHelper;

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
        simpleApiHelper = new SimpleApiHelper(this);
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
        deviceAdapter = new DeviceAdapter(deviceList, new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onDeviceClick(Device device) {
                // 处理设备点击事件
                Toast.makeText(DeviceManagementActivity.this, "点击了设备: " + device.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        rvDevices.setAdapter(deviceAdapter);
    }

    private void loadDeviceData() {
        // 从API获取设备数据
        simpleApiHelper.getDeviceList(new SimpleApiHelper.DeviceListCallback() {
            @Override
            public void onSuccess(List<SimpleApiHelper.DeviceData> devices) {
                runOnUiThread(() -> {
                    // 清空现有数据
                    deviceList.clear();
                    
                    // 转换API数据为本地设备模型
                    for (SimpleApiHelper.DeviceData apiDevice : devices) {
                        Device localDevice = convertApiDeviceToLocal(apiDevice);
                        deviceList.add(localDevice);
                    }
                    
                    deviceAdapter.notifyDataSetChanged();
                    updateDeviceStatistics();
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(DeviceManagementActivity.this, "获取设备数据失败: " + errorMessage, Toast.LENGTH_LONG).show();
                    // 显示空状态
                    deviceList.clear();
                    deviceAdapter.notifyDataSetChanged();
                    updateDeviceStatistics();
                });
            }
        });
    }
    
    /**
     * 将API设备数据转换为本地设备模型
     */
    private Device convertApiDeviceToLocal(SimpleApiHelper.DeviceData apiDevice) {
        // 使用设备模型的内置方法
        String status = getDeviceStatusText(apiDevice.status);
        String value = apiDevice.value;
        int iconRes = getDeviceIconResource(apiDevice.type);
        
        return new Device(apiDevice.name, status, value, iconRes);
    }
    
    private String getDeviceStatusText(int status) {
        switch (status) {
            case 1: return "在线";
            case 0: return "离线";
            case -1: return "故障";
            default: return "未知";
        }
    }
    
    private int getDeviceIconResource(int type) {
        switch (type) {
            case 1: return R.drawable.ic_camera;
            case 2: return R.drawable.ic_thermometer;
            case 3: return R.drawable.ic_control;
            default: return R.drawable.ic_device;
        }
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
