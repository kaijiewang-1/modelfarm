package com.example.modelfarm;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.UserWitnRole;
import com.example.modelfarm.network.services.AdminApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminUserAdapter adapter;
    private ProgressBar progressBar;
    // 假设你有一个单例类 RetrofitClient 用于获取 ApiService

    private AdminApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏默认标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_admin);

        // 初始化 API (这里假设你已经配置好了 Retrofit)
        apiService = RetrofitClient.create(this,AdminApiService.class);

        initView();
        fetchUserList();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressBar);

        // 1. 初始化 RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 2. 初始化 Adapter 并处理回调
        adapter = new AdminUserAdapter(new AdminUserAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(UserWitnRole user) {
                showDeleteConfirmDialog(user);
            }
        });
        recyclerView.setAdapter(adapter);

        // 3. 初始化 Header 的返回按钮
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 关闭当前页面，返回上一页
                finish();
            }
        });
    }

    // 请求用户列表
    private void fetchUserList() {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getAllUsers().enqueue(new Callback<ApiResponse<List<UserWitnRole>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<UserWitnRole>>> call, Response<ApiResponse<List<UserWitnRole>>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        // 更新列表数据
                        adapter.setData(response.body().getData());
                    } else {
                        Toast.makeText(AdminActivity.this, "获取失败: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<UserWitnRole>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 弹出删除确认框
    private void showDeleteConfirmDialog(final UserWitnRole user) {
        new AlertDialog.Builder(this)
                .setTitle("确认移除")
                .setMessage("确定要移除用户 " + user.getUser().getUsername() + " 吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUserFromApi(user);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    // 调用 API 删除用户
    private void deleteUserFromApi(final UserWitnRole user) {
        progressBar.setVisibility(View.VISIBLE);

        apiService.deleteUser(user.getUser().getId()).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(AdminActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        // 从界面移除该项，不用重新请求整个列表
                        adapter.removeItem(user);
                    } else {
                        Toast.makeText(AdminActivity.this, "删除失败: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}