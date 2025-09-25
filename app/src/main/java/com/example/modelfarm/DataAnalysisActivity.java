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
import com.google.android.material.button.MaterialButton;

public class DataAnalysisActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MaterialButton btnToday;
    private MaterialButton btnWeek;
    private MaterialButton btnMonth;
    private TextView tvAvgTemperature;
    private TextView tvAvgHumidity;
    private TextView tvDeviceRuntime;
    private TextView tvAlarmCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_analysis);

        initViews();
        setupToolbar();
        setupTimeButtons();
        loadAnalysisData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        btnToday = findViewById(R.id.btn_today);
        btnWeek = findViewById(R.id.btn_week);
        btnMonth = findViewById(R.id.btn_month);
        tvAvgTemperature = findViewById(R.id.tv_avg_temperature);
        tvAvgHumidity = findViewById(R.id.tv_avg_humidity);
        tvDeviceRuntime = findViewById(R.id.tv_device_runtime);
        tvAlarmCount = findViewById(R.id.tv_alarm_count);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupTimeButtons() {
        // 默认选中今天
        btnToday.setSelected(true);
        
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimeButton(btnToday);
                loadAnalysisData("today");
            }
        });

        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimeButton(btnWeek);
                loadAnalysisData("week");
            }
        });

        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimeButton(btnMonth);
                loadAnalysisData("month");
            }
        });
    }

    private void selectTimeButton(MaterialButton selectedButton) {
        btnToday.setSelected(false);
        btnWeek.setSelected(false);
        btnMonth.setSelected(false);
        selectedButton.setSelected(true);
    }

    private void loadAnalysisData() {
        loadAnalysisData("today");
    }

    private void loadAnalysisData(String timeRange) {
        // 模拟数据分析结果
        switch (timeRange) {
            case "today":
                tvAvgTemperature.setText("24.8°C");
                tvAvgHumidity.setText("68%");
                tvDeviceRuntime.setText("98.5%");
                tvAlarmCount.setText("3");
                break;
            case "week":
                tvAvgTemperature.setText("25.2°C");
                tvAvgHumidity.setText("65%");
                tvDeviceRuntime.setText("97.8%");
                tvAlarmCount.setText("12");
                break;
            case "month":
                tvAvgTemperature.setText("24.5°C");
                tvAvgHumidity.setText("70%");
                tvDeviceRuntime.setText("96.2%");
                tvAlarmCount.setText("45");
                break;
        }
    }
}
