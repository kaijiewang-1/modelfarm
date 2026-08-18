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
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import com.example.modelfarm.network.models.MonthInoutData;

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
    
    // 鸡只监测数据
    private TextView tvChickenStatus;
    private TextView tvChickenSteps;
    private TextView tvAirQuality;
    
    // 出入库统计图表
    private LineChart chartInout;
    private Spinner spinnerYear;
    private int currentYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farm_point_detail);


            initViews();
            setupToolbar();
            setupRecyclerView();
            setupYearSpinner();
            setupChart();
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
        
        // 鸡只监测数据
        tvChickenStatus = findViewById(R.id.tv_chicken_status);
        tvChickenSteps = findViewById(R.id.tv_chicken_steps);
        tvAirQuality = findViewById(R.id.tv_air_quality);
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputChickenDialog();
            }
        });
        
        // 出入库统计图表
        chartInout = findViewById(R.id.chart_inout);
        spinnerYear = findViewById(R.id.spinner_year);
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
                        tvCrop.setText("类型：" +(data.getProperties()==null?"鸡舍": data.getProperties().getOrDefault("capacity", "-")));
                        tvTime.setText("创建时间：" + data.getCreatedAt());
                        
                        // 加载鸡只监测数据（从properties中获取）
                        loadChickenMonitoringData();
                        
                        // 加载出入库统计数据（默认当前年份）
                        loadInoutData(currentYear);
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

    /**
     * 加载鸡只监测数据
     * 从养殖点的properties属性中获取鸡只状态、步数、空气质量等数据
     */
    private void loadChickenMonitoringData() {
        if (data == null || data.getProperties() == null || data.getProperties().isEmpty()) {
            // 没有properties数据，显示默认值
            if (tvChickenStatus != null) tvChickenStatus.setText("无数据");
            if (tvChickenSteps != null) tvChickenSteps.setText("无数据");
            if (tvAirQuality != null) tvAirQuality.setText("无数据");
            return;
        }

        // 从properties中获取数据
        Map<String, Object> properties = data.getProperties();
        updateChickenMonitoringUI(properties);
    }

    /**
     * 更新鸡只监测数据UI
     */
    private void updateChickenMonitoringUI(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            if (tvChickenStatus != null) tvChickenStatus.setText("无数据");
            if (tvChickenSteps != null) tvChickenSteps.setText("无数据");
            if (tvAirQuality != null) tvAirQuality.setText("无数据");
            return;
        }

        // 解析鸡只状态
        String chickenStatus = parseChickenStatus(data);
        if (tvChickenStatus != null) {
            tvChickenStatus.setText(chickenStatus);
        }

        // 解析平均步数
        String avgSteps = parseAverageSteps(data);
        if (tvChickenSteps != null) {
            tvChickenSteps.setText(avgSteps);
        }

        // 解析空气质量
        String airQuality = parseAirQuality(data);
        if (tvAirQuality != null) {
            tvAirQuality.setText(airQuality);
        }
    }

    /**
     * 解析鸡只状态
     */
    private String parseChickenStatus(Map<String, Object> data) {
        // 从properties中获取status字段
        Object status = data.get("status");
        if (status == null) status = data.get("chicken_status");
        if (status == null) status = data.get("chickenStatus");
        if (status == null) status = data.get("animal_status");
        
        if (status != null) {
            String statusStr = status.toString();
            // 如果是数字，转换为状态文本
            try {
                int statusInt = Integer.parseInt(statusStr);
                switch (statusInt) {
                    case 1: return "健康";
                    case 2: return "异常";
                    case 3: return "生病";
                    default: return statusStr;
                }
            } catch (NumberFormatException e) {
                // 直接返回状态文本（如"正常"、"良好"等）
                return statusStr;
            }
        }
        return "无数据";
    }

    /**
     * 解析平均步数
     */
    private String parseAverageSteps(Map<String, Object> data) {
        // 从properties中获取avaStep字段（根据图片中的格式）
        Object steps = data.get("avaStep");
        if (steps == null) steps = data.get("ava_step");
        if (steps == null) steps = data.get("avg_steps");
        if (steps == null) steps = data.get("average_steps");
        if (steps == null) steps = data.get("steps");
        if (steps == null) steps = data.get("chicken_steps");
        if (steps == null) steps = data.get("avgSteps");
        
        if (steps != null) {
            try {
                // 如果是数字，格式化显示
                double stepsValue = Double.parseDouble(steps.toString());
                if (stepsValue == (int) stepsValue) {
                    return String.valueOf((int) stepsValue) + " 步";
                } else {
                    return String.format("%.1f 步", stepsValue);
                }
            } catch (NumberFormatException e) {
                return steps.toString() + " 步";
            }
        }
        return "无数据";
    }

    /**
     * 解析空气质量
     */
    private String parseAirQuality(Map<String, Object> data) {
        // 从properties中获取airInfo字段（根据图片中的格式）
        Object airQuality = data.get("airInfo");
        if (airQuality == null) airQuality = data.get("air_info");
        if (airQuality == null) airQuality = data.get("air_quality");
        if (airQuality == null) airQuality = data.get("airQuality");
        if (airQuality == null) airQuality = data.get("air_quality_value");
        if (airQuality == null) airQuality = data.get("aqi");
        if (airQuality == null) airQuality = data.get("pm25");
        if (airQuality == null) airQuality = data.get("pm2.5");
        
        if (airQuality != null) {
            String qualityStr = airQuality.toString();
            // 如果是文本描述（如"良好"），直接返回
            if (qualityStr.matches("[\\u4e00-\\u9fa5]+")) {
                return qualityStr;
            }
            
            // 如果是数字，格式化显示并判断等级
            try {
                double qualityValue = Double.parseDouble(qualityStr);
                String qualityLevel = getAirQualityLevel(qualityValue);
                if (qualityValue == (int) qualityValue) {
                    return String.valueOf((int) qualityValue) + " (" + qualityLevel + ")";
                } else {
                    return String.format("%.1f (%s)", qualityValue, qualityLevel);
                }
            } catch (NumberFormatException e) {
                return qualityStr;
            }
        }
        return "无数据";
    }

    /**
     * 根据数值获取空气质量等级
     */
    private String getAirQualityLevel(double value) {
        // 假设是AQI标准（0-500）
        if (value <= 50) return "优";
        else if (value <= 100) return "良";
        else if (value <= 150) return "轻度污染";
        else if (value <= 200) return "中度污染";
        else if (value <= 300) return "重度污染";
        else return "严重污染";
    }
    
    /**
     * 设置年份选择器
     */
    private void setupYearSpinner() {
        // 生成最近5年的年份列表
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int currentYear = calendar.get(java.util.Calendar.YEAR);
        this.currentYear = currentYear;
        
        List<String> years = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            years.add(String.valueOf(currentYear - i));
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapter);
        
        // 设置年份选择监听
        spinnerYear.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                int selectedYear = Integer.parseInt(years.get(position));
                loadInoutData(selectedYear);
            }
            
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }
    
    /**
     * 设置图表样式
     */
    private void setupChart() {
        // 禁用描述
        chartInout.getDescription().setEnabled(false);
        
        // 启用触摸手势
        chartInout.setTouchEnabled(true);
        chartInout.setDragEnabled(true);
        chartInout.setScaleEnabled(true);
        chartInout.setPinchZoom(true);
        
        // 设置X轴
        XAxis xAxis = chartInout.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(12);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int month = (int) value;
                if (month >= 1 && month <= 12) {
                    return month + "月";
                }
                return "";
            }
        });
        
        // 设置Y轴
        YAxis leftAxis = chartInout.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        
        YAxis rightAxis = chartInout.getAxisRight();
        rightAxis.setEnabled(false);
        
        // 设置图例
        chartInout.getLegend().setEnabled(true);
        chartInout.getLegend().setTextSize(12f);
    }
    
    /**
     * 加载出入库统计数据
     */
    private void loadInoutData(int year) {
        if (farmSiteId <= 0) {
            return;
        }
        
        FarmSiteApiService api = RetrofitClient.create(this, FarmSiteApiService.class);
        api.getYearSiteData(year, farmSiteId).enqueue(new Callback<ApiResponse<List<MonthInoutData>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<MonthInoutData>>> call, 
                    Response<ApiResponse<List<MonthInoutData>>> response) {
                if (response.isSuccessful() && response.body() != null 
                        && response.body().getCode() == 200 
                        && response.body().getData() != null) {
                    List<MonthInoutData> dataList = response.body().getData();
                    updateChart(dataList);
                } else {
                    Toast.makeText(farm_point_detail.this, 
                        "加载数据失败: " + (response.body() != null ? response.body().getMessage() : "未知错误"), 
                        Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<MonthInoutData>>> call, Throwable t) {
                Toast.makeText(farm_point_detail.this, "网络请求失败: " + t.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * 更新图表数据
     */
    private void updateChart(List<MonthInoutData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        
        // 创建总数量数据集
        List<Entry> sumEntries = new ArrayList<>();
        List<Entry> inEntries = new ArrayList<>();
        List<Entry> outEntries = new ArrayList<>();
        
        // 按月份排序（确保数据按1-12月顺序）
        java.util.Collections.sort(dataList, (a, b) -> Integer.compare(a.getMonth(), b.getMonth()));
        
        for (MonthInoutData data : dataList) {
            int month = data.getMonth();
            sumEntries.add(new Entry(month, data.getSum()));
            inEntries.add(new Entry(month, data.getInCount()));
            outEntries.add(new Entry(month, data.getOutCount()));
        }
        
        // 创建数据集
        LineDataSet sumDataSet = new LineDataSet(sumEntries, "存栏量");
        sumDataSet.setColor(android.graphics.Color.parseColor("#4CAF50"));
        sumDataSet.setLineWidth(2f);
        sumDataSet.setCircleColor(android.graphics.Color.parseColor("#4CAF50"));
        sumDataSet.setCircleRadius(4f);
        sumDataSet.setValueTextSize(10f);
        sumDataSet.setDrawValues(false);
        
        LineDataSet inDataSet = new LineDataSet(inEntries, "入库数量");
        inDataSet.setColor(android.graphics.Color.parseColor("#2196F3"));
        inDataSet.setLineWidth(2f);
        inDataSet.setCircleColor(android.graphics.Color.parseColor("#2196F3"));
        inDataSet.setCircleRadius(4f);
        inDataSet.setValueTextSize(10f);
        inDataSet.setDrawValues(false);
        
        LineDataSet outDataSet = new LineDataSet(outEntries, "出库数量");
        outDataSet.setColor(android.graphics.Color.parseColor("#FF9800"));
        outDataSet.setLineWidth(2f);
        outDataSet.setCircleColor(android.graphics.Color.parseColor("#FF9800"));
        outDataSet.setCircleRadius(4f);
        outDataSet.setValueTextSize(10f);
        outDataSet.setDrawValues(false);
        
        // 添加到图表
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(sumDataSet);
        dataSets.add(inDataSet);
        dataSets.add(outDataSet);
        
        LineData lineData = new LineData(dataSets);
        chartInout.setData(lineData);
        chartInout.invalidate(); // 刷新图表
    }
}