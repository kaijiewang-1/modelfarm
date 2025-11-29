package com.example.modelfarm;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.DispatchOrderRequest;
import com.example.modelfarm.network.models.EnterpriseUser;
import com.example.modelfarm.network.services.EnterpriseApiService;
import com.example.modelfarm.network.services.OrderApiService;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 工单派发页面
 * 使用 POST /order/dispatch 派发工单给指定用户
 */
public class OrderDispatchActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView tvTitle;
    private AutoCompleteTextView spUsers;
    private MaterialButton btnDispatch;
    private TextView tvOrderInfo;

    private int orderId;
    private String orderTitle;
    private List<EnterpriseUser> userList = new ArrayList<>();
    private int selectedUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_dispatch);

        // 获取传递的工单信息
        orderId = getIntent().getIntExtra("orderId", -1);
        orderTitle = getIntent().getStringExtra("orderTitle");
        
        if (orderId == -1 || orderTitle == null) {
            Toast.makeText(this, "工单信息无效", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupClicks();
        loadEnterpriseUsers();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        spUsers = findViewById(R.id.spUsers);
        btnDispatch = findViewById(R.id.btnDispatch);
        tvOrderInfo = findViewById(R.id.tvOrderInfo);

        tvTitle.setText("派发工单");
        tvOrderInfo.setText("工单: " + orderTitle);
    }

    private void setupClicks() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUserId == -1) {
                    Toast.makeText(OrderDispatchActivity.this, "请选择派发人员", Toast.LENGTH_SHORT).show();
                    return;
                }
                dispatchOrder();
            }
        });
    }

    private void loadEnterpriseUsers() {
        EnterpriseApiService api = RetrofitClient.create(this, EnterpriseApiService.class);
        // 获取当前企业用户列表
        api.getEnterpriseUsers().enqueue(new Callback<ApiResponse<List<EnterpriseUser>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<EnterpriseUser>>> call, Response<ApiResponse<List<EnterpriseUser>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200 && response.body().getData() != null) {
                    userList.clear();
                    userList.addAll(response.body().getData());
                    setupUserDropdown();
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "获取用户列表失败";
                    Toast.makeText(OrderDispatchActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<EnterpriseUser>>> call, Throwable t) {
                Toast.makeText(OrderDispatchActivity.this, "网络错误:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupUserDropdown() {
        List<String> userNames = new ArrayList<>();
        for (EnterpriseUser user : userList) {
            userNames.add(user.getUsername() + " (ID: " + user.getId() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        spUsers.setAdapter(adapter);
        
        spUsers.setOnItemClickListener((parent, view, position, id) -> {
            selectedUserId = userList.get(position).getId();
        });
    }

    private void dispatchOrder() {
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);
        DispatchOrderRequest request = new DispatchOrderRequest(orderId, selectedUserId);
        
        btnDispatch.setEnabled(false);
        btnDispatch.setText("派发中...");
        
        api.dispatchOrder(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                btnDispatch.setEnabled(true);
                btnDispatch.setText("派发");
                
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Toast.makeText(OrderDispatchActivity.this, "工单派发成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "派发失败";
                    Toast.makeText(OrderDispatchActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                btnDispatch.setEnabled(true);
                btnDispatch.setText("派发");
                Toast.makeText(OrderDispatchActivity.this, "网络错误:" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
