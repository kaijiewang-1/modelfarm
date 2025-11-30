package com.example.modelfarm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.modelfarm.network.AuthManager;
import com.example.modelfarm.network.RetrofitClient;
import com.example.modelfarm.network.models.ApiResponse;
import com.example.modelfarm.network.models.Order;
import com.example.modelfarm.network.models.OrderStatus;
import com.example.modelfarm.network.models.UpdateOrderRequest;
import com.example.modelfarm.network.services.OrderApiService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    private MaterialButton btnSave;
    
    // 编辑相关
    private TextInputEditText etSolution;
    private TextInputEditText etInspectionIssue;
    private MaterialButton btnAddSolutionImage;
    private MaterialButton btnAddInspectionImage;
    private LinearLayout llSolutionImages;
    private LinearLayout llInspectionImages;
    private MaterialCardView cardInspection;
    
    // 图片相关
    private static final int REQUEST_CODE_PICK_SOLUTION_IMAGE = 1001;
    private static final int REQUEST_CODE_PICK_INSPECTION_IMAGE = 1002;
    private List<Uri> solutionImageUris = new ArrayList<>();
    private List<Uri> inspectionImageUris = new ArrayList<>();
    private List<String> solutionImageUrls = new ArrayList<>();
    private List<String> inspectionImageUrls = new ArrayList<>();
    private int currentImageType = 0; // 0: solution, 1: inspection

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
        btnSave = findViewById(R.id.btnSave);
        
        // 编辑相关视图
        etSolution = findViewById(R.id.etSolution);
        etInspectionIssue = findViewById(R.id.etInspectionIssue);
        btnAddSolutionImage = findViewById(R.id.btnAddSolutionImage);
        btnAddInspectionImage = findViewById(R.id.btnAddInspectionImage);
        llSolutionImages = findViewById(R.id.llSolutionImages);
        llInspectionImages = findViewById(R.id.llInspectionImages);
        cardInspection = findViewById(R.id.cardInspection);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentOrder != null) {
                    handleCompleteOrder(currentOrder);
                }
            }
        });
        
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrderChanges();
            }
        });
        
        btnAddSolutionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageType = 0;
                pickImage();
            }
        });
        
        btnAddInspectionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageType = 1;
                pickImage();
            }
        });
    }
    
    /**
     * 选择图片
     */
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        int requestCode = currentImageType == 0 ? REQUEST_CODE_PICK_SOLUTION_IMAGE : REQUEST_CODE_PICK_INSPECTION_IMAGE;
        startActivityForResult(intent, requestCode);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                if (requestCode == REQUEST_CODE_PICK_SOLUTION_IMAGE) {
                    solutionImageUris.add(imageUri);
                    addImageToLayout(imageUri, llSolutionImages, true);
                } else if (requestCode == REQUEST_CODE_PICK_INSPECTION_IMAGE) {
                    inspectionImageUris.add(imageUri);
                    addImageToLayout(imageUri, llInspectionImages, false);
                }
            }
        }
    }
    
    /**
     * 添加图片到布局
     */
    private void addImageToLayout(Uri imageUri, LinearLayout container, boolean isSolution) {
        try {
            // 创建图片视图
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (120 * getResources().getDisplayMetrics().density),
                (int) (120 * getResources().getDisplayMetrics().density)
            );
            params.setMargins(0, 0, (int) (8 * getResources().getDisplayMetrics().density), 0);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            
            // 加载图片
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            if (inputStream != null) inputStream.close();
            
            // 添加删除按钮
            ImageView deleteBtn = new ImageView(this);
            LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                (int) (24 * getResources().getDisplayMetrics().density),
                (int) (24 * getResources().getDisplayMetrics().density)
            );
            deleteBtn.setLayoutParams(deleteParams);
            deleteBtn.setImageResource(android.R.drawable.ic_menu_delete);
            deleteBtn.setBackgroundColor(0x80FF0000);
            deleteBtn.setPadding(4, 4, 4, 4);
            
            // 创建容器
            LinearLayout imageContainer = new LinearLayout(this);
            imageContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            containerParams.setMargins(0, 0, (int) (8 * getResources().getDisplayMetrics().density), 0);
            imageContainer.setLayoutParams(containerParams);
            
            imageContainer.addView(imageView);
            imageContainer.addView(deleteBtn);
            
            // 删除按钮点击事件
            final Uri uriToRemove = imageUri;
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSolution) {
                        solutionImageUris.remove(uriToRemove);
                    } else {
                        inspectionImageUris.remove(uriToRemove);
                    }
                    container.removeView(imageContainer);
                }
            });
            
            container.addView(imageContainer);
        } catch (IOException e) {
            Toast.makeText(this, "加载图片失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

        // 显示解决方案
        if (order.getSolution() != null && !order.getSolution().isEmpty()) {
            etSolution.setText(order.getSolution());
        }
        
        // 显示解决图片
        if (order.getSolutionImages() != null && !order.getSolutionImages().isEmpty()) {
            loadImageUrls(order.getSolutionImages(), solutionImageUrls);
            displayImages(solutionImageUrls, llSolutionImages, true);
        }
        
        // 显示异常情况和巡查图片（仅普通工单和紧急工单）
        int status = order.getStatus();
        boolean showInspection = (status == OrderStatus.PENDING || status == OrderStatus.URGENT);
        
        if (showInspection) {
            cardInspection.setVisibility(View.VISIBLE);
            
            if (order.getInspectionIssue() != null && !order.getInspectionIssue().isEmpty()) {
                etInspectionIssue.setText(order.getInspectionIssue());
            }
            
            if (order.getInspectionImages() != null && !order.getInspectionImages().isEmpty()) {
                loadImageUrls(order.getInspectionImages(), inspectionImageUrls);
                displayImages(inspectionImageUrls, llInspectionImages, false);
            }
        } else {
            cardInspection.setVisibility(View.GONE);
        }

        // 根据接口文档：只能标记待处理(1)或紧急处理(3)状态的工单
        // 不能标记已完成(2)状态的工单
        boolean canComplete = (status == OrderStatus.PENDING || status == OrderStatus.URGENT);

        if (canComplete) {
            btnComplete.setVisibility(View.VISIBLE);
        } else {
            btnComplete.setVisibility(View.GONE);
        }
    }
    
    /**
     * 加载图片URL列表
     */
    private void loadImageUrls(String imageUrlsJson, List<String> targetList) {
        targetList.clear();
        if (imageUrlsJson == null || imageUrlsJson.isEmpty()) {
            return;
        }
        
        try {
            // 尝试解析JSON数组
            Gson gson = new Gson();
            List<String> urls = gson.fromJson(imageUrlsJson, new TypeToken<List<String>>(){}.getType());
            if (urls != null) {
                targetList.addAll(urls);
            }
        } catch (Exception e) {
            // 如果不是JSON，尝试按逗号分隔
            String[] urls = imageUrlsJson.split(",");
            for (String url : urls) {
                String trimmed = url.trim();
                if (!trimmed.isEmpty()) {
                    targetList.add(trimmed);
                }
            }
        }
    }
    
    /**
     * 显示已存在的图片
     */
    private void displayImages(List<String> imageUrls, LinearLayout container, boolean isSolution) {
        container.removeAllViews();
        for (String url : imageUrls) {
            // 这里应该使用图片加载库（如Glide）来加载网络图片
            // 为了简化，这里只显示占位符
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (120 * getResources().getDisplayMetrics().density),
                (int) (120 * getResources().getDisplayMetrics().density)
            );
            params.setMargins(0, 0, (int) (8 * getResources().getDisplayMetrics().density), 0);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            imageView.setBackgroundColor(0xFFE0E0E0);
            container.addView(imageView);
        }
    }
    
    /**
     * 保存工单更改
     */
    private void saveOrderChanges() {
        if (currentOrder == null) {
            Toast.makeText(this, "工单数据未加载", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String solution = etSolution.getText() != null ? etSolution.getText().toString().trim() : "";
        String inspectionIssue = etInspectionIssue.getText() != null ? etInspectionIssue.getText().toString().trim() : "";
        
        // 合并图片URLs（新上传的图片需要先上传到服务器获取URL，这里简化处理）
        // 实际应该先上传图片，获取URL后再保存
        String solutionImagesJson = buildImageUrlsJson(solutionImageUrls, solutionImageUris);
        String inspectionImagesJson = buildImageUrlsJson(inspectionImageUrls, inspectionImageUris);
        
        UpdateOrderRequest request = new UpdateOrderRequest(
            currentOrder.getId(),
            null, // title
            null, // description
            null, // status
            solution.isEmpty() ? null : solution,
            solutionImagesJson.isEmpty() ? null : solutionImagesJson,
            inspectionIssue.isEmpty() ? null : inspectionIssue,
            inspectionImagesJson.isEmpty() ? null : inspectionImagesJson
        );
        
        OrderApiService api = RetrofitClient.create(this, OrderApiService.class);
        api.updateOrder(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Toast.makeText(OrderDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    // 重新加载工单详情
                    loadOrderDetail();
                } else {
                    String msg = response.body() != null ? response.body().getMessage() : "保存失败";
                    Toast.makeText(OrderDetailActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * 构建图片URLs JSON字符串
     */
    private String buildImageUrlsJson(List<String> existingUrls, List<Uri> newUris) {
        List<String> allUrls = new ArrayList<>(existingUrls);
        
        // 注意：新上传的图片需要先上传到服务器获取URL
        // 这里简化处理，实际应该先上传图片
        for (Uri uri : newUris) {
            // TODO: 上传图片到服务器，获取URL后添加到allUrls
            // 这里暂时跳过新图片
        }
        
        if (allUrls.isEmpty()) {
            return "";
        }
        
        Gson gson = new Gson();
        return gson.toJson(allUrls);
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
                    Toast.makeText(OrderDetailActivity.this, "工单标记为已完成", Toast.LENGTH_SHORT).show();
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