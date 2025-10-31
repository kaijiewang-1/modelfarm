package logins;

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

public class create_company extends AppCompatActivity {

    private EditText etCompanyName;
    private EditText etCompanyAddress;
    private EditText etContactPerson;
    private EditText etContactPhone;
    private Button btnCreate;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_company);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etCompanyName = findViewById(R.id.etCompanyName);
        etCompanyAddress = findViewById(R.id.etCompanyAddress);
        etContactPerson = findViewById(R.id.etContactPerson);
        etContactPhone = findViewById(R.id.etContactPhone);
        btnCreate = findViewById(R.id.btnCreate);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName = etCompanyName.getText().toString().trim();
                String companyAddress = etCompanyAddress.getText().toString().trim();
                String contactPerson = etContactPerson.getText().toString().trim();
                String contactPhone = etContactPhone.getText().toString().trim();

                if (companyName.isEmpty() || companyAddress.isEmpty() || 
                    contactPerson.isEmpty() || contactPhone.isEmpty()) {
                    Toast.makeText(create_company.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 模拟创建企业成功
                Toast.makeText(create_company.this, "企业创建成功", Toast.LENGTH_SHORT).show();
                
                // 跳转到主控制台
                Intent intent = new Intent(create_company.this, DashboardActivity.class);
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