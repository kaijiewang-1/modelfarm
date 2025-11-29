package com.example.modelfarm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.modelfarm.network.AuthManager;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.Order;
import com.example.modelfarm.network.models.OrderStatus;
import com.example.modelfarm.network.services.OrderApiService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 工单详情页面
 */
public class OrderDetailActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvOrderId;
    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvStatus;
    private TextView tvCreatorId;
    private TextView tvAcceptedId;
    private TextView tvSolvedId;
    private TextView tvCreatedAt;
    private TextView tvCompletedAt;
    private TextView tvUpdatedAt;
    private MaterialButton btnComplete;
    
    private int orderId;
    private Order currentOrder;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);

        orderId = getIntent().getIntExtra("orderId", -1);
        currentUserId = AuthManager.getInstance(this).getUserId();
        
        if (orderId == -1) {
            Toast.makeText(this, "工单ID无效", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        loadOrderDetail();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvStatus = findViewById(R.id.tvStatus);
        tvCreatorId = findViewById(R.id.tvCreatorId);
        tvAcceptedId = findViewById(R.id.tvAcceptedId);
        tvSolvedId = findViewById(R.id.tvSolvedId);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvCompletedAt = findViewById(R.id.tvCompletedAt);
        tvUpdatedAt = findViewById(R.id.tvUpdatedAt);
        btnComplete = findViewById(R.id.btnComplete);
        
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentOrder != null) {
                    handleCompleteOrder(currentOrder);
                }
            }
        });
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadOrderDetail() {
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);
        api.getOrder(orderId).enqueue(new Callback<ApiResponse<Order>>() {
            @Override
            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200 && response.body().getData() != null) {
                    currentOrder = response.body().getData();
                    displayOrderDetail(currentOrder);
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "获取工单详情失败";
                    Toast.makeText(OrderDetailActivity.this, msg, Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void displayOrderDetail(Order order) {
        tvOrderId.setText("工单编号: #" + order.getId());
        tvTitle.setText(order.getTitle());
        tvDescription.setText(order.getDescription());
        tvStatus.setText(getStatusText(order.getStatus()));
        tvCreatorId.setText("创建人ID: " + order.getCreatorId());
        
        if (order.getAcceptedId() != null) {
            tvAcceptedId.setText("受理人ID: " + order.getAcceptedId());
        } else {
            tvAcceptedId.setText("受理人: 未派发");
        }
        
        if (order.getSolvedId() != null) {
            tvSolvedId.setText("处理人ID: " + order.getSolvedId());
        } else {
            tvSolvedId.setText("处理人: 未处理");
        }
        
        tvCreatedAt.setText("创建时间: " + order.getCreatedAt());
        
        if (order.getCompletedAt() != null && !order.getCompletedAt().isEmpty()) {
            tvCompletedAt.setText("完成时间: " + order.getCompletedAt());
        } else {
            tvCompletedAt.setText("完成时间: 未完成");
        }
        
        tvUpdatedAt.setText("更新时间: " + order.getUpdatedAt());
        
        // 只有在"我的工单"中且工单状态为已认领或紧急时，才显示完成按钮
        boolean canComplete = (order.getStatus() == OrderStatus.CLAIMED || 
                              (order.getStatus() == OrderStatus.URGENT && order.isAccepted() == 1)) &&
                              (order.getAcceptedId() != null && order.getAcceptedId() == currentUserId);
        
        if (canComplete && order.getStatus() != OrderStatus.COMPLETED) {
            btnComplete.setVisibility(View.VISIBLE);
        } else {
            btnComplete.setVisibility(View.GONE);
        }
    }

    private String getStatusText(int status) {
        switch (status) {
            case OrderStatus.PENDING:
                return "待处理";
            case OrderStatus.COMPLETED:
                return "已完成";
            case OrderStatus.URGENT:
                return "紧急处理";
            case OrderStatus.CLAIMED:
                return "受理中";
            default:
                return String.valueOf(status);
        }
    }

    private void handleCompleteOrder(Order order) {
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);
        api.completeOrder(order.getId()).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Toast.makeText(OrderDetailActivity.this, "工单已完成", Toast.LENGTH_SHORT).show();
                    // 重新加载工单详情
                    loadOrderDetail();
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "完成失败";
                    Toast.makeText(OrderDetailActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

