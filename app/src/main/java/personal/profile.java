package personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.DashboardActivity;
import com.example.modelfarm.R;
import com.google.android.material.appbar.MaterialToolbar;

public class profile extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private EditText etNickname;
    private EditText etPhone;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        initViews();
        setupToolbar();
        loadUserData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etNickname = findViewById(R.id.et_nickname);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
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
        etNickname.setText("张三");
        etPhone.setText("13800138001");
        etPassword.setText("********");
    }
}