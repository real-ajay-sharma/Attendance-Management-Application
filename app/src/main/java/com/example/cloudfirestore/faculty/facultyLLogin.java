package com.example.cloudfirestore.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudfirestore.R;
import com.example.cloudfirestore.forgotPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class facultyLLogin extends AppCompatActivity {


    EditText emai,pass;
    Button fl;
    TextView cna,tv;
    String email,password;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ProgressDialog pd;

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            finish();
            startActivity(new Intent(facultyLLogin.this,facultyProfile.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_llogin);

        emai = findViewById(R.id.emai);
        pass = findViewById(R.id.pass);
        fl = findViewById(R.id.button3);
        cna = findViewById(R.id.cna);
        tv  = findViewById(R.id.forgot_password_f);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");


        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.show();
                if(!valid()) {

                    email = emai.getText().toString().trim();
                    password = pass.getText().toString().trim();
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                    finish();
                                    startActivity(new Intent(facultyLLogin.this, facultyProfile.class));

                                   // Toast.makeText(facultyLLogin.this, "successful", Toast.LENGTH_LONG).show();
                                }
                                else
                                    sendVerification();
                            }else
                                Toast.makeText(facultyLLogin.this, "Email or password is incorrect!", Toast.LENGTH_LONG).show();

                        }
                    });
                }
                else
                    Toast.makeText(facultyLLogin.this, "Error!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

        cna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(facultyLLogin.this,createFaculty.class));
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(facultyLLogin.this, forgotPassword.class));
            }
        });
    }

    private boolean valid(){

        email = emai.getText().toString().trim();
        password = pass.getText().toString().trim();

        return email.isEmpty() || password.isEmpty();
    }

    private void sendVerification(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(facultyLLogin.this, "Verification mail sent, check your email!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(facultyLLogin.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
