package com.example.modelfarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class DataAnalysisActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MaterialButton btnCropAnalysis;
    private MaterialButton btnYieldAnalysis;
    private MaterialButton btnWeatherAnalysis;
    private MaterialButton btnEnhancedAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_analysis);

        initViews();
        setupToolbar();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        // 暂时注释掉不存在的资源引用
        // btnCropAnalysis = findViewById(R.id.btn_crop_analysis);
        // btnYieldAnalysis = findViewById(R.id.btn_yield_analysis);
        // btnWeatherAnalysis = findViewById(R.id.btn_weather_analysis);
        // btnEnhancedAnalysis = findViewById(R.id.btn_enhanced_analysis);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回主控制台
                Intent intent = new Intent(DataAnalysisActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupClickListeners() {
        // 暂时注释掉不存在的按钮点击事件
        // 这些功能将在布局文件创建后启用
    }
}