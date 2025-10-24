package com.example.modelfarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.R;
import com.example.modelfarm.utils.SimpleApiHelper;
import com.google.android.material.card.MaterialCardView;

import farm.farm_list;
import logins.login;

/**
 * 智慧养殖系统主控制台
 * 提供养殖场管理、环境监控、设备管理、数据分析等功能入口
 */
public class DashboardActivity extends AppCompatActivity {

    // 功能模块卡片
    private MaterialCardView cardDeviceManagement;
    private MaterialCardView cardEnvironmentMonitoring;
    private MaterialCardView cardFarmManagement;
    private MaterialCardView cardDataAnalysis;
    private MaterialCardView cardCompanyInfo;
    private MaterialCardView cardProfile;
    private MaterialCardView cardLivestockManagement;
    private MaterialCardView cardFeedManagement;
    private MaterialCardView cardHealthMonitoring;
    private MaterialCardView cardProductionTracking;

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
    private SimpleApiHelper simpleApiHelper;
    
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
        setupClickListeners();
        updateRealTimeData();
        startDataUpdate();
        checkFirstVisit();
    }


    /**
     * 初始化API组件
     */
    private void initApiComponents() {
        simpleApiHelper = new SimpleApiHelper(this);
    }

    private void initViews() {
        // 功能模块卡片
        cardDeviceManagement = findViewById(R.id.card_device_management);
        cardEnvironmentMonitoring = findViewById(R.id.card_environment_monitoring);
        cardFarmManagement = findViewById(R.id.card_farm_management);
        cardDataAnalysis = findViewById(R.id.card_data_analysis);
        cardCompanyInfo = findViewById(R.id.card_company_info);
        cardProfile = findViewById(R.id.card_profile);
        cardLivestockManagement = findViewById(R.id.card_livestock_management);
        cardFeedManagement = findViewById(R.id.card_feed_management);
        cardHealthMonitoring = findViewById(R.id.card_health_monitoring);
        cardProductionTracking = findViewById(R.id.card_production_tracking);

        // 实时数据显示
        tvTemperature = findViewById(R.id.tv_temperature);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvWelcome = findViewById(R.id.tv_welcome);
        tvFarmCount = findViewById(R.id.tv_farm_count);
        tvDeviceCount = findViewById(R.id.tv_device_count);
        tvAlertCount = findViewById(R.id.tv_alert_count);
        
        // 消息通知按钮
        findViewById(R.id.btnMessageNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MessageNotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void initAnimations() {
        // 初始化动画
        cardClickAnimation = AnimationUtils.loadAnimation(this, R.anim.card_click);
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
    }

    private void initPreferences() {
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
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

        // 环境监控 - 养殖环境实时监控
        cardEnvironmentMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(cardClickAnimation);
                Intent intent = new Intent(DashboardActivity.this, EnvironmentMonitoringActivity.class);
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

        // 数据分析 - 养殖数据分析
        cardDataAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(cardClickAnimation);
                Intent intent = new Intent(DashboardActivity.this, DataAnalysisActivity.class);
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

        // 牲畜管理 - 牲畜信息管理
        cardLivestockManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComingSoon("牲畜管理功能");
            }
        });

        // 饲料管理 - 饲料投喂管理
        cardFeedManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComingSoon("饲料管理功能");
            }
        });

        // 健康监控 - 牲畜健康监测
        cardHealthMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComingSoon("健康监控功能");
            }
        });

        // 生产跟踪 - 生产数据跟踪
        cardProductionTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComingSoon("生产跟踪功能");
            }
        });
    }

    /**
     * 更新实时数据
     */
    private void updateRealTimeData() {
        // 模拟养殖场环境数据
        tvTemperature.setText("22°C");
        tvHumidity.setText("68%");
        tvWelcome.setText("欢迎使用智慧养殖系统");
        
        // 从API获取企业统计数据
        if (simpleApiHelper != null) {
            simpleApiHelper.getEnterpriseStats(new SimpleApiHelper.EnterpriseStatsCallback() {
                @Override
                public void onSuccess(SimpleApiHelper.EnterpriseStats stats) {
                    runOnUiThread(() -> {
                        tvFarmCount.setText(stats.farmCount + "个养殖场");
                        tvDeviceCount.setText(stats.deviceCount + "台设备");
                        tvAlertCount.setText(stats.faultDeviceCount + "个预警");
                    });
                }
                
                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() -> {
                        // 使用默认数据
                        tvFarmCount.setText("3个养殖场");
                        tvDeviceCount.setText("15台设备");
                        tvAlertCount.setText("2个预警");
                    });
                }
            });
        } else {
            // 使用默认数据
            tvFarmCount.setText("3个养殖场");
            tvDeviceCount.setText("15台设备");
            tvAlertCount.setText("2个预警");
        }
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
