package com.example.cloudfirestore.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cloudfirestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createFaculty extends AppCompatActivity {

    EditText name,email,password;
    Button bt;
    String Email,Password,Name;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference mref ;
    private final String NAME_KEY = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_faculty);

        name = findViewById(R.id.facultyName);
        email = findViewById(R.id.facultyEmail);
        password = findViewById(R.id.facultyPassword);

        bt = findViewById(R.id.submit);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!valid()) {

                    Email = email.getText().toString().trim();
                    Password = password.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                sendVerification();
                                sendInfoToFirebase();
                              //  Toast.makeText(createFaculty.this, "successful", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(createFaculty.this, "Something went wrong, please try again!", Toast.LENGTH_LONG).show();

                        }
                    });
                }
                else
                    Toast.makeText(createFaculty.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendInfoToFirebase()
    {
        Name = name.getText().toString().trim();
        mref = db.collection("faculty").document(firebaseAuth.getUid());

        Map<String,Object> map = new HashMap<>();
        map.put(NAME_KEY,Name);

        mref.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                    Toast.makeText(createFaculty.this,"successful",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(createFaculty.this,"not successful",Toast.LENGTH_LONG).show();

            }
        });
    }

    private boolean valid(){

        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();
        Name = name.getText().toString().trim();

        return Email.isEmpty() || Password.isEmpty() || Name.isEmpty();
    }

    private void sendVerification(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(createFaculty.this, "Verification mail sent, check your email!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(createFaculty.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
