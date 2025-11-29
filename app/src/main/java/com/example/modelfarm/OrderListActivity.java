package com.example.modelfarm;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.network.AuthManager;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.AcceptOrderRequest;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.CheckInOrderRequest;
import com.example.modelfarm.network.models.DispatchOrderRequest;
import com.example.modelfarm.network.models.Order;
import com.example.modelfarm.network.services.AdminApiService;
import com.example.modelfarm.network.services.OrderApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import company.company_info;
import personal.profile;
import farm.farm_list;

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
    private TabLayout tabLayout;

    private OrderListAdapter adapter;
    private List<Order> orderList = new ArrayList<>();
    private List<Order> allOrderList = new ArrayList<>(); // 全部工单
    private List<Order> myOrderList = new ArrayList<>(); // 我的工单
    private int currentTab = 0; // 0-全部, 1-我的
    private int currentUserId = -1;
    private boolean isAdmin = false; // 是否为管理员

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_list);

        initViews();
        setupRecyclerView();
        setupClicks();
        initBottomNavigation();
        checkAdminPermission();
        loadOrders();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        rvOrderList = findViewById(R.id.rvOrderList);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);
        tabLayout = findViewById(R.id.tabLayout);

        // 获取当前用户ID
        currentUserId = AuthManager.getInstance(this).getUserId();

        // 设置标签栏
        setupTabLayout();
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        tabLayout.addTab(tabLayout.newTab().setText("我的"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                switchTab();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void switchTab() {
        orderList.clear();
        if (currentTab == 0) {
            // 全部工单
            orderList.addAll(allOrderList);
        } else {
            // 我的工单
            orderList.addAll(myOrderList);
        }
        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void setupRecyclerView() {
        adapter = new OrderListAdapter(orderList, new OrderListAdapter.OnOrderActionListener() {
            @Override
            public void onDelete(Order order) {
                confirmDelete(order);
            }

            @Override
            public void onClaim(Order order) {
                handleClaimOrder(order);
            }

            @Override
            public void onDispatch(Order order) {
                handleDispatchOrder(order);
            }

            @Override
            public void onComplete(Order order) {
                handleCompleteOrder(order);
            }

            @Override
            public void onCheckIn(Order order) {
                handleCheckInOrder(order);
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
                    if (isAdmin) {
                        android.content.Intent intent = new android.content.Intent(OrderListActivity.this, OrderCreateActivity.class);
                        startActivity(intent);
                    } else {
                        android.widget.Toast.makeText(OrderListActivity.this, "只有管理员可以创建工单", android.widget.Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        if (bottomNavigationView == null) {
            return;
        }

        bottomNavigationView.setSelectedItemId(R.id.menu_orders);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_orders) {
                return true;
            } else if (itemId == R.id.menu_dashboard) {
                startActivity(new android.content.Intent(OrderListActivity.this, DashboardActivity.class));
            } else if (itemId == R.id.menu_devices) {
                startActivity(new android.content.Intent(OrderListActivity.this, DeviceManagementActivity.class));
            } else if (itemId == R.id.menu_farms) {
                startActivity(new android.content.Intent(OrderListActivity.this, farm_list.class));
            } else if (itemId == R.id.menu_company) {
                startActivity(new android.content.Intent(OrderListActivity.this, company_info.class));
            } else {
                return false;
            }
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        });
    }

    private void loadOrders() {
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);

        // 加载全部工单
        api.getAllOrders().enqueue(new Callback<ApiResponse<List<Order>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200 && response.body().getData()!=null) {
                    allOrderList.clear();
                    allOrderList.addAll(response.body().getData());

                    // 如果当前是"全部"标签，更新列表
                    if (currentTab == 0) {
                        orderList.clear();
                        orderList.addAll(allOrderList);
                        adapter.notifyDataSetChanged();
                    }
                }
                updateEmptyState();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                updateEmptyState();
            }
        });

        // 加载我的工单（如果用户已登录）
        if (currentUserId > 0) {
            api.getOrdersByUserId(currentUserId).enqueue(new Callback<ApiResponse<List<Order>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<Order>>> call, Response<ApiResponse<List<Order>>> response) {
                    if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200 && response.body().getData()!=null) {
                        myOrderList.clear();
                        myOrderList.addAll(response.body().getData());

                        // 如果当前是"我的"标签，更新列表
                        if (currentTab == 1) {
                            orderList.clear();
                            orderList.addAll(myOrderList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    updateEmptyState();
                }

                @Override
                public void onFailure(Call<ApiResponse<List<Order>>> call, Throwable t) {
                    updateEmptyState();
                }
            });
        }
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

    private void handleClaimOrder(Order order) {
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);
        AcceptOrderRequest request = new AcceptOrderRequest(order.getId());
        api.acceptOrder(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200) {
                    android.widget.Toast.makeText(OrderListActivity.this, "工单认领成功", android.widget.Toast.LENGTH_SHORT).show();
                    loadOrders();
                } else {
                    String msg = response.body()!=null? response.body().getMessage(): "认领失败";
                    android.widget.Toast.makeText(OrderListActivity.this, msg, android.widget.Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                android.widget.Toast.makeText(OrderListActivity.this, "网络错误:"+t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleDispatchOrder(Order order) {
        // 检查管理员权限
        if (!isAdmin) {
            android.widget.Toast.makeText(OrderListActivity.this, "只有管理员可以派发工单", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        // 打开派发工单页面，让用户选择派发对象
        android.content.Intent intent = new android.content.Intent(OrderListActivity.this, OrderDispatchActivity.class);
        intent.putExtra("orderId", order.getId());
        intent.putExtra("orderTitle", order.getTitle());
        startActivity(intent);
    }

    /**
     * 检查管理员权限
     */
    private void checkAdminPermission() {
        AdminApiService api = RetrofitClient.create(this, AdminApiService.class);
        api.checkAdminPermission().enqueue(new retrofit2.Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse<Void>> call, retrofit2.Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    isAdmin = true;
                    // 显示创建工单按钮
                    if (btnCreateOrder != null) {
                        btnCreateOrder.setVisibility(View.VISIBLE);
                    }
                } else {
                    isAdmin = false;
                    // 隐藏创建工单按钮
                    if (btnCreateOrder != null) {
                        btnCreateOrder.setVisibility(View.GONE);
                    }
                }
                // 更新Adapter的管理员权限状态
                if (adapter != null) {
                    adapter.setAdmin(isAdmin);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse<Void>> call, Throwable t) {
                isAdmin = false;
                // 隐藏创建工单按钮
                if (btnCreateOrder != null) {
                    btnCreateOrder.setVisibility(View.GONE);
                }
                // 更新Adapter的管理员权限状态
                if (adapter != null) {
                    adapter.setAdmin(false);
                }
            }
        });
    }

    private void handleCompleteOrder(Order order) {
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);
        api.completeOrder(order.getId()).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200) {
                    android.widget.Toast.makeText(OrderListActivity.this, "工单已完成", android.widget.Toast.LENGTH_SHORT).show();
                    loadOrders();
                } else {
                    String msg = response.body()!=null? response.body().getMessage(): "完成失败";
                    android.widget.Toast.makeText(OrderListActivity.this, msg, android.widget.Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                android.widget.Toast.makeText(OrderListActivity.this, "网络错误:"+t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleCheckInOrder(Order order) {
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);
        CheckInOrderRequest request = new CheckInOrderRequest(order.getId());
        api.checkInOrder(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body()!=null && response.body().getCode()==200) {
                    android.widget.Toast.makeText(OrderListActivity.this, "工单打卡成功", android.widget.Toast.LENGTH_SHORT).show();
                    loadOrders();
                } else {
                    String msg = response.body()!=null? response.body().getMessage(): "打卡失败";
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