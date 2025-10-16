package personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.DashboardActivity;
import com.example.modelfarm.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class profile extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private TextView tvUserPhone;
    private MaterialButton btnEditProfile;
    private MaterialButton btnChangePassword;
    private MaterialButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        initViews();
        setupToolbar();
        loadUserData();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvUserName = findViewById(R.id.tv_user_name);
        // 暂时注释掉不存在的资源引用
        // tvUserEmail = findViewById(R.id.tv_user_email);
        // tvUserPhone = findViewById(R.id.tv_user_phone);
        // btnEditProfile = findViewById(R.id.btn_edit_profile);
        // btnChangePassword = findViewById(R.id.btn_change_password);
        // btnLogout = findViewById(R.id.btn_logout);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回主控制台
                Intent intent = new Intent(profile.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadUserData() {
        // 模拟用户数据
        tvUserName.setText("张先生");
        // tvUserEmail.setText("zhang@example.com");
        // tvUserPhone.setText("13800138000");
    }

    private void setupClickListeners() {
        // 暂时注释掉不存在的按钮点击事件
        // 这些功能将在布局文件创建后启用
    }
}