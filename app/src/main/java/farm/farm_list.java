package farm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.DashboardActivity;
import com.example.modelfarm.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.example.modelfarm.utils.SimpleApiHelper;
import com.example.modelfarm.utils.FarmDataDisplay;

import java.util.ArrayList;
import java.util.List;

public class farm_list extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MaterialButton btnAddFarm;
    private RecyclerView rvFarms;
    private FarmAdapter farmAdapter;
    private List<Farm> farmList;
    
    // API相关
    private SimpleApiHelper simpleApiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farm_list);

        initViews();
        initApiComponents();
        setupToolbar();
        setupRecyclerView();
        loadFarmData();
    }

    /**
     * 初始化API组件
     */
    private void initApiComponents() {
        simpleApiHelper = new SimpleApiHelper(this);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        btnAddFarm = findViewById(R.id.btn_add_farm);
        rvFarms = findViewById(R.id.rv_farms);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回主控制台
                Intent intent = new Intent(farm_list.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        farmList = new ArrayList<>();
        farmAdapter = new FarmAdapter(farmList, new FarmAdapter.OnFarmClickListener() {
            @Override
            public void onFarmClick(Farm farm) {
                // 点击农场，跳转到农场详情
                Intent intent = new Intent(farm_list.this, farm_detail.class);
                intent.putExtra("farm_name", farm.getName());
                intent.putExtra("farm_location", farm.getLocation());
                startActivity(intent);
            }
        });
        rvFarms.setLayoutManager(new LinearLayoutManager(this));
        rvFarms.setAdapter(farmAdapter);
    }

    private void loadFarmData() {
        // 从API获取农场数据
        simpleApiHelper.getEnterpriseFarms(new SimpleApiHelper.FarmListCallback() {
            @Override
            public void onSuccess(List<SimpleApiHelper.FarmData> farms) {
                runOnUiThread(() -> {
                    // 清空现有数据
                    farmList.clear();
                    
                    // 转换API数据为本地农场模型
                    for (SimpleApiHelper.FarmData apiFarm : farms) {
                        Farm localFarm = convertApiFarmToLocal(apiFarm);
                        farmList.add(localFarm);
                    }
                    
                    farmAdapter.notifyDataSetChanged();
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(farm_list.this, "获取农场数据失败: " + errorMessage, Toast.LENGTH_LONG).show();
                    // 显示空状态
                    farmList.clear();
                    farmAdapter.notifyDataSetChanged();
                });
            }
        });
    }
    
    /**
     * 将API农场数据转换为本地农场模型
     */
    private Farm convertApiFarmToLocal(SimpleApiHelper.FarmData apiFarm) {
        // 使用农场数据展示器获取显示信息
        String area = String.format("%.1f亩", apiFarm.area);
        String type = getFarmTypeDisplay(apiFarm.type);
        String status = getFarmStatusDisplay(apiFarm.status);
        
        return new Farm(
            apiFarm.name,
            apiFarm.address,
            area,
            "智慧养殖", // 描述信息
            type,
            status,
            "25°C", // 温度信息（可以从设备数据获取）
            "65%" // 湿度信息（可以从设备数据获取）
        );
    }
    
    private String getFarmTypeDisplay(int type) {
        switch (type) {
            case 1: return "养殖场";
            case 2: return "种植园";
            case 3: return "混合农场";
            default: return "未知";
        }
    }
    
    private String getFarmStatusDisplay(int status) {
        switch (status) {
            case 1: return "正常运营";
            case 0: return "暂停运营";
            case -1: return "已关闭";
            default: return "未知状态";
        }
    }

    // 农场数据模型
    public static class Farm {
        private final String name;
        private final String location;
        private final String area;
        private final String description;
        private final String type;
        private final String status;
        private final String temperature;
        private final String humidity;

        public Farm(String name, String location, String area, String description, 
                   String type, String status, String temperature, String humidity) {
            this.name = name;
            this.location = location;
            this.area = area;
            this.description = description;
            this.type = type;
            this.status = status;
            this.temperature = temperature;
            this.humidity = humidity;
        }

        public String getName() { return name; }
        public String getLocation() { return location; }
        public String getArea() { return area; }
        public String getDescription() { return description; }
        public String getType() { return type; }
        public String getStatus() { return status; }
        public String getTemperature() { return temperature; }
        public String getHumidity() { return humidity; }
    }
}