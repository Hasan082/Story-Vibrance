package com.hasan.storyvibrance.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hasan.storyvibrance.BottomNav.BottomNavActivity;
import com.hasan.storyvibrance.R;

public class LoginActivity extends AppCompatActivity {

    Button login;
    TextView signTxtBtn;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        signTxtBtn = findViewById(R.id.signTxtBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if
                startActivity(new Intent(LoginActivity.this, BottomNavActivity.class));
            }
        });
        signTxtBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

    }
}