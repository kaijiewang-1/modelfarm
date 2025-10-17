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

    private String deviceName;
    private String deviceType;
    private String deviceStatus;

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
    }

    private void loadDeviceData() {
        // 获取传递的设备信息
        Intent intent = getIntent();
        deviceName = intent.getStringExtra("device_name");
        deviceType = intent.getStringExtra("device_type");
        deviceStatus = intent.getStringExtra("device_status");

        // 设置设备信息
        tvTitle.setText(deviceName != null ? deviceName : "设备详情");
        tvDeviceType.setText(deviceType != null ? deviceType : "传感器");
        tvInstallLocation.setText("北方一号农场 - A区大棚");
        tvDeviceStatus.setText(deviceStatus != null ? deviceStatus : "在线");
        tvLastMaintenance.setText("2024-01-15");

        // 设置状态颜色
        if ("在线".equals(deviceStatus)) {
            tvDeviceStatus.setTextColor(0xFF4CAF50);
        } else {
            tvDeviceStatus.setTextColor(0xFFF44336);
        }
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
