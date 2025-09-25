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
import com.google.android.material.card.MaterialCardView;

import farm.farm_list;
import login.login;

public class DashboardActivity extends AppCompatActivity {

    private MaterialCardView cardDeviceManagement;
    private MaterialCardView cardEnvironmentMonitoring;
    private MaterialCardView cardFarmManagement;
    private MaterialCardView cardDataAnalysis;
    private MaterialCardView cardCompanyInfo;
    private MaterialCardView cardProfile;
    private TextView tvTemperature;
    private TextView tvHumidity;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        initViews();
        setupClickListeners();
        updateRealTimeData();
    }

    private void initViews() {
        cardDeviceManagement = findViewById(R.id.card_device_management);
        cardEnvironmentMonitoring = findViewById(R.id.card_environment_monitoring);
        cardFarmManagement = findViewById(R.id.card_farm_management);
        cardDataAnalysis = findViewById(R.id.card_data_analysis);
        cardCompanyInfo = findViewById(R.id.card_company_info);
        cardProfile = findViewById(R.id.card_profile);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvWelcome = findViewById(R.id.tv_welcome);
    }

    private void setupClickListeners() {
        // 设备管理
        cardDeviceManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, DeviceManagementActivity.class);
                startActivity(intent);
            }
        });

        // 环境监控
        cardEnvironmentMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EnvironmentMonitoringActivity.class);
                startActivity(intent);
            }
        });

        // 农场管理
        cardFarmManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, farm_list.class);
                startActivity(intent);
            }
        });

        // 数据分析
        cardDataAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, DataAnalysisActivity.class);
                startActivity(intent);
            }
        });

        // 企业信息
        cardCompanyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, company.company_info.class);
                startActivity(intent);
            }
        });

        // 个人资料
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, personal.profile.class);
                startActivity(intent);
            }
        });
    }

    private void updateRealTimeData() {
        // 模拟实时数据更新
        tvTemperature.setText("25°C");
        tvHumidity.setText("65%");
        tvWelcome.setText("欢迎回来，张先生");
    }
}
