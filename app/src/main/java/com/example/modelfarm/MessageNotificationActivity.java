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
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.services.NotificationApiService;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.Notification;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private NotificationApiService notificationApi;

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
        notificationApi = RetrofitClient.create(this, NotificationApiService.class);
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
        notificationApi.getAllNotifications().enqueue(new Callback<ApiResponse<List<Notification>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Notification>>> call, Response<ApiResponse<List<Notification>>> response) {
                runOnUiThread(() -> {
                    notificationList.clear();
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        notificationList.addAll(response.body().getData());
                    } else {
                        Toast.makeText(MessageNotificationActivity.this, "获取通知数据失败: " + (response.body()!=null?response.body().getMessage():"接口异常"), Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                });
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Notification>>> call, Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(MessageNotificationActivity.this, "获取通知数据失败: 网络异常", Toast.LENGTH_LONG).show();
                    notificationList.clear();
                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                });
            }
        });
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
        // 已读处理应通过后台接口，如PUT /notification/readAll，原有本地setRead无效，直接请求后台并刷新列表（如有需求可添加请求）。
        // 示例：调用notificationApi.markAllAsRead();
        // 示例后刷新loadNotificationData();
        // 当前先清空列表UI相关已读操作。
        adapter.notifyDataSetChanged();
        btnMarkAllRead.setVisibility(View.GONE);
    }
}
