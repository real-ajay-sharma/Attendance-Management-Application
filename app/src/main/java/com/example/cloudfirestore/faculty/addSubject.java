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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class addSubject extends AppCompatActivity {

    EditText addsubject;
    Button add;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        addsubject = findViewById(R.id.add_subject);
        add = findViewById(R.id.add_button_subject);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addsubject.getText().toString().trim().isEmpty())
                    Toast.makeText(addSubject.this, "subject can't be empty", Toast.LENGTH_SHORT).show();
                else {
                    Map<String, Object> map = new HashMap<>();
                    db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects").document(addsubject.getText().toString())
                            .set(map, SetOptions.merge());
                    finish();
                    startActivity(new Intent(addSubject.this, facultyProfile.class));
                }
            }
        });
    }
}
