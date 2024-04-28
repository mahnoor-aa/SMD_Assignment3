package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogIn, btnSignUp;
    private InfoDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new InfoDB(this);
        dbHelper.open();

        etUsername = findViewById(R.id.etUsernameforapp);
        etPassword = findViewById(R.id.etPasswordforapp);
        btnLogIn = findViewById(R.id.btnLogInforapp);
        btnSignUp = findViewById(R.id.btnSignUpforapp);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (dbHelper.authenticateUser(username, password)) {
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    long userId = dbHelper.getUserIdByUsername(username);
                    openMainActivity2(userId);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    etUsername.getText().clear();
                    etPassword.getText().clear();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    long result = dbHelper.addUser(username, password);
                    if (result != -1) {
                        Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                        etUsername.getText().clear();
                        etPassword.getText().clear();

                        long userId = dbHelper.getUserIdByUsername(username);
                        openMainActivity2(userId);
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void openMainActivity2(long userId) {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
