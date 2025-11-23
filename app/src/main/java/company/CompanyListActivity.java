package company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.R;
import com.google.android.material.button.MaterialButton;

import logins.create_company;
import logins.join_company;

/**
 * 企业选择页面
 * 只保留加入企业和创建企业两个功能
 */
public class CompanyListActivity extends AppCompatActivity {

    private MaterialButton btnJoinCompany;
    private MaterialButton btnCreateCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_company_list);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnJoinCompany = findViewById(R.id.btnJoinCompany);
        btnCreateCompany = findViewById(R.id.btnCreateCompany);
    }

    private void setupClickListeners() {
        btnJoinCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyListActivity.this, join_company.class);
                startActivity(intent);
            }
        });

        btnCreateCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyListActivity.this, create_company.class);
                startActivity(intent);
            }
        });
    }
}
