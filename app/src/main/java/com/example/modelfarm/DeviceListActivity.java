package com.example.modelfarm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.Device;
import com.example.modelfarm.network.models.DeviceTypeEnum;
import com.example.modelfarm.network.models.PageResponse;
import com.example.modelfarm.network.services.DeviceApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备列表页面
 * 显示和管理所有设备
 */
public class DeviceListActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTitle;
    private RecyclerView rvDeviceList;
    private TextView tvEmptyState;

    private DeviceAdapter adapter;
    private List<Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_list);

        initViews();
        setupRecyclerView();
        updateEmptyState();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        rvDeviceList = findViewById(R.id.rvDeviceList);
        tvEmptyState = findViewById(R.id.tvEmptyState);
    }

    private void setupRecyclerView() {
        deviceList = new ArrayList<>();
        adapter = new DeviceAdapter(deviceList, new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onDeviceClick(Device device) {
                if (device.getType() == DeviceTypeEnum.CAMERA) {
                    handleCameraDeviceClick(device);
                } else {
                    openDeviceDetail(device);
                }
            }

            @Override
            public void onDeviceDelete(Device device) {
                // 在设备列表页面不提供删除功能
            }
        });
        rvDeviceList.setLayoutManager(new LinearLayoutManager(this));
        rvDeviceList.setAdapter(adapter);
        loadDevices();
    }

    private void handleCameraDeviceClick(Device device) {
        String pushName = device.getPushName();
        if (TextUtils.isEmpty(pushName)) {
            Toast.makeText(this, "该摄像头缺少推流名称，无法打开直播", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(DeviceListActivity.this, LiveStreamActivity.class);
        intent.putExtra("device_id", device.getId());
        intent.putExtra("device_name", device.getName());
        intent.putExtra("push_name", pushName);
        if (!TextUtils.isEmpty(device.getUrl())) {
            intent.putExtra("stream_url", device.getUrl());
        }
        startActivity(intent);
    }

    private void openDeviceDetail(Device device) {
        Intent intent = new Intent(DeviceListActivity.this, DeviceDetailActivity.class);
        intent.putExtra("device_id", device.getId());
        intent.putExtra("device_name", device.getName());
        intent.putExtra("device_type", device.getTypeText());
        intent.putExtra("device_status", device.getStatusText());
        startActivity(intent);
    }

    private void loadDevices() {
        DeviceApiService api = RetrofitClient.create(this, DeviceApiService.class);
        api.getDeviceList(1, 100, null, null, null, null).enqueue(new Callback<ApiResponse<PageResponse<Device>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<Device>>> call, Response<ApiResponse<PageResponse<Device>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200 && response.body().getData() != null) {
                    deviceList.clear();
                    deviceList.addAll(response.body().getData().getRecords());
                    adapter.notifyDataSetChanged();
                }
                updateEmptyState();
            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<Device>>> call, Throwable t) {
                updateEmptyState();
            }
        });
    }

    private void updateEmptyState() {
        if (adapter.getItemCount() == 0) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvDeviceList.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvDeviceList.setVisibility(View.VISIBLE);
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
