package com.example.modelfarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备列表页面
 * 显示和管理所有设备
 */
public class DeviceListActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTitle;
    private RecyclerView rvDeviceList;
    private TextView tvEmptyState;

    private DeviceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_list);

        initViews();
        setupRecyclerView();
        updateEmptyState();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        rvDeviceList = findViewById(R.id.rvDeviceList);
        tvEmptyState = findViewById(R.id.tvEmptyState);
    }

    private void setupRecyclerView() {
        // 创建模拟设备数据
        List<DeviceManagementActivity.Device> deviceManagementList = new ArrayList<>();
        deviceManagementList.add(new DeviceManagementActivity.Device(
            "温湿度传感器001",
            "在线",
            "22°C / 65%",
            R.drawable.ic_thermometer
        ));
        deviceManagementList.add(new DeviceManagementActivity.Device(
            "光照传感器002",
            "离线",
            "无数据",
            R.drawable.ic_light
        ));
        deviceManagementList.add(new DeviceManagementActivity.Device(
            "自动灌溉系统001",
            "在线",
            "运行中",
            R.drawable.ic_water_drop
        ));

        adapter = new DeviceAdapter(deviceManagementList, new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onDeviceClick(DeviceManagementActivity.Device device) {
                // 跳转到设备详情页面
                Intent intent = new Intent(DeviceListActivity.this, DeviceDetailActivity.class);
                intent.putExtra("device_name", device.getName());
                intent.putExtra("device_type", "传感器");
                intent.putExtra("device_status", device.getStatus());
                startActivity(intent);
            }
        });
        
        rvDeviceList.setLayoutManager(new LinearLayoutManager(this));
        rvDeviceList.setAdapter(adapter);
    }

    private void updateEmptyState() {
        if (adapter.getItemCount() == 0) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvDeviceList.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvDeviceList.setVisibility(View.VISIBLE);
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
