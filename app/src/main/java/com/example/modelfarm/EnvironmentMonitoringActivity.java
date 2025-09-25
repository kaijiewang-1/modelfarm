package com.example.modelfarm;

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

public class EnvironmentMonitoringActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvTemperatureValue;
    private TextView tvTemperatureStatus;
    private TextView tvHumidityValue;
    private TextView tvHumidityStatus;
    private TextView tvLightValue;
    private TextView tvLightStatus;
    private TextView tvAirQualityValue;
    private TextView tvAirQualityStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_environment_monitoring);

        initViews();
        setupToolbar();
        updateEnvironmentData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTemperatureValue = findViewById(R.id.tv_temperature_value);
        tvTemperatureStatus = findViewById(R.id.tv_temperature_status);
        tvHumidityValue = findViewById(R.id.tv_humidity_value);
        tvHumidityStatus = findViewById(R.id.tv_humidity_status);
        tvLightValue = findViewById(R.id.tv_light_value);
        tvLightStatus = findViewById(R.id.tv_light_status);
        tvAirQualityValue = findViewById(R.id.tv_air_quality_value);
        tvAirQualityStatus = findViewById(R.id.tv_air_quality_status);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateEnvironmentData() {
        // 模拟环境数据更新
        tvTemperatureValue.setText("25.5°C");
        tvTemperatureStatus.setText("正常");
        tvTemperatureStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        tvHumidityValue.setText("65%");
        tvHumidityStatus.setText("正常");
        tvHumidityStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        tvLightValue.setText("800 lux");
        tvLightStatus.setText("正常");
        tvLightStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        tvAirQualityValue.setText("良好");
        tvAirQualityStatus.setText("正常");
        tvAirQualityStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
    }
}
