package company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.button.MaterialButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modelfarm.DashboardActivity;
import com.example.modelfarm.R;
import com.example.modelfarm.network.AuthManager;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import logins.create_company;
import logins.join_company;

/**
 * 企业列表管理页面
 * 处理已入企业和未加入企业两种状态
 */
public class CompanyListActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvSubtitle;
    private ImageView ivBuilding;
    private RecyclerView rvCompanyList;
    private MaterialCardView cardNoCompany;
    private MaterialButton btnJoinCompany;
    private MaterialButton btnCreateCompany;
    private TextView btnLogout;

    private List<Company> companyList = new ArrayList<>();
    private CompanyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (!AuthManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, logins.login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_company_list);

        initViews();
        setupRecyclerView();
        loadCompanyData();
        setupClickListeners();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        ivBuilding = findViewById(R.id.ivBuilding);
        rvCompanyList = findViewById(R.id.rvCompanyList);
        cardNoCompany = findViewById(R.id.cardNoCompany);
        btnJoinCompany = findViewById(R.id.btnJoinCompany);
        btnCreateCompany = findViewById(R.id.btnCreateCompany);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupRecyclerView() {
        adapter = new CompanyAdapter(companyList, new CompanyAdapter.OnCompanyClickListener() {
            @Override
            public void onCompanyClick(Company company) {
                // 选择企业，跳转到主控制台
                Intent intent = new Intent(CompanyListActivity.this, DashboardActivity.class);
                intent.putExtra("company_name", company.getName());
                startActivity(intent);
                finish();
            }
        });
        
        rvCompanyList.setLayoutManager(new LinearLayoutManager(this));
        rvCompanyList.setAdapter(adapter);
    }

    private void loadCompanyData() {
        // 模拟加载企业数据
        companyList.clear();
        
        // 模拟已加入的企业
        companyList.add(new Company("绿农生态科技股份有限公司", "已加入", true));
        companyList.add(new Company("智慧农业科技有限公司", "已加入", true));
        
        if (companyList.isEmpty()) {
            showNoCompanyState();
        } else {
            showCompanyListState();
        }
        
        adapter.notifyDataSetChanged();
    }

    private void showNoCompanyState() {
        tvTitle.setText("未加入企业");
        tvSubtitle.setText("您还没有加入任何企业，请先加入或创建企业");
        ivBuilding.setVisibility(View.VISIBLE);
        rvCompanyList.setVisibility(View.GONE);
        cardNoCompany.setVisibility(View.VISIBLE);
    }

    private void showCompanyListState() {
        tvTitle.setText("已入企业");
        tvSubtitle.setText("请选择您要进入的企业");
        ivBuilding.setVisibility(View.GONE);
        rvCompanyList.setVisibility(View.VISIBLE);
        cardNoCompany.setVisibility(View.GONE);
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

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 退出登录
                AuthManager.getInstance(CompanyListActivity.this).logout();
                Intent intent = new Intent(CompanyListActivity.this, logins.login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 页面恢复时重新加载数据
        loadCompanyData();
    }

    /**
     * 企业数据模型
     */
    public static class Company {
        private final String name;
        private final String status;
        private final boolean isJoined;

        public Company(String name, String status, boolean isJoined) {
            this.name = name;
            this.status = status;
            this.isJoined = isJoined;
        }

        public String getName() { return name; }
        public String getStatus() { return status; }
        public boolean isJoined() { return isJoined; }
    }
}
