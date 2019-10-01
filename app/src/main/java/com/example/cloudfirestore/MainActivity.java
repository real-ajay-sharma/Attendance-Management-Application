package com.example.cloudfirestore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;

public class MainActivity extends AppCompatActivity {

    EditText et1,et2;
    TextView tv,tv2;
    Button bt;
    String email,password;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ProgressDialog pd;


    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null && user.isEmailVerified()) {
            finish();
            startActivity(new Intent(MainActivity.this, studentProfile.class));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = findViewById(R.id.editText);
        et2 = findViewById(R.id.editText2);
        tv = findViewById(R.id.textView);
        tv2 = findViewById(R.id.forgot_password_s);
        bt = findViewById(R.id.button);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.show();
                email = et1.getText().toString().trim();
                password = et2.getText().toString().trim();
                if(!valid()) {

                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                        finish();
                                        startActivity(new Intent(MainActivity.this, studentProfile.class));
                                        Toast.makeText(MainActivity.this, "successful", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                        sendVerification();
                                } else
                                    Toast.makeText(MainActivity.this, "Email or password is incorrect!", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                    else
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,newUser.class));
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,forgotPassword.class));
            }
        });

    }

    private boolean valid(){
        email = et1.getText().toString().trim();
        password = et2.getText().toString().trim();
        return email.isEmpty() || password.isEmpty();
    }
    private void sendVerification(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(MainActivity.this, "Verification mail sent to your email, verify to login!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
