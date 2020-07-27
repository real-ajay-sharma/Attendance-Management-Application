package com.example.cloudfirestore;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class newUser extends AppCompatActivity {

    EditText et3,et4,et5,et6,et7;
    Button bt2;
    TextView tv2;
    String name,email2,password2,age,mobile;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference mref ;
    ProgressDialog pd;

    private final String NAME_KEY = "name";
    private final String MOBILE_KEY = "mobile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        et3 = findViewById(R.id.editText3);
        et5 = findViewById(R.id.editText5);
        et6 = findViewById(R.id.editText6);
        et7 = findViewById(R.id.editText7);
        bt2 = findViewById(R.id.button2);
        tv2 = findViewById(R.id.textView2);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(newUser.this,MainActivity.class));
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.show();
                email2 = et6.getText().toString().trim();
                password2 = et7.getText().toString().trim();

                if(!valid()) {

                    firebaseAuth.createUserWithEmailAndPassword(email2, password2).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            sendVerification();
                            sendInfoToFirebase();
                           // Toast.makeText(newUser.this, authResult.toString(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(newUser.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(newUser.this, "Error!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }
    private void sendInfoToFirebase()
    {
        name = et3.getText().toString().trim();
        mobile = et5.getText().toString().trim();

        mref = db.collection("students").document(firebaseAuth.getUid());

        Map<String , Object> map = new HashMap<>();
        map.put(NAME_KEY,name);
        map.put(MOBILE_KEY,mobile);
        mref.set(map);
    }

    private boolean valid(){

        name = et3.getText().toString().trim();
        mobile = et5.getText().toString().trim();
        email2 = et6.getText().toString().trim();
        password2 = et7.getText().toString().trim();
        return (name.isEmpty()  || mobile.isEmpty() || email2.isEmpty() || password2.isEmpty());
    }

    protected void sendVerification(){

        email2 = et6.getText().toString().trim();
        password2 = et7.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(newUser.this, "Successful registered, Check your email for verification", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(newUser.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
