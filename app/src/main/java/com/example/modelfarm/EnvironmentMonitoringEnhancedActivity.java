package com.example.modelfarm;

import android.os.Bundle;
import android.text.Layout;
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
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.Device;
import com.example.modelfarm.network.models.DeviceData;
import com.example.modelfarm.network.models.Notification;
import com.example.modelfarm.network.services.DeviceApiService;
import com.example.modelfarm.network.services.DeviceDataApiService;
import com.example.modelfarm.network.services.NotificationApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

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
    private View history_data;
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
        history_data = (View) findViewById(R.id.history_data).getParent();

        history_data.setVisibility(INVISIBLE);
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
        // 从设备列表中过滤传感器(type=2)，取任一获取最新数据
        DeviceApiService deviceApi = RetrofitClient.create(this, DeviceApiService.class);
        deviceApi.getDeviceList(1, 50, null, null, 2, null).enqueue(new Callback<ApiResponse<com.example.modelfarm.network.models.PageResponse<Device>>>() {
            @Override
            public void onResponse(Call<ApiResponse<com.example.modelfarm.network.models.PageResponse<Device>>> call, Response<ApiResponse<com.example.modelfarm.network.models.PageResponse<Device>>> response) {
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200 && response.body().getData()!=null && !response.body().getData().getRecords().isEmpty()) {
                    Device sensor = response.body().getData().getRecords().get(0);
                    fetchLatestData(sensor.getId());
                } else {
                    showEnvPlaceholders();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<com.example.modelfarm.network.models.PageResponse<Device>>> call, Throwable t) {
                showEnvPlaceholders();
            }
        });
    }

    private void fetchLatestData(int deviceId) {
        DeviceDataApiService dataApi = RetrofitClient.create(this, DeviceDataApiService.class);
        dataApi.getLatestDeviceData(deviceId).enqueue(new Callback<ApiResponse<DeviceData>>() {
            @Override
            public void onResponse(Call<ApiResponse<DeviceData>> call, Response<ApiResponse<DeviceData>> response) {
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200 && response.body().getData()!=null) {
                    java.util.Map<String, Object> data = response.body().getData().getData();
                    String temperature = data.get("temperature")!=null ? data.get("temperature").toString()+"°C" : "--°C";
                    String humidity = data.get("humidity")!=null ? data.get("humidity").toString()+"%" : "68%";
                    String light = data.get("light")!=null ? data.get("light").toString()+"Lux" : "41Lux";
                    tvTemperature.setText(temperature);
                    tvHumidity.setText(humidity);
                    tvLight.setText(light);
                } else {
                    showEnvPlaceholders();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<DeviceData>> call, Throwable t) {
                showEnvPlaceholders();
            }
        });
    }

    private void showEnvPlaceholders() {
        tvTemperature.setText("--°C");
        tvHumidity.setText("--%");
        tvLight.setText("--Lux");
        if (tvSoilHumidity != null) tvSoilHumidity.setText("--%");
        if (tvCo2 != null) tvCo2.setText("--ppm");
        if (tvWindSpeed != null) tvWindSpeed.setText("--m/s");
    }

    private void loadAlertData() {
        // 使用未删除的通知作为预警来源
        NotificationApiService notificationApi = RetrofitClient.create(this, NotificationApiService.class);
        notificationApi.getAllNotifications().enqueue(new Callback<ApiResponse<java.util.List<Notification>>>() {
            @Override
            public void onResponse(Call<ApiResponse<java.util.List<Notification>>> call, Response<ApiResponse<java.util.List<Notification>>> response) {
                alertList.clear();
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200 && response.body().getData()!=null) {
                    for (Notification n : response.body().getData()) {
                        String level = n.getType()==2 ? "high" : (n.getType()==3 ? "medium" : "low");
                        alertList.add(new Alert(n.getTitle(), n.getContent(), n.getCreatedAt(), level));
                    }
                }
                alertAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ApiResponse<java.util.List<Notification>>> call, Throwable t) {
                // 保持空列表
                alertAdapter.notifyDataSetChanged();
            }
        });
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
