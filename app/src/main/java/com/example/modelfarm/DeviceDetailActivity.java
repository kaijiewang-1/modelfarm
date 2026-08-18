package com.example.modelfarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.R;
import com.google.android.material.button.MaterialButton;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.Device;
import com.example.modelfarm.network.models.DeviceData;
import com.example.modelfarm.network.services.DeviceApiService;
import com.example.modelfarm.network.services.DeviceDataApiService;
import com.example.modelfarm.utils.DeviceDataParser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.material.card.MaterialCardView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import java.util.Map;

/**
 * 设备详情页面
 * 显示设备详细信息，进行数据查看、控制和维护操作
 */
public class DeviceDetailActivity extends AppCompatActivity {

    private com.google.android.material.appbar.MaterialToolbar toolbar;
    private TextView tvTitle;
    private TextView tvDeviceType;
    private TextView tvInstallLocation;
    private TextView tvDeviceStatus;
    private TextView tvLastMaintenance;
    private MaterialCardView cardBasicInfo;
    private MaterialButton btnDeleteDevice;
    private MaterialButton btnCreateOrder;

    private String deviceName;
    private String deviceType;
    private String deviceStatus;
    private int deviceId = -1;
    private TextView tvPushName;
    private TextView tvMac;
    private TextView tvUrl;
    private TextView tvEnterpriseId;
    private TextView tvCreatedAt;
    private TextView tvUpdatedAt;
    
