package farm;

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

import com.example.modelfarm.DeviceManagementActivity;
import com.example.modelfarm.DeviceAdapter;
import com.example.modelfarm.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class farm_point_detail extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvPointName;
    private TextView tvArea;
    private TextView tvCrop;
    private TextView tvTime;
    private TextView tvTotalDevices;
    private TextView tvOnlineDevices;
    private TextView tvOfflineDevices;
    private MaterialButton btnAddDevice;
    private RecyclerView rvDevices;
    private DeviceAdapter deviceAdapter;
    private List<DeviceManagementActivity.Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farm_point_detail);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadPointData();
        loadDeviceData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvPointName = findViewById(R.id.tv_point_name);
        tvArea = findViewById(R.id.tv_area);
        tvCrop = findViewById(R.id.tv_crop);
        tvTime = findViewById(R.id.tv_time);
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
                // 返回农场详情
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

    private void loadPointData() {
        // 从Intent获取农场点信息
        Intent intent = getIntent();
        String pointName = intent.getStringExtra("point_name");
        String pointArea = intent.getStringExtra("point_area");
        String pointCrop = intent.getStringExtra("point_crop");
        
        if (pointName != null) {
            tvPointName.setText(pointName);
        }
        if (pointArea != null) {
            tvArea.setText("面积：" + pointArea);
        }
        if (pointCrop != null) {
            tvCrop.setText("种植作物：" + pointCrop);
        }
        
        // 设置其他信息
        tvTime.setText("建成时间：2023年3月");
    }

    private void loadDeviceData() {
        // 模拟设备数据
        deviceList.clear();
        deviceList.add(new DeviceManagementActivity.Device("温度传感器-001", "在线", "25.5°C", R.drawable.ic_thermometer));
        deviceList.add(new DeviceManagementActivity.Device("湿度传感器-002", "在线", "65%", R.drawable.ic_humidity));
        deviceList.add(new DeviceManagementActivity.Device("光照传感器-003", "离线", "无数据", R.drawable.ic_light));
        deviceList.add(new DeviceManagementActivity.Device("通风设备-004", "在线", "运行中", R.drawable.ic_fan));
        deviceList.add(new DeviceManagementActivity.Device("加热设备-005", "在线", "待机", R.drawable.ic_heater));

        deviceAdapter.notifyDataSetChanged();

        // 更新统计信息
        int total = deviceList.size();
        int online = 0;
        int offline = 0;

        for (DeviceManagementActivity.Device device : deviceList) {
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
}