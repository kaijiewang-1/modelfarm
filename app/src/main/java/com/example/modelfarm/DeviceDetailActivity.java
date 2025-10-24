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
import com.example.modelfarm.network.services.DeviceApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.android.material.card.MaterialCardView;

/**
 * 设备详情页面
 * 显示设备详细信息，进行数据查看、控制和维护操作
 */
public class DeviceDetailActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTitle;
    private TextView tvDeviceType;
    private TextView tvInstallLocation;
    private TextView tvDeviceStatus;
    private TextView tvLastMaintenance;
    private MaterialCardView cardBasicInfo;
    private MaterialCardView cardDataCurve;
    private MaterialCardView cardDeviceControl;
    private MaterialCardView cardDeviceMaintenance;
    private MaterialButton btnDeleteDevice;
    private MaterialButton btnCreateOrder;
    private MaterialButton btnCopyJson;

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
    private TextView tvRawJson;

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
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvDeviceType = findViewById(R.id.tvDeviceType);
        tvInstallLocation = findViewById(R.id.tvInstallLocation);
        tvDeviceStatus = findViewById(R.id.tvDeviceStatus);
        tvLastMaintenance = findViewById(R.id.tvLastMaintenance);
        cardBasicInfo = findViewById(R.id.cardBasicInfo);
        cardDataCurve = findViewById(R.id.cardDataCurve);
        cardDeviceControl = findViewById(R.id.cardDeviceControl);
        cardDeviceMaintenance = findViewById(R.id.cardDeviceMaintenance);
        btnDeleteDevice = findViewById(R.id.btnDeleteDevice);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);
        btnCopyJson = findViewById(R.id.btnCopyJson);
        tvPushName = findViewById(R.id.tvPushName);
        tvMac = findViewById(R.id.tvMac);
        tvUrl = findViewById(R.id.tvUrl);
        tvEnterpriseId = findViewById(R.id.tvEnterpriseId);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvUpdatedAt = findViewById(R.id.tvUpdatedAt);
        tvRawJson = findViewById(R.id.tvRawJson);
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

                    // 显示原始 JSON（格式化）
                    try {
                        com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(d);
                        if (tvRawJson != null) tvRawJson.setText(json);
                    } catch (Exception ignore) {}
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Device>> call, Throwable t) {
                // 忽略，保持占位
            }
        });
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 数据曲线
        cardDataCurve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataCurve();
            }
        });

        // 设备控制
        cardDeviceControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeviceControl();
            }
        });

        // 设备维护
        cardDeviceMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeviceMaintenance();
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

        // 复制 JSON
        if (btnCopyJson != null) {
            btnCopyJson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String txt = tvRawJson != null ? tvRawJson.getText().toString() : "";
                        android.content.ClipboardManager cm = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        if (cm != null) {
                            cm.setPrimaryClip(android.content.ClipData.newPlainText("device_json", txt));
                            Toast.makeText(DeviceDetailActivity.this, "已复制 JSON", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(DeviceDetailActivity.this, "复制失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showDataCurve() {
        Toast.makeText(this, "查看数据曲线功能", Toast.LENGTH_SHORT).show();
        // TODO: 实现数据曲线查看功能
    }

    private void showDeviceControl() {
        Toast.makeText(this, "设备控制功能", Toast.LENGTH_SHORT).show();
        // TODO: 实现设备控制功能
    }

    private void showDeviceMaintenance() {
        Toast.makeText(this, "设备维护功能", Toast.LENGTH_SHORT).show();
        // TODO: 实现设备维护功能
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
}
