package login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modelfarm.R;

public class register extends AppCompatActivity {

    private EditText etPhone;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private TextView tvGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        // 暂时注释掉不存在的资源引用
        // etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        // tvGoLogin = findViewById(R.id.tvGoLogin);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                // String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(register.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 暂时注释掉密码确认功能
                // if (!password.equals(confirmPassword)) {
                //     Toast.makeText(register.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                //     return;
                // }

                // 模拟注册成功
                Toast.makeText(register.this, "注册成功", Toast.LENGTH_SHORT).show();
                
                // 跳转到登录页面
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        // 暂时注释掉不存在的按钮点击事件
        // tvGoLogin.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         // 返回登录页面
        //         Intent intent = new Intent(register.this, login.class);
        //         startActivity(intent);
        //         finish();
        //     }
        // });
    }
}