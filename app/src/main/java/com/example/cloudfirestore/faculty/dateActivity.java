package com.example.cloudfirestore.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.cloudfirestore.R;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dateActivity extends AppCompatActivity {

    CalendarView cv;
    Button next;
    String subject;
    DateFormat dateFormat;
    Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        cv = findViewById(R.id.calendr);
        next = findViewById(R.id.next);
        subject = getIntent().getExtras().getString("subject");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFormat = new SimpleDateFormat("dd MM yyyy");
                date = new Date(cv.getDate());
                startActivity(new Intent(dateActivity.this,studentsName.class)
                .putExtra("subject",subject)
                .putExtra("date",dateFormat.format(date)));
            }
        });
    }
}
