package com.example.modelfarm;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.Order;
import com.example.modelfarm.network.services.OrderApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 工单列表页面
 * 使用 GET /order/list 获取并展示工单
 */
public class OrderListActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTitle;
    private com.google.android.material.button.MaterialButton btnCreateOrder;
    private RecyclerView rvOrderList;
    private TextView tvEmptyState;

    private OrderListAdapter adapter;
    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_list);

        initViews();
        setupRecyclerView();
        setupClicks();
        loadOrders();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        rvOrderList = findViewById(R.id.rvOrderList);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);
    }

    private void setupRecyclerView() {
        adapter = new OrderListAdapter(orderList, new OrderListAdapter.OnOrderActionListener() {
            @Override
            public void onDelete(Order order) {
                confirmDelete(order);
            }
        });
        rvOrderList.setLayoutManager(new LinearLayoutManager(this));
        rvOrderList.setAdapter(adapter);
    }

    private void setupClicks() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (btnCreateOrder != null) {
            btnCreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.content.Intent intent = new android.content.Intent(OrderListActivity.this, OrderCreateActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void loadOrders() {
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);
        api.getAllOrders().enqueue(new Callback<ApiResponse<List<Order>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200 && response.body().getData()!=null) {
                    orderList.clear();
                    orderList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                }
                updateEmptyState();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                updateEmptyState();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 返回列表时刷新
        loadOrders();
    }

    private void confirmDelete(final Order order) {
        String content = "#"+order.getId()+"\n标题: "+order.getTitle()+"\n描述: "+order.getDescription()+"\n状态: "+order.getStatus()+"\n创建时间: "+order.getCreatedAt();
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("确认删除该工单？")
                .setMessage(content)
                .setNegativeButton("取消", null)
                .setPositiveButton("删除", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        performDelete(order.getId());
                    }
                })
                .show();
    }

    private void performDelete(int orderId) {
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);
        api.deleteOrder(orderId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200) {
                    android.widget.Toast.makeText(OrderListActivity.this, "删除成功", android.widget.Toast.LENGTH_SHORT).show();
                    loadOrders();
                } else {
                    String msg = response.body()!=null? response.body().getMessage(): "删除失败";
                    android.widget.Toast.makeText(OrderListActivity.this, msg, android.widget.Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                android.widget.Toast.makeText(OrderListActivity.this, "网络错误:"+t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateEmptyState() {
        if (adapter.getItemCount() == 0) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvOrderList.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvOrderList.setVisibility(View.VISIBLE);
        }
    }
}


