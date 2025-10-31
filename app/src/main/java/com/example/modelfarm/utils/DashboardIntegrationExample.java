package com.example.modelfarm.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.modelfarm.network.AuthManager;
import com.example.modelfarm.network.models.EnterpriseStats;
import com.example.modelfarm.network.repositories.DashboardRepository;

/**
 * 仪表板集成示例
 * 演示如何将真实API数据集成到仪表板中
 */
public class DashboardIntegrationExample {
    
    private Context context;
    private DashboardRepository dashboardRepository;
    private AuthManager authManager;
    
    public DashboardIntegrationExample(Context context) {
        this.context = context;
        this.dashboardRepository = new DashboardRepository(context);
        this.authManager = AuthManager.Companion.getInstance(context);
    }
    
    /**
     * 更新仪表板统计数据
     * 这是一个示例方法，展示如何从API获取真实数据并更新UI
     */
    public void updateDashboardStats(
            TextView tvFarmCount,
            TextView tvDeviceCount,
            TextView tvAlertCount,
            TextView tvOnlineDeviceCount,
            TextView tvFaultDeviceCount
    ) {
        // 检查用户是否已登录
        if (!authManager.isLoggedIn()) {
            Toast.makeText(context, "用户未登录", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // 调用API获取企业统计数据
        dashboardRepository.getEnterpriseStats(new DashboardRepository.EnterpriseStatsCallback() {
            @Override
            public void onSuccess(EnterpriseStats stats) {
                // 在主线程中更新UI
                if (context instanceof android.app.Activity) {
                    ((android.app.Activity) context).runOnUiThread(() -> {
                        // 更新农场数量
                        if (tvFarmCount != null) {
                            tvFarmCount.setText(String.valueOf(stats.getFarmCount()));
                        }
                        
                        // 更新设备总数
                        if (tvDeviceCount != null) {
                            tvDeviceCount.setText(String.valueOf(stats.getDeviceCount()));
                        }
                        
                        // 更新待处理订单数量
                        if (tvAlertCount != null) {
                            tvAlertCount.setText(String.valueOf(stats.getPendingOrderCount()));
                        }
                        
                        // 更新在线设备数量
                        if (tvOnlineDeviceCount != null) {
                            tvOnlineDeviceCount.setText(String.valueOf(stats.getOnlineDeviceCount()));
                        }
                        
                        // 更新故障设备数量
                        if (tvFaultDeviceCount != null) {
                            tvFaultDeviceCount.setText(String.valueOf(stats.getFaultDeviceCount()));
                        }
                        
                        Toast.makeText(context, "数据更新成功", Toast.LENGTH_SHORT).show();
                    });
                }
            }
            
            @Override
            public void onError(String errorMessage) {
                // 在主线程中显示错误信息
                if (context instanceof android.app.Activity) {
                    ((android.app.Activity) context).runOnUiThread(() -> {
                        Toast.makeText(context, "获取数据失败: " + errorMessage, Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
    
    /**
     * 检查用户登录状态
     */
    public boolean isUserLoggedIn() {
        return authManager.isLoggedIn();
    }
    
    /**
     * 获取当前用户ID
     */
    public int getCurrentUserId() {
        return authManager.getUserId();
    }
    
    /**
     * 获取当前企业ID
     */
    public int getCurrentEnterpriseId() {
        return authManager.getEnterpriseId();
    }
}
