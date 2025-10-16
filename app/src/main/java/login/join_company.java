package login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.DashboardActivity;
import com.example.modelfarm.R;

public class join_company extends AppCompatActivity {

    private EditText etCompanyCode;
    private EditText etInviteCode;
    private Button btnJoin;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_join_company);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etCompanyCode = findViewById(R.id.etCompanyCode);
        etInviteCode = findViewById(R.id.etInviteCode);
        btnJoin = findViewById(R.id.btnJoin);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyCode = etCompanyCode.getText().toString().trim();
                String inviteCode = etInviteCode.getText().toString().trim();

                if (companyCode.isEmpty() || inviteCode.isEmpty()) {
                    Toast.makeText(join_company.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 模拟加入企业成功
                Toast.makeText(join_company.this, "成功加入企业", Toast.LENGTH_SHORT).show();
                
                // 跳转到主控制台
                Intent intent = new Intent(join_company.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回企业选择页面
                finish();
            }
        });
    }
}