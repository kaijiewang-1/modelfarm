package company;

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

public class company_info extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView tvCompanyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_info);

        initViews();
        setupToolbar();
        loadCompanyData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvCompanyName = findViewById(R.id.tv_company_name);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回主控制台
                Intent intent = new Intent(company_info.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadCompanyData() {
        // 模拟企业数据
        tvCompanyName.setText("绿色农业科技有限公司");
    }
}