package com.example.modelfarm;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;
import com.example.modelfarm.utils.SimpleApiHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息通知页面
 * 显示系统通知和预警信息
 */
public class MessageNotificationActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTitle;
    private RecyclerView rvNotifications;
    private Button btnMarkAllRead;
    private TextView tvEmptyState;

    private List<Notification> notificationList = new ArrayList<>();
    private NotificationAdapter adapter;
    
    // API相关
    private SimpleApiHelper simpleApiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message_notification);

        initViews();
        initApiComponents();
        setupRecyclerView();
        loadNotificationData();
        setupClickListeners();
    }

    /**
     * 初始化API组件
     */
    private void initApiComponents() {
        simpleApiHelper = new SimpleApiHelper(this);
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        rvNotifications = findViewById(R.id.rvNotifications);
        btnMarkAllRead = findViewById(R.id.btnMarkAllRead);
        tvEmptyState = findViewById(R.id.tvEmptyState);
    }

    private void setupRecyclerView() {
        adapter = new NotificationAdapter(notificationList);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(adapter);
    }

    private void loadNotificationData() {
        // 从API获取通知数据
        simpleApiHelper.getAllNotifications(new SimpleApiHelper.NotificationListCallback() {
            @Override
            public void onSuccess(List<SimpleApiHelper.NotificationData> apiNotifications) {
                runOnUiThread(() -> {
                    // 清空现有数据
                    notificationList.clear();
                    
                    // 转换API数据为本地通知模型
                    for (SimpleApiHelper.NotificationData apiNotification : apiNotifications) {
                        Notification localNotification = convertApiNotificationToLocal(apiNotification);
                        notificationList.add(localNotification);
                    }
                    
                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(MessageNotificationActivity.this, "获取通知数据失败: " + errorMessage, Toast.LENGTH_LONG).show();
                    // 显示空状态
                    notificationList.clear();
                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                });
            }
        });
    }
    
    /**
     * 将API通知数据转换为本地通知模型
     */
    private Notification convertApiNotificationToLocal(SimpleApiHelper.NotificationData apiNotification) {
        // 使用通知模型的内置方法
        String priority = getNotificationPriority(apiNotification.type);
        String timeAgo = getTimeAgo(apiNotification.createdAt);
        boolean isRead = apiNotification.isRead == 1;
        
        return new Notification(
            apiNotification.title,
            apiNotification.content,
            timeAgo,
            priority,
            isRead
        );
    }
    
    private String getNotificationPriority(int type) {
        switch (type) {
            case 1: return "系统通知";
            case 2: return "警告通知";
            case 3: return "消息通知";
            default: return "未知";
        }
    }
    
    private String getTimeAgo(String createdAt) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date createdDate = sdf.parse(createdAt);
            long diffInMillis = System.currentTimeMillis() - createdDate.getTime();
            long diffInMinutes = diffInMillis / (1000 * 60);
            
            if (diffInMinutes < 1) {
                return "刚刚";
            } else if (diffInMinutes < 60) {
                return diffInMinutes + "分钟前";
            } else if (diffInMinutes < 1440) {
                return (diffInMinutes / 60) + "小时前";
            } else {
                return (diffInMinutes / 1440) + "天前";
            }
        } catch (Exception e) {
            return "未知时间";
        }
    }

    private void updateEmptyState() {
        if (notificationList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvNotifications.setVisibility(View.GONE);
            btnMarkAllRead.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvNotifications.setVisibility(View.VISIBLE);
            btnMarkAllRead.setVisibility(View.VISIBLE);
        }
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMarkAllRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAllAsRead();
            }
        });
    }

    private void markAllAsRead() {
        for (Notification notification : notificationList) {
            notification.setRead(true);
        }
        adapter.notifyDataSetChanged();
        btnMarkAllRead.setVisibility(View.GONE);
    }

    /**
     * 通知数据模型
     */
    public static class Notification {
        private final String farmName;
        private final String message;
        private final String time;
        private final String level;
        private boolean isRead;

        public Notification(String farmName, String message, String time, String level, boolean isRead) {
            this.farmName = farmName;
            this.message = message;
            this.time = time;
            this.level = level;
            this.isRead = isRead;
        }

        public String getFarmName() { return farmName; }
        public String getMessage() { return message; }
        public String getTime() { return time; }
        public String getLevel() { return level; }
        public boolean isRead() { return isRead; }
        public void setRead(boolean read) { isRead = read; }
    }
}
