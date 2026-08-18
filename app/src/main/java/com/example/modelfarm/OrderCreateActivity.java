package com.example.modelfarm;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.CreateOrderRequest;
import com.example.modelfarm.network.models.OrderStatus;
import com.example.modelfarm.network.services.OrderApiService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  OrderCreateActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextInputEditText etTitle;
    private TextInputEditText etDescription;
    private AutoCompleteTextView spStatus;
    private MaterialButton btnSubmit;

    private int currentStatus = OrderStatus.PENDING; // 默认待处理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_create);

        initViews();
        setupToolbar();
        setupStatusDropdown();
        setupSubmit();
        prefillFromIntent();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        spStatus = findViewById(R.id.sp_status);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
    }

    private void setupStatusDropdown() {
        // 创建工单时只能选择待处理或紧急处理，不能直接创建已完成状态的工单
        String[] items = new String[]{"待处理", "紧急处理"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        spStatus.setAdapter(adapter);
        spStatus.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                currentStatus = OrderStatus.PENDING;
            } else if (position == 1) {
                currentStatus = OrderStatus.URGENT;
            } else {
                currentStatus = OrderStatus.PENDING; // 默认待处理
            }
        });
        // 默认选择待处理
        spStatus.setText(items[0], false);
        currentStatus = OrderStatus.PENDING;
    }

    private void prefillFromIntent() {
        String hintTitle = getIntent().getStringExtra("hint_title");
        String hintDesc = getIntent().getStringExtra("hint_desc");
        if (hintTitle != null) etTitle.setText(hintTitle);
        if (hintDesc != null) etDescription.setText(hintDesc);
    }

    private void setupSubmit() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText()!=null? etTitle.getText().toString().trim(): "";
                String desc = etDescription.getText()!=null? etDescription.getText().toString().trim(): "";
                if (title.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(OrderCreateActivity.this, "请填写标题与描述", Toast.LENGTH_SHORT).show();
                    return;
                }
                submitOrder(title, desc, currentStatus);
            }
        });
    }

    private void submitOrder(String title, String description, int status) {
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);
        CreateOrderRequest request = new CreateOrderRequest(title, description, status);
        btnSubmit.setEnabled(false);
        btnSubmit.setText("提交中...");
        api.createOrder(request).enqueue(new Callback<ApiResponse<Integer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Integer>> call, Response<ApiResponse<Integer>> response) {
                btnSubmit.setEnabled(true);
                btnSubmit.setText("提交");
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200) {
                    Toast.makeText(OrderCreateActivity.this, "创建成功，工单#" + response.body().getData(), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    String msg = response.body()!=null ? response.body().getMessage() : "创建失败";
                    Toast.makeText(OrderCreateActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Integer>> call, Throwable t) {
                btnSubmit.setEnabled(true);
                btnSubmit.setText("提交");
                Toast.makeText(OrderCreateActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}