package com.example.cloudfirestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class addStudentSubject extends AppCompatActivity {

    EditText et1, et2, et3;
    Button bt;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_subject);

        et1 = findViewById(R.id.subject_name2);
        et2 = findViewById(R.id.attended_class);
        et3 = findViewById(R.id.total_class);
        bt = findViewById(R.id.submit2);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subject = et1.getText().toString().trim();
                String attended_class1 = et2.getText().toString().trim();
                String total_class1 = et3.getText().toString().trim();
                Long attended_class = 0L;
                Long total_class = 0L;
                if(!(attended_class1.isEmpty() || total_class1.isEmpty())) {
                    attended_class = Long.parseLong(attended_class1);
                    total_class = Long.parseLong(total_class1);
                }
                if (!(subject.isEmpty() || et2.getText().toString().trim().isEmpty() || et3.getText().toString().trim().isEmpty()) && attended_class <= total_class) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("total class", Long.parseLong(et3.getText().toString().trim()));
                    map.put("attended class", Long.parseLong(et2.getText().toString().trim()));

                    String attendance_percentage = "0%";
                    if (total_class != 0L)
                        attendance_percentage = (Long.parseLong(et2.getText().toString().trim()) * 100) / (Long.parseLong(et3.getText().toString().trim())) + "%";

                    map.put("attendance percentage", attendance_percentage);
                    if ((Long.parseLong(et2.getText().toString().trim()) * 100) / (Long.parseLong(et3.getText().toString().trim()) + 1) >= 75)
                        map.put("status", "you can miss next class");
                    else
                        map.put("status", "you can't miss next class");

                    db.collection("students").document(Objects.requireNonNull(firebaseAuth.getUid())).collection("subjects")
                            .document(et1.getText().toString().trim()).set(map);
                    finish();
                    startActivity(new Intent(addStudentSubject.this, studentProfile.class));

                } else
                    Toast.makeText(addStudentSubject.this, "enter all the things correctly", Toast.LENGTH_SHORT).show();
            }
        });
    }
}