package com.example.modelfarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.R;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.SaToken;
import com.example.modelfarm.network.services.AdminApiService;
import com.example.modelfarm.network.services.EnterpriseApiService;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.EnterpriseStats;

import company.company_info;
import personal.profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import farm.farm_list;
import logins.login;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

/**
 * 智慧养殖系统主控制台
 * 提供养殖场管理、环境监控、设备管理、数据分析等功能入口
 */
public class DashboardActivity extends AppCompatActivity {

    // 功能模块卡片
    private MaterialCardView cardDeviceManagement;
    private MaterialCardView cardFarmManagement;
    private MaterialCardView cardCompanyInfo;
    private MaterialCardView cardProfile;
    private MaterialCardView cardOrderManagement;
    private Button btnEnvMonitor;
    private MaterialCardView cardAdmin;
    // 实时数据显示
    private TextView tvTemperature;
    private TextView tvHumidity;
    private TextView tvWelcome;
    private TextView tvFarmCount;
    private TextView tvDeviceCount;
    private TextView tvAlertCount;

    // 数据更新处理器
    private Handler dataUpdateHandler = new Handler();
    private Runnable dataUpdateRunnable;

    // API相关
    private EnterpriseApiService enterpriseApi;
    private AdminApiService adminApiService;
    // 动画和交互
    private Animation cardClickAnimation;
    private Animation fadeInAnimation;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "SmartFarmPrefs";
    private static final String KEY_FIRST_VISIT = "first_visit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        initViews();
        initApiComponents();
        initAnimations();
        initPreferences();
        initBottomNavigation();
        setupClickListeners();
        updateRealTimeData();
        startDataUpdate();
        checkFirstVisit();
    }


    /**
     * 初始化API组件
     */
    private void initApiComponents() {
        enterpriseApi = RetrofitClient.create(this, EnterpriseApiService.class);
        adminApiService = RetrofitClient.create(this, AdminApiService.class);
    }

    private void initViews() {
        // 功能模块卡片
        cardDeviceManagement = findViewById(R.id.card_device_management);
        cardFarmManagement = findViewById(R.id.card_farm_management);
        cardCompanyInfo = findViewById(R.id.card_company_info);
        cardProfile = findViewById(R.id.card_profile);
        cardOrderManagement = findViewById(R.id.card_order_management);
        cardAdmin = findViewById(R.id.card_admin);
        btnEnvMonitor = findViewById(R.id.btn_env_detail);
        // 实时数据显示
        tvTemperature = findViewById(R.id.tv_temperature);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvWelcome = findViewById(R.id.tv_welcome);
        tvFarmCount = findViewById(R.id.tv_farm_count);
        tvDeviceCount = findViewById(R.id.tv_device_count);
        tvAlertCount = findViewById(R.id.tv_alert_count);
    }

    private void initAnimations() {
        // 初始化动画
        cardClickAnimation = AnimationUtils.loadAnimation(this, R.anim.card_click);
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
    }

    private void initPreferences() {
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (bottomNavigationView == null) {
            return;
        }

        bottomNavigationView.setSelectedItemId(R.id.menu_dashboard);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_dashboard) {
                return true;
            } else if (itemId == R.id.menu_devices) {
                startActivity(new Intent(DashboardActivity.this, DeviceManagementActivity.class));
            } else if (itemId == R.id.menu_farms) {
                startActivity(new Intent(DashboardActivity.this, farm_list.class));
            } else if (itemId == R.id.menu_orders) {
                startActivity(new Intent(DashboardActivity.this, OrderListActivity.class));
            } else if (itemId == R.id.menu_company) {
                startActivity(new Intent(DashboardActivity.this, company_info.class));
            } else {
                return false;
            }
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        });
    }

    private void checkFirstVisit() {
        boolean isFirstVisit = preferences.getBoolean(KEY_FIRST_VISIT, true);
        if (isFirstVisit) {
            showWelcomeGuide();
            preferences.edit().putBoolean(KEY_FIRST_VISIT, false).apply();
        }
    }

    private void showWelcomeGuide() {
        Toast.makeText(this, "欢迎使用智慧养殖系统！点击各功能模块开始使用", Toast.LENGTH_LONG).show();
    }

    private void setupClickListeners() {
        //进入环境监控页
        btnEnvMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(cardClickAnimation);
                Intent intent = new Intent(DashboardActivity.this, EnvironmentMonitoringEnhancedActivity.class);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // 设备管理 - 智能设备监控
        cardDeviceManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(cardClickAnimation);
                Intent intent = new Intent(DashboardActivity.this, DeviceManagementActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // 农场管理 - 养殖场管理
        cardFarmManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(cardClickAnimation);
                Intent intent = new Intent(DashboardActivity.this, farm_list.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // 企业信息 - 养殖企业信息
        cardCompanyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(cardClickAnimation);
                Intent intent = new Intent(DashboardActivity.this, company.company_info.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // 个人资料 - 用户信息管理
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(cardClickAnimation);
                Intent intent = new Intent(DashboardActivity.this, personal.profile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // 工单管理 - 列表页
        cardOrderManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(cardClickAnimation);
                Intent intent = new Intent(DashboardActivity.this, OrderListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //进入管理员页面
        cardAdmin.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             checkAdmin(v);
                                         }
                                     }
        );
    }

    private void checkAdmin(View v) {
        adminApiService.check().enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.body().getCode() == 200) {
                    v.startAnimation(cardClickAnimation);
                    Intent intent = new Intent(DashboardActivity.this, AdminActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else{
                    Toast.makeText(DashboardActivity.this, "无企业权限", 1).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable throwable) {
                Toast.makeText(DashboardActivity.this, "无企业权限", 1).show();
            }
        });

    }

    /**
     * 更新实时数据
     */
    private void updateRealTimeData() {
        tvTemperature.setText("22°C");
        tvHumidity.setText("68%");
        tvWelcome.setText("欢迎使用智慧养殖系统");
        enterpriseApi.getEnterpriseStats().enqueue(new Callback<ApiResponse<EnterpriseStats>>() {
            @Override
            public void onResponse(Call<ApiResponse<EnterpriseStats>> call, Response<ApiResponse<EnterpriseStats>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    EnterpriseStats stats = response.body().getData();
                    tvFarmCount.setText(stats.getFarmCount() + "个养殖场");
                    tvDeviceCount.setText(stats.getDeviceCount() + "台设备");
                    tvAlertCount.setText(stats.getFaultDeviceCount() + "个预警");
                } else {
                    setDefaultStats();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<EnterpriseStats>> call, Throwable t) {
                setDefaultStats();
            }

            private void setDefaultStats() {
                tvFarmCount.setText("3个养殖场");
                tvDeviceCount.setText("15台设备");
                tvAlertCount.setText("2个预警");
            }
        });
    }

    /**
     * 启动数据更新
     */
    private void startDataUpdate() {
        dataUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateRealTimeData();
                // 每30秒更新一次数据
                dataUpdateHandler.postDelayed(this, 30000);
            }
        };
        dataUpdateHandler.post(dataUpdateRunnable);
    }

    /**
     * 显示即将推出功能提示
     */
    private void showComingSoon(String featureName) {
        Toast.makeText(this, featureName + "即将推出，敬请期待！", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止数据更新
        if (dataUpdateHandler != null && dataUpdateRunnable != null) {
            dataUpdateHandler.removeCallbacks(dataUpdateRunnable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 页面恢复时更新数据
        updateRealTimeData();
    }
}