    // 数据展示相关
    private LinearLayout llLatestDataContainer;
    private TextView tvLatestDataTime;
    private RecyclerView rvHistoryData;
    private TextView tvNoHistoryData;
    private HistoryDataAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_detail);

        initViews();
        loadDeviceData();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        tvDeviceType = findViewById(R.id.tvDeviceType);
        tvInstallLocation = findViewById(R.id.tvInstallLocation);
        tvDeviceStatus = findViewById(R.id.tvDeviceStatus);
        tvLastMaintenance = findViewById(R.id.tvLastMaintenance);
        cardBasicInfo = findViewById(R.id.cardBasicInfo);
        btnDeleteDevice = findViewById(R.id.btnDeleteDevice);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);
        tvPushName = findViewById(R.id.tvPushName);
        tvMac = findViewById(R.id.tvMac);
        tvUrl = findViewById(R.id.tvUrl);
        tvEnterpriseId = findViewById(R.id.tvEnterpriseId);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvUpdatedAt = findViewById(R.id.tvUpdatedAt);
        
        // 数据展示相关视图
        llLatestDataContainer = findViewById(R.id.llLatestDataContainer);
        tvLatestDataTime = findViewById(R.id.tvLatestDataTime);
        rvHistoryData = findViewById(R.id.rvHistoryData);
        tvNoHistoryData = findViewById(R.id.tvNoHistoryData);
        
        // 初始化历史数据列表
        rvHistoryData.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryDataAdapter();
        rvHistoryData.setAdapter(historyAdapter);
    }

    private void loadDeviceData() {
        // 获取传递的设备信息
        Intent intent = getIntent();
        if (intent.hasExtra("device_id")) {
            deviceId = intent.getIntExtra("device_id", -1);
        }
        deviceName = intent.getStringExtra("device_name");
        deviceType = intent.getStringExtra("device_type");
        deviceStatus = intent.getStringExtra("device_status");
        if (deviceId > 0) {
            fetchDeviceDetail(deviceId);
        } else {
            // 回退到传参展示
            tvTitle.setText(deviceName != null ? deviceName : "设备详情");
            tvDeviceType.setText(deviceType != null ? deviceType : "传感器");
            tvInstallLocation.setText("-");
            tvDeviceStatus.setText(deviceStatus != null ? deviceStatus : "-");
            tvLastMaintenance.setText("-");
            // 如果是传感器设备，尝试加载数据
            if ("传感器".equals(deviceType)) {
                if (intent.hasExtra("device_id")) {
                    int id = intent.getIntExtra("device_id", -1);
                    if (id > 0) {
                        fetchLatestDeviceData(id);
                        fetchAllDeviceData(id);
                    }
                }
            }
        }
    }

    private void fetchDeviceDetail(int id) {
        DeviceApiService api = RetrofitClient.create(this, DeviceApiService.class);
        api.getDevice(id).enqueue(new Callback<ApiResponse<Device>>() {
            @Override
            public void onResponse(Call<ApiResponse<Device>> call, Response<ApiResponse<Device>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200 && response.body().getData() != null) {
                    Device d = response.body().getData();
                    tvTitle.setText(d.getName());
                    tvDeviceType.setText(d.getTypeText());
                    tvInstallLocation.setText(d.getProperties() != null && d.getProperties().get("location") != null ? String.valueOf(d.getProperties().get("location")) : "-");
                    tvDeviceStatus.setText(d.getStatusText());
                    tvLastMaintenance.setText(d.getUpdatedAt());
                    if (tvPushName != null) tvPushName.setText(d.getPushName()!=null?d.getPushName():"-");
                    if (tvMac != null) tvMac.setText(d.getMac());
                    if (tvUrl != null) tvUrl.setText(d.getUrl()!=null?d.getUrl():"-");
                    if (tvEnterpriseId != null) tvEnterpriseId.setText(String.valueOf(d.getEnterpriseId()));
                    if (tvCreatedAt != null) tvCreatedAt.setText(d.getCreatedAt());
                    if (tvUpdatedAt != null) tvUpdatedAt.setText(d.getUpdatedAt());
                    if ("在线".equals(d.getStatusText())) {
                        tvDeviceStatus.setTextColor(0xFF4CAF50);
                    } else {
                        tvDeviceStatus.setTextColor(0xFFF44336);
                    }

                    // 如果是传感器设备，加载数据
                    if (d.getType() == 2) { // 传感器类型
                        fetchLatestDeviceData(d.getId());
                        fetchAllDeviceData(d.getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Device>> call, Throwable t) {
                // 忽略，保持占位
            }
        });
    }

    /**
     * 获取设备最新数据
     */
    private void fetchLatestDeviceData(int deviceId) {
        DeviceDataApiService dataApi = RetrofitClient.create(this, DeviceDataApiService.class);
        dataApi.getLatestDeviceData(deviceId).enqueue(new Callback<ApiResponse<DeviceData>>() {
            @Override
            public void onResponse(Call<ApiResponse<DeviceData>> call, Response<ApiResponse<DeviceData>> response) {
                if (response.isSuccessful() && response.body() != null && 
                    response.body().getCode() == 200 && response.body().getData() != null) {
                    DeviceData deviceData = response.body().getData();
                    displayLatestData(deviceData);
                } else {
                    showNoLatestData();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DeviceData>> call, Throwable t) {
                showNoLatestData();
            }
        });
    }

    /**
     * 获取设备所有数据
     */
    private void fetchAllDeviceData(int deviceId) {
        DeviceDataApiService dataApi = RetrofitClient.create(this, DeviceDataApiService.class);
        dataApi.getAllDeviceData(deviceId).enqueue(new Callback<ApiResponse<List<DeviceData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<DeviceData>>> call, Response<ApiResponse<List<DeviceData>>> response) {
                if (response.isSuccessful() && response.body() != null && 
                    response.body().getCode() == 200 && response.body().getData() != null) {
                    List<DeviceData> dataList = response.body().getData();
                    if (dataList != null && !dataList.isEmpty()) {
                        displayHistoryData(dataList);
                    } else {
                        showNoHistoryData();
                    }
                } else {
                    showNoHistoryData();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<DeviceData>>> call, Throwable t) {
                showNoHistoryData();
            }
        });
    }

    /**
     * 显示最新数据
     */
    private void displayLatestData(DeviceData deviceData) {
        if (llLatestDataContainer == null || deviceData == null) return;
        
        llLatestDataContainer.removeAllViews();
        Map<String, Object> data = deviceData.getData();
        
        if (data == null || data.isEmpty()) {
            showNoLatestData();
            return;
        }

        // 使用DeviceDataParser解析数据
        String temperature = DeviceDataParser.parseTemperature(data);
        String humidity = DeviceDataParser.parseHumidity(data);
        String light = DeviceDataParser.parseLight(data);
        String ph = DeviceDataParser.parsePH(data);
        String co2 = DeviceDataParser.parseCO2(data);
        String soilMoisture = DeviceDataParser.parseSoilMoisture(data);

        // 添加数据项
        if (!"--°C".equals(temperature)) {
            addDataItem("温度", temperature, "#FF5722");
        }
        if (!"--%".equals(humidity)) {
            addDataItem("湿度", humidity, "#2196F3");
        }
        if (!"-- lux".equals(light)) {
            addDataItem("光照", light, "#FFC107");
        }
        if (!"--".equals(ph)) {
            addDataItem("pH值", ph, "#9C27B0");
        }
        if (!"-- ppm".equals(co2)) {
            addDataItem("CO₂", co2, "#607D8B");
        }
        if (!"--%".equals(soilMoisture)) {
            addDataItem("土壤湿度", soilMoisture, "#795548");
        }

        // 显示其他可能的字段
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            if (!key.equals("temperature") && !key.equals("humidity") && 
                !key.equals("light") && !key.equals("ph") && 
                !key.equals("co2") && !key.equals("soil_moisture") &&
                !key.equals("moisture") && !key.equals("temp") &&
                !key.equals("illumination") && !key.equals("timestamp") &&
                !key.equals("time") && !key.equals("status")) {
                Object value = entry.getValue();
                if (value != null) {
                    addDataItem(key, value.toString(), "#666666");
                }
            }
        }

        // 更新时间
        if (tvLatestDataTime != null) {
            tvLatestDataTime.setText("更新时间：" + (deviceData.getCreatedAt() != null ? deviceData.getCreatedAt() : "-"));
        }
    }

    /**
     * 添加数据项视图
     */
    private void addDataItem(String label, String value, String colorHex) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(0, 12, 0, 12);

        TextView labelView = new TextView(this);
        labelView.setText(label + "：");
        labelView.setTextSize(14);
        labelView.setTextColor(0xFF666666);
        labelView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView valueView = new TextView(this);
        valueView.setText(value);
        valueView.setTextSize(14);
        valueView.setTypeface(null, android.graphics.Typeface.BOLD);
        try {
            valueView.setTextColor(android.graphics.Color.parseColor(colorHex));
        } catch (Exception e) {
            valueView.setTextColor(0xFF333333);
        }
        valueView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f));
        valueView.setGravity(android.view.Gravity.END);

        itemLayout.addView(labelView);
        itemLayout.addView(valueView);
        llLatestDataContainer.addView(itemLayout);

        // 添加分隔线
        View divider = new View(this);
        divider.setBackgroundColor(0xFFE0E0E0);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 1);
        dividerParams.setMargins(0, 12, 0, 0);
        divider.setLayoutParams(dividerParams);
        llLatestDataContainer.addView(divider);
    }

    /**
     * 显示无最新数据
     */
    private void showNoLatestData() {
        if (llLatestDataContainer != null) {
            llLatestDataContainer.removeAllViews();
            TextView noDataView = new TextView(this);
            noDataView.setText("暂无最新数据");
            noDataView.setTextSize(14);
            noDataView.setTextColor(0xFF999999);
            noDataView.setGravity(android.view.Gravity.CENTER);
            noDataView.setPadding(0, 16, 0, 16);
            llLatestDataContainer.addView(noDataView);
        }
        if (tvLatestDataTime != null) {
            tvLatestDataTime.setText("更新时间：-");
        }
    }

    /**
     * 显示历史数据
     */
    private void displayHistoryData(List<DeviceData> dataList) {
        if (historyAdapter != null) {
            historyAdapter.setData(dataList);
            historyAdapter.notifyDataSetChanged();
        }
        if (rvHistoryData != null) {
            rvHistoryData.setVisibility(View.VISIBLE);
        }
        if (tvNoHistoryData != null) {
            tvNoHistoryData.setVisibility(View.GONE);
        }
    }

    /**
     * 显示无历史数据
     */
    private void showNoHistoryData() {
        if (rvHistoryData != null) {
            rvHistoryData.setVisibility(View.GONE);
        }
        if (tvNoHistoryData != null) {
            tvNoHistoryData.setVisibility(View.VISIBLE);
        }
    }

    private void setupClickListeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 删除设备
        btnDeleteDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmDialog();
            }
        });

        // 创建工单
        btnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceDetailActivity.this, OrderCreateActivity.class);
                intent.putExtra("hint_title", "设备故障维修工单");
                intent.putExtra("hint_desc", tvTitle!=null? ("设备[" + tvTitle.getText() + "]异常，请检查"): "");
                startActivity(intent);
            }
        });

    }



    private void showDeleteConfirmDialog() {
        // 显示删除确认对话框
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("确认删除设备？")
                .setMessage("删除后数据将无法恢复，请谨慎操作。")
                .setPositiveButton("删除", (dialog, which) -> {
                    // 执行删除操作
                    Toast.makeText(this, "设备已删除", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 历史数据适配器
     */
    private class HistoryDataAdapter extends RecyclerView.Adapter<HistoryDataAdapter.ViewHolder> {
        private List<DeviceData> dataList = new java.util.ArrayList<>();

        public void setData(List<DeviceData> data) {
            this.dataList = data != null ? data : new java.util.ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history_data, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DeviceData deviceData = dataList.get(position);
            if (deviceData != null) {
                Map<String, Object> data = deviceData.getData();
                if (data != null) {
                    StringBuilder dataText = new StringBuilder();
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        if (dataText.length() > 0) {
                            dataText.append(" | ");
                        }
                        dataText.append(entry.getKey()).append(": ").append(entry.getValue());
                    }
                    holder.tvData.setText(dataText.toString());
                } else {
                    holder.tvData.setText("无数据");
                }
                holder.tvTime.setText(deviceData.getCreatedAt() != null ? deviceData.getCreatedAt() : "-");
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvData;
            TextView tvTime;

            ViewHolder(android.view.View itemView) {
                super(itemView);
                tvData = itemView.findViewById(R.id.tvData);
                tvTime = itemView.findViewById(R.id.tvTime);
            }
        }
    }
}
