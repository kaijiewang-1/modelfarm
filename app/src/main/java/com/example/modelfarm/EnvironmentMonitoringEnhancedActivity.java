package com.example.modelfarm;

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

public class EnvironmentMonitoringEnhancedActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvTemperature;
    private TextView tvHumidity;
    private TextView tvLight;
    private TextView tvSoilHumidity;
    private TextView tvCo2;
    private TextView tvWindSpeed;
    private RecyclerView rvAlerts;
    private MaterialButton btnToday;
    private MaterialButton btnWeek;
    private MaterialButton btnMonth;
    
    private AlertAdapter alertAdapter;
    private List<Alert> alertList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_environment_monitoring_enhanced);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupTimeButtons();
        updateEnvironmentData();
        loadAlertData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvLight = findViewById(R.id.tv_light);
        tvSoilHumidity = findViewById(R.id.tv_soil_humidity);
        tvCo2 = findViewById(R.id.tv_co2);
        tvWindSpeed = findViewById(R.id.tv_wind_speed);
        rvAlerts = findViewById(R.id.rv_alerts);
        btnToday = findViewById(R.id.btn_today);
        btnWeek = findViewById(R.id.btn_week);
        btnMonth = findViewById(R.id.btn_month);
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
        alertList = new ArrayList<>();
        alertAdapter = new AlertAdapter(alertList);
        rvAlerts.setLayoutManager(new LinearLayoutManager(this));
        rvAlerts.setAdapter(alertAdapter);
    }

    private void setupTimeButtons() {
        btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimeRange(btnToday, btnWeek, btnMonth);
                // 加载今日数据
            }
        });

        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimeRange(btnWeek, btnToday, btnMonth);
                // 加载本周数据
            }
        });

        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimeRange(btnMonth, btnToday, btnWeek);
                // 加载本月数据
            }
        });

        // 默认选中今日
        selectTimeRange(btnToday, btnWeek, btnMonth);
    }

    private void selectTimeRange(MaterialButton selected, MaterialButton... others) {
        selected.setBackgroundColor(getResources().getColor(R.color.material_dynamic_primary));
        selected.setTextColor(getResources().getColor(android.R.color.white));
        
        for (MaterialButton other : others) {
            other.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            other.setTextColor(getResources().getColor(R.color.material_dynamic_primary));
        }
    }

    private void updateEnvironmentData() {
        // 模拟环境数据更新
        tvTemperature.setText("25°C");
        tvHumidity.setText("65%");
        tvLight.setText("800Lux");
        tvSoilHumidity.setText("45%");
        tvCo2.setText("400ppm");
        tvWindSpeed.setText("2.5m/s");
    }

    private void loadAlertData() {
        // 模拟报警数据
        alertList.clear();
        alertList.add(new Alert("温度异常", "A区大棚温度超过30°C", "2024-01-15 14:30", "high"));
        alertList.add(new Alert("湿度不足", "B区大棚湿度低于40%", "2024-01-15 13:45", "medium"));
        alertList.add(new Alert("设备离线", "C区光照传感器离线", "2024-01-15 12:20", "low"));
        
        alertAdapter.notifyDataSetChanged();
    }

    // 报警数据模型
    public static class Alert {
        private String title;
        private String message;
        private String time;
        private String level;

        public Alert(String title, String message, String time, String level) {
            this.title = title;
            this.message = message;
            this.time = time;
            this.level = level;
        }

        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public String getTime() { return time; }
        public String getLevel() { return level; }
    }
}
