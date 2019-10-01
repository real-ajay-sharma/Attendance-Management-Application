package com.example.cloudfirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    EditText et;
    Button bt;
    String email;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        et = findViewById(R.id.forgotEmail);
        bt = findViewById(R.id.reset_password);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et.getText().toString().trim();

                if (!email.isEmpty()) {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(forgotPassword.this, "Password reset link sent to your email.", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(forgotPassword.this, "Email is not registered, register your email first!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    Toast.makeText(forgotPassword.this, "Email is empty!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
