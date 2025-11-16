package logins;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.DashboardActivity;
import com.example.modelfarm.R;

public class choose_company extends AppCompatActivity {

    private Button btnCreateCompany;
    private Button btnJoinCompany;
    private Button btnSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_company);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnCreateCompany = findViewById(R.id.btnCreateCompany);
        btnJoinCompany = findViewById(R.id.btnJoinCompany);
        btnSkip = findViewById(R.id.btnSkip);
    }

    private void setupClickListeners() {
        btnCreateCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到创建企业页面
                Intent intent = new Intent(choose_company.this, create_company.class);
                startActivity(intent);
            }
        });

        btnJoinCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到加入企业页面
                Intent intent = new Intent(choose_company.this, join_company.class);
                startActivity(intent);
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳过企业选择，直接进入主控制台
                Intent intent = new Intent(choose_company.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}