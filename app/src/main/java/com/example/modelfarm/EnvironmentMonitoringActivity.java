package com.example.modelfarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import java.util.Map;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.example.modelfarm.network.repositories.DeviceRepository;
import com.example.modelfarm.utils.DeviceDataParser;
import com.google.android.material.button.MaterialButton;

public class EnvironmentMonitoringActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvTemperature;
    private TextView tvHumidity;
    private TextView tvLight;
    
    // API相关
    private DeviceRepository deviceRepository;
    private TextView tvSoilMoisture;
    private MaterialButton btnEnhancedMonitoring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_environment_monitoring);

        initViews();
        initApiComponents();
        setupToolbar();
        loadEnvironmentData();
    }

    /**
     * 初始化API组件
     */
    private void initApiComponents() {
        deviceRepository = new DeviceRepository(this);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvLight = findViewById(R.id.tv_light);
        // 暂时注释掉不存在的资源引用
        // tvSoilMoisture = findViewById(R.id.tv_soil_moisture);
        // btnEnhancedMonitoring = findViewById(R.id.btn_enhanced_monitoring);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回主控制台
                Intent intent = new Intent(EnvironmentMonitoringActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadEnvironmentData() {
        // 从API获取环境数据
        deviceRepository.getDeviceList(1, 100, null, null, 2, null, new DeviceRepository.DeviceListCallback() {
            @Override
            public void onSuccess(List<com.example.modelfarm.network.models.Device> devices) {
                runOnUiThread(() -> {
                    // 查找传感器设备并获取最新数据
                    for (com.example.modelfarm.network.models.Device device : devices) {
                        if (device.getType() == 2) { // 传感器设备
                            loadDeviceLatestData(device.getId());
                        }
                    }
                    
                    // 如果没有传感器设备，显示默认数据
                    if (devices.isEmpty()) {
                        tvTemperature.setText("--°C");
                        tvHumidity.setText("--%");
                        tvLight.setText("-- lux");
                    }
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(EnvironmentMonitoringActivity.this, "获取环境数据失败: " + errorMessage, Toast.LENGTH_SHORT).show();
                    // 显示默认数据
                    tvTemperature.setText("--°C");
                    tvHumidity.setText("--%");
                    tvLight.setText("-- lux");
                });
            }
        });
    }
    
    /**
     * 加载设备最新数据
     */
    private void loadDeviceLatestData(int deviceId) {
        deviceRepository.getLatestDeviceData(deviceId, new DeviceRepository.LatestDeviceDataCallback() {
            @Override
            public void onSuccess(com.example.modelfarm.network.models.DeviceData deviceData) {
                runOnUiThread(() -> {
                    // 使用数据解析器解析设备数据
                    Map<String, Object> data = deviceData.getData();
                    if (DeviceDataParser.isValidData(data)) {
                        // 解析并更新环境数据
                        String temperature = DeviceDataParser.parseTemperature(data);
                        String humidity = DeviceDataParser.parseHumidity(data);
                        String light = DeviceDataParser.parseLight(data);
                        
                        tvTemperature.setText(temperature);
                        tvHumidity.setText(humidity);
                        tvLight.setText(light);
                    }
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                // 静默处理错误，不显示Toast
            }
        });
    }
}