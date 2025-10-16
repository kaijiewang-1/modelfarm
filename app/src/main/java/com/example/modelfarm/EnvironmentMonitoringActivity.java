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

import com.example.modelfarm.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class EnvironmentMonitoringActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvTemperature;
    private TextView tvHumidity;
    private TextView tvLight;
    private TextView tvSoilMoisture;
    private MaterialButton btnEnhancedMonitoring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_environment_monitoring);

        initViews();
        setupToolbar();
        loadEnvironmentData();
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
        // 模拟环境数据
        tvTemperature.setText("25°C");
        tvHumidity.setText("65%");
        tvLight.setText("800 lux");
        // tvSoilMoisture.setText("45%");

        // 暂时注释掉不存在的按钮点击事件
        // btnEnhancedMonitoring.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         Intent intent = new Intent(EnvironmentMonitoringActivity.this, EnvironmentMonitoringEnhancedActivity.class);
        //         startActivity(intent);
        //     }
        // });
    }
}