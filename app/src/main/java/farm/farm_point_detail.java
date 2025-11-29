package farm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.FarmSite;
import com.example.modelfarm.network.models.UpdateFarmSiteRequest;
import com.example.modelfarm.network.services.FarmSiteApiService;
import com.example.modelfarm.network.services.FarmApiService;
import com.example.modelfarm.network.services.DeviceApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.modelfarm.network.models.Device;
import com.example.modelfarm.DeviceAdapter;
import com.example.modelfarm.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class farm_point_detail extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvPointName;
    private TextView tvArea;
    private TextView tvCrop;
    private TextView tvTime;
    private TextView tvTotalDevices;
    private TextView tvOnlineDevices;
    private TextView tvOfflineDevices;
    // private MaterialButton btnAddDevice; // 暂时注释掉未使用的变量
    private RecyclerView rvDevices;
    private DeviceAdapter deviceAdapter;
    private FarmSite data;
    private Button btnEditInfo;
    private List<Device> deviceList;
    private int farmSiteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farm_point_detail);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadPointData();
        loadDeviceData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvPointName = findViewById(R.id.tv_point_name);
        tvArea = findViewById(R.id.tv_area);
        tvCrop = findViewById(R.id.tv_crop);
        tvTime = findViewById(R.id.tv_time);
        tvTotalDevices = findViewById(R.id.tv_total_devices);
        tvOnlineDevices = findViewById(R.id.tv_online_devices);
        tvOfflineDevices = findViewById(R.id.tv_offline_devices);
        // btnAddDevice = findViewById(R.id.btn_add_device); // 已注释掉
        btnEditInfo = findViewById(R.id.tv_btn_update);
        rvDevices = findViewById(R.id.rv_devices);
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputChickenDialog();
            }
        });
    }

    private void showInputChickenDialog() {
        // 1. 创建 EditText
        final EditText inputEdit = new EditText(this);
        // 设置输入类型为数字
        inputEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputEdit.setHint("例如：50");

        // 2. 构建 Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入鸡只总数");
        builder.setView(inputEdit);

        // 3. 设置确认按钮
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String chickenCountStr = inputEdit.getText().toString();
                if (!chickenCountStr.isEmpty()) {
                    int count = Integer.parseInt(chickenCountStr);


                    FarmSiteApiService api = RetrofitClient.create(farm_point_detail.this, FarmSiteApiService.class);
                    api.updateFarmSite(new UpdateFarmSiteRequest(data.getFarmId(), data.getId(), data.getName(), count, data.getProperties())).enqueue(
                            new Callback<ApiResponse>() {

                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    Toast.makeText(farm_point_detail.this, "更新成功", Toast.LENGTH_SHORT).show();
                                    tvArea.setText("鸡只总数：" + count);
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                                    Toast.makeText(farm_point_detail.this, "更新失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );


                } else {
                    Toast.makeText(farm_point_detail.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 4. 设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel(); // 关闭对话框
            }
        });

        // 5. 显示
        builder.show();
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回农场详情
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        deviceList = new ArrayList<>();
        deviceAdapter = new DeviceAdapter(deviceList, new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onDeviceClick(Device device) {
                Toast.makeText(farm_point_detail.this, "点击了设备: " + device.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceDelete(Device device) {
                // 在养殖点详情页面不提供删除功能
            }
        });
        rvDevices.setLayoutManager(new LinearLayoutManager(this));
        rvDevices.setAdapter(deviceAdapter);
    }

    private void loadPointData() {
        Intent intent = getIntent();
        String pointName = intent.getStringExtra("point_name");
        String pointArea = intent.getStringExtra("point_area");
        String pointCrop = intent.getStringExtra("point_crop");
        farmSiteId = intent.getIntExtra("farm_site_id", -1);
        if (farmSiteId > 0) {
            FarmSiteApiService api = RetrofitClient.create(this, FarmSiteApiService.class);
            api.getFarmSite(farmSiteId).enqueue(new Callback<ApiResponse<com.example.modelfarm.network.models.FarmSite>>() {
                @Override
                public void onResponse(Call<ApiResponse<com.example.modelfarm.network.models.FarmSite>> call, Response<ApiResponse<com.example.modelfarm.network.models.FarmSite>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200 && response.body().getData() != null) {
                        data = response.body().getData();
                        tvPointName.setText(data.getName());

                        tvArea.setText("鸡只总数：" + data.getSum());
                        tvCrop.setText("类型：" + data.getProperties().getOrDefault("capacity", "-"));
                        tvTime.setText("创建时间：" + data.getCreatedAt());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<com.example.modelfarm.network.models.FarmSite>> call, Throwable t) {
                }
            });
        } else {
            if (pointName != null) tvPointName.setText(pointName);
            if (pointArea != null) tvArea.setText("面积：" + pointArea);
            if (pointCrop != null) tvCrop.setText("种植作物：" + pointCrop);
            tvTime.setText("建成时间：-");
        }
    }

    private void loadDeviceData() {
        deviceList.clear();
        if (farmSiteId <= 0) {
            deviceAdapter.notifyDataSetChanged();
        } else {
            FarmSiteApiService api = RetrofitClient.create(this, FarmSiteApiService.class);
            api.getFarmSiteDevices(farmSiteId).enqueue(new Callback<ApiResponse<java.util.List<Device>>>() {
                @Override
                public void onResponse(Call<ApiResponse<java.util.List<Device>>> call, Response<ApiResponse<java.util.List<Device>>> response) {
                    deviceList.clear();
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200 && response.body().getData() != null) {
                        deviceList.addAll(response.body().getData());
                    }
                    deviceAdapter.notifyDataSetChanged();
                    updateStats();
                }

                @Override
                public void onFailure(Call<ApiResponse<java.util.List<Device>>> call, Throwable t) {
                    deviceAdapter.notifyDataSetChanged();
                    updateStats();
                }
            });
        }
    }

    private void updateStats() {
        int total = deviceList.size();
        int online = 0;
        int offline = 0;
        for (Device device : deviceList) {
            if ("在线".equals(device.getStatusText())) online++;
            else offline++;
        }
        tvTotalDevices.setText(String.valueOf(total));
        tvOnlineDevices.setText(String.valueOf(online));
        tvOfflineDevices.setText(String.valueOf(offline));
    }
}