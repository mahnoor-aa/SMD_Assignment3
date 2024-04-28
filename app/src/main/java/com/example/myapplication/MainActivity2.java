package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements InfoAdapter.DeleteContact{

    FloatingActionButton fabAdd;
    RecyclerView rvPassword;
    InfoAdapter adapter;
    ArrayList<Info> passwords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        long userId = getIntent().getLongExtra("USER_ID", -1);

        if (userId != -1) {

        } else {
            Toast.makeText(this, "Error: User ID not received", Toast.LENGTH_SHORT).show();

        }

        fabAdd = findViewById(R.id.fabAdd);
        rvPassword = findViewById(R.id.rvPasswordDisplay);
        rvPassword.setHasFixedSize(true);
        rvPassword.setLayoutManager(new LinearLayoutManager(this));

        InfoDB database = new InfoDB(this);
        database.open();
        passwords = database.readAllContacts(userId);
        database.close();

        adapter = new InfoAdapter(this, passwords);

        rvPassword.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, NewPassword.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onDeleteContact(int index) {
        passwords.remove(index);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onUpdateContact(int index, String[] values) {
        passwords.get(index).setUsername(values[0]);
        passwords.get(index).setPassword(values[1]);
        passwords.get(index).setUrl(values[2]);
        adapter.notifyDataSetChanged();
    }

}