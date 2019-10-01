package com.example.cloudfirestore.faculty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cloudfirestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class deleteStudents extends AppCompatActivity {

   FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
   FirebaseFirestore db = FirebaseFirestore.getInstance();
   DocumentReference mdocref2;
   String subject;
    ListView lvds;
    Button done;

   @Override
   protected void onStart()
   {
       super.onStart();
       mdocref2 = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects")
               .document(subject);
       mdocref2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
           @Override
           public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

               if(e != null)
                   Toast.makeText(deleteStudents.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
               else
               if(documentSnapshot != null){
                   Map<String,Object> map = new HashMap<>();
                   map = documentSnapshot.getData();
                   if(map != null){

                       ArrayList<student> arrayList = new ArrayList<>();
                       for(Map.Entry<String,Object> entry : map.entrySet()){
                           arrayList.add(new student(entry.getKey(),R.drawable.ic_delete_black_24dp));
                       }
                       studentDeleteAdapter deleteAdapter = new studentDeleteAdapter(deleteStudents.this,arrayList);
                       lvds.setAdapter(deleteAdapter);
                   }
               }

           }
       });
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_students);

        subject = getIntent().getExtras().getString("subject");
        lvds = findViewById(R.id.lvds);
        done = findViewById(R.id.done);

        displayStudents();

        lvds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                student s = (student) parent.getItemAtPosition(position);
                Map<String,Object> map = new HashMap<>();
                map.put(s.getName(), FieldValue.delete());
                mdocref2 = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects").document(subject);
                mdocref2.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                            Toast.makeText(deleteStudents.this,"successfully deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(deleteStudents.this,"Error!",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(deleteStudents.this,facultyProfile.class));
            }
        });
    }

    private void displayStudents()
    {
        mdocref2 = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects")
                .document(subject);

        mdocref2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        Map<String,Object> map = new HashMap<>();
                        map = document.getData();
                        if(map != null){

                            ArrayList<student> arrayList = new ArrayList<>();
                            for(Map.Entry<String,Object> entry : map.entrySet()){
                                arrayList.add(new student(entry.getKey(),R.drawable.ic_delete_black_24dp));
                            }
                            studentDeleteAdapter deleteAdapter = new studentDeleteAdapter(deleteStudents.this,arrayList);
                            lvds.setAdapter(deleteAdapter);
                        }
                    }
                }
            }
        });
    }
}
