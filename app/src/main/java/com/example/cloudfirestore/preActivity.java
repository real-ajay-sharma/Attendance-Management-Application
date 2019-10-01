package com.example.cloudfirestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cloudfirestore.faculty.facultyLLogin;

public class preActivity extends AppCompatActivity {

    Button st,ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre);

        st = findViewById(R.id.st);
        ft = findViewById(R.id.ft);

        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(preActivity.this,MainActivity.class));
            }
        });
        ft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(preActivity.this, facultyLLogin.class));
            }
        });
    }
}
