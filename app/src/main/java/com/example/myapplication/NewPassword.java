package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPassword extends AppCompatActivity {

    EditText etUsername, etPassword,etUrl;
    Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        long userId = getIntent().getLongExtra("USER_ID", -1);
        if (userId != -1) {
        } else {
            Toast.makeText(this, "Error: User ID not received", Toast.LENGTH_SHORT).show();
        }


        init();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact(userId);
                Intent intent = new Intent(NewPassword.this, MainActivity2.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPassword.this, MainActivity2.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveContact(long userId)
    {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();
        String url = etUrl.getText().toString();

        InfoDB database = new InfoDB(this);
        database.open();

        database.insert(username, password,url,userId);

        database.close();

    }
    private void init()
    {
        etUsername = findViewById(R.id.etaddUsername);
        etPassword = findViewById(R.id.etaddPassword);
        etUrl= findViewById(R.id.etaddURL);
        btnAdd = findViewById(R.id.btnAddNewPassword);
        btnCancel = findViewById(R.id.btnCancelNewPassword);
    }

}