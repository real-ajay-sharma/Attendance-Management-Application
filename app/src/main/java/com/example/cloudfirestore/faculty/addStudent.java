package com.example.cloudfirestore.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cloudfirestore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class addStudent extends AppCompatActivity {

    EditText addstudent,addrollno;
    Button add;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DocumentReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        addstudent = findViewById(R.id.add_name);
        add = findViewById(R.id.add_button);
       // addrollno = findViewById(R.id.add_rollno);

        final String subject = getIntent().getExtras().getString("subject");
        final String date = getIntent().getExtras().getString("date");


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = addstudent.getText().toString().trim();
               // String rollno = addrollno.getText().toString().trim();

                if (name.isEmpty())
                    Toast.makeText(addStudent.this, "student name or roll no can't be empty", Toast.LENGTH_SHORT).show();
                else {
                    mref = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects")
                            .document(subject);

                    //Map<String,Object> map1 = new HashMap<>();
                    Map<String, Object> map = new HashMap<>();
                    //map1.put(name,"a");
                    map.put(name, "A");
                    mref.set(map, SetOptions.merge());
                    finish();
                    startActivity(new Intent(addStudent.this, studentsName.class)
                            .putExtra("subject", subject)
                    .putExtra("date",date));
                }
            }
        });


    }
}