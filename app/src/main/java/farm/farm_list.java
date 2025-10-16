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

import java.util.ArrayList;
import java.util.List;

public class farm_list extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MaterialButton btnAddFarm;
    private RecyclerView rvFarms;
    private FarmAdapter farmAdapter;
    private List<Farm> farmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farm_list);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadFarmData();
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
        // 模拟农场数据
        farmList.clear();
        farmList.add(new Farm("北方一号农场", "北京市朝阳区", "500亩", "主要种植蔬菜", "智能温室", "正常", "25°C", "65%"));
        farmList.add(new Farm("南方二号农场", "上海市浦东区", "300亩", "主要种植水果", "露天种植", "正常", "28°C", "70%"));
        farmList.add(new Farm("西部三号农场", "成都市双流区", "800亩", "主要种植粮食", "大田种植", "正常", "22°C", "60%"));
        
        farmAdapter.notifyDataSetChanged();
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