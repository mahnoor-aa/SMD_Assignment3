package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecycleBin extends AppCompatActivity implements RecycleBinAdapter.DeleteContact {

    RecyclerView rvRecycle;
    Button btnBack;
    RecycleBinAdapter adapter;
    ArrayList<Info> passwords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);
        long userId = getIntent().getLongExtra("USER_ID", -1);

        if (userId != -1) {

        } else {
            Toast.makeText(this, "Error: User ID not received", Toast.LENGTH_SHORT).show();

        }

        btnBack=findViewById(R.id.btnBackRecycle);
        rvRecycle = findViewById(R.id.rvRecycle);
        rvRecycle.setHasFixedSize(true);
        rvRecycle.setLayoutManager(new LinearLayoutManager(this));

        InfoDB database = new InfoDB(this);
        database.open();
        passwords = database.readAllRecyclePasswords(userId);
        database.close();

        adapter = new RecycleBinAdapter(this, passwords);

        rvRecycle.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecycleBin.this, MainActivity2.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onDeletePassword(int index) {

        passwords.remove(index);
        adapter.notifyDataSetChanged();

    }
}