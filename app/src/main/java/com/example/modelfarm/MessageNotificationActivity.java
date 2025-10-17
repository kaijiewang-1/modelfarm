package com.example.modelfarm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message_notification);

        initViews();
        setupRecyclerView();
        loadNotificationData();
        setupClickListeners();
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
        // 模拟加载通知数据
        notificationList.clear();
        
        notificationList.add(new Notification(
            "北方一号农场",
            "设备离线，请及时处理",
            "2小时前",
            "high",
            false
        ));
        
        notificationList.add(new Notification(
            "南方二号农场",
            "温度异常，请检查温控设备",
            "4小时前",
            "medium",
            false
        ));
        
        notificationList.add(new Notification(
            "系统通知",
            "系统维护完成，所有功能已恢复正常",
            "1天前",
            "low",
            true
        ));

        adapter.notifyDataSetChanged();
        updateEmptyState();
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
