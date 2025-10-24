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
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.FarmSite;
import com.example.modelfarm.network.services.FarmApiService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private int farmId = -1;

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
        Intent intent = getIntent();
        String farmName = intent.getStringExtra("farm_name");
        String farmLocation = intent.getStringExtra("farm_location");
        farmId = intent.getIntExtra("farm_id", -1);
        if (farmName != null) tvFarmName.setText(farmName);
        if (farmLocation != null) tvFarmLocation.setText(farmLocation);
        tvFarmArea.setText("-");
    }

    private void loadPointData() {
        if (farmId <= 0) {
            pointList.clear();
            pointAdapter.notifyDataSetChanged();
            tvPointCount.setText("0");
            tvDeviceCount.setText("0");
            return;
        }
        FarmApiService api = RetrofitClient.create(this, FarmApiService.class);
        api.getFarmSites(farmId).enqueue(new Callback<ApiResponse<java.util.List<FarmSite>>>() {
            @Override
            public void onResponse(Call<ApiResponse<java.util.List<FarmSite>>> call, Response<ApiResponse<java.util.List<FarmSite>>> response) {
                pointList.clear();
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200 && response.body().getData()!=null) {
                    for (FarmSite s: response.body().getData()) {
                        String area = s.getProperties()!=null && s.getProperties().get("capacity")!=null ? String.valueOf(s.getProperties().get("capacity")) : "-";
                        String crop = s.getProperties()!=null && s.getProperties().get("type")!=null ? String.valueOf(s.getProperties().get("type")) : "-";
                        pointList.add(new FarmPoint(s.getName(), area, crop, s.getCreatedAt()));
                    }
                }
                pointAdapter.notifyDataSetChanged();
                tvPointCount.setText(String.valueOf(pointList.size()));
                tvDeviceCount.setText("-");
            }
            @Override
            public void onFailure(Call<ApiResponse<java.util.List<FarmSite>>> call, Throwable t) {
                pointList.clear();
                pointAdapter.notifyDataSetChanged();
                tvPointCount.setText("0");
                tvDeviceCount.setText("0");
            }
        });
    }

    // 农场点数据模型
    public static class FarmPoint {
        private final String name;
        private final String area;
        private final String crop;
        private final String buildTime;

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