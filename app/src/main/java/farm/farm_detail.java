package farm;

import android.content.Intent;
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

public class farm_detail extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvFarmName;
    private TextView tvFarmLocation;
    private TextView tvFarmArea;
    private TextView tvPointCount;
    private TextView tvDeviceCount;
    private MaterialButton btnAddPoint;
    private RecyclerView rvPoints;
    private FarmPointAdapter pointAdapter;
    private List<FarmPoint> pointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_farm_detail);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadFarmData();
        loadPointData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvFarmName = findViewById(R.id.tv_farm_name);
        tvFarmLocation = findViewById(R.id.tv_farm_location);
        tvFarmArea = findViewById(R.id.tv_farm_area);
        tvPointCount = findViewById(R.id.tv_point_count);
        tvDeviceCount = findViewById(R.id.tv_device_count);
        btnAddPoint = findViewById(R.id.btn_add_point);
        rvPoints = findViewById(R.id.rv_points);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回农场列表
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        pointList = new ArrayList<>();
        pointAdapter = new FarmPointAdapter(pointList, new FarmPointAdapter.OnPointClickListener() {
            @Override
            public void onPointClick(FarmPoint point) {
                // 点击农场点，跳转到农场点详情
                Intent intent = new Intent(farm_detail.this, farm_point_detail.class);
                intent.putExtra("point_name", point.getName());
                intent.putExtra("point_area", point.getArea());
                intent.putExtra("point_crop", point.getCrop());
                startActivity(intent);
            }
        });
        rvPoints.setLayoutManager(new LinearLayoutManager(this));
        rvPoints.setAdapter(pointAdapter);
    }

    private void loadFarmData() {
        // 从Intent获取农场信息
        Intent intent = getIntent();
        String farmName = intent.getStringExtra("farm_name");
        String farmLocation = intent.getStringExtra("farm_location");
        
        if (farmName != null) {
            tvFarmName.setText(farmName);
        }
        if (farmLocation != null) {
            tvFarmLocation.setText(farmLocation);
        }
        
        // 设置其他信息
        tvFarmArea.setText("500亩");
        tvPointCount.setText("3");
        tvDeviceCount.setText("12");
    }

    private void loadPointData() {
        // 模拟农场点数据
        pointList.clear();
        pointList.add(new FarmPoint("A区大棚", "200平方米", "番茄", "2023年3月"));
        pointList.add(new FarmPoint("B区大棚", "150平方米", "黄瓜", "2023年4月"));
        pointList.add(new FarmPoint("C区大棚", "180平方米", "茄子", "2023年5月"));
        
        pointAdapter.notifyDataSetChanged();
    }

    // 农场点数据模型
    public static class FarmPoint {
        private String name;
        private String area;
        private String crop;
        private String buildTime;

        public FarmPoint(String name, String area, String crop, String buildTime) {
            this.name = name;
            this.area = area;
            this.crop = crop;
            this.buildTime = buildTime;
        }

        public String getName() { return name; }
        public String getArea() { return area; }
        public String getCrop() { return crop; }
        public String getBuildTime() { return buildTime; }
    }
}