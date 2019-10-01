package com.example.cloudfirestore.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudfirestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.Inflater;

import javax.annotation.Nullable;

public class studentsName extends AppCompatActivity {

    ListView lvsn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DocumentReference mdocref1,mdocref2;
    String subject,date;
    FloatingActionButton fabc;
    ProgressDialog pd ;
    TextView tv;
    View view;

    @Override
    protected void onStart(){
        super.onStart();

        subject = getIntent().getExtras().getString("subject");

        mdocref2 = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects")
                .document(subject).collection("Date").document(date);

        mdocref2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){
                    Map<String,Object> map = new HashMap<>();
                    map = documentSnapshot.getData();
                    if(map != null){

                        ArrayList<student> arrayList = new ArrayList<>();
                        for(Map.Entry<String,Object> entry : map.entrySet()){
                            arrayList.add(new student(entry.getKey(),entry.getValue().toString()));
                        }
                        studentsNameAdapter adapter = new studentsNameAdapter(studentsName.this,arrayList);
                        lvsn.setAdapter(adapter);
                    }
                }
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_name);

        subject  = Objects.requireNonNull(getIntent().getExtras()).getString("subject");
        date = getIntent().getExtras().getString("date");

        lvsn = findViewById(R.id.lvsn);
        fabc = findViewById(R.id.fabc);
        tv = findViewById(R.id.textf);
        view = findViewById(R.id.empty_view);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        copyStudentsNameToDate();
        loadStudntsName();

        lvsn.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               student name = (student) parent.getAdapter().getItem(position);
               student attendance = (student) parent.getAdapter().getItem(position);

                mdocref2 = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects")
                        .document(subject).collection("Date").document(date);

                Map<String,Object> map = new HashMap<>();
                if(attendance.getAttendance().equals("a")){
                    map.put(name.getName(),"p");
                }
                else
                    map.put(name.getName(),"a");
                mdocref2.set(map,SetOptions.merge());
            }
        });

        fabc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(new Intent(studentsName.this,addStudent.class)
                .putExtra("subject",subject)
                        .putExtra("date",date));
            }
        });

        tv.setText("Students Registered in "+subject+" are listed below...");
        lvsn.setEmptyView(view);

    }

    private void copyStudentsNameToDate()
    {
        pd.show();
        mdocref1 = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects").document(subject);
           mdocref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                   if(task.isSuccessful()){
                       DocumentSnapshot document = task.getResult();
                       if(document.exists()){

                           Map<String, Object> map = new HashMap<>();

                           map = document.getData();
                           if(map != null) {

                                   mdocref2 = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects")
                                           .document(subject).collection("Date").document(date);

                                   final Map<String, Object> finalMap = map;
                                   mdocref2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                       @Override
                                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                           if(task.isSuccessful())
                                           {
                                               DocumentSnapshot document1 = task.getResult();
                                               if(document1.exists()) {
                                                   Map<String, Object> map1 = new HashMap<>();

                                                   map1 = document1.getData();
                                                   if (map1 != null) {
                                                       mdocref2.set(finalMap, SetOptions.merge());
                                                       mdocref2.set(map1, SetOptions.merge());
                                                   }
                                               }
                                               else{
                                                   mdocref2.set(finalMap, SetOptions.merge());
                                               }
                                           }

                                       }
                                   });
                           }
                       }
                   }
               }
           });
    }

    private void loadStudntsName()
    {

            mdocref2 = db.collection("faculty").document(Objects.requireNonNull(firebaseAuth.getUid())).collection("subjects")
                    .document(subject).collection("Date").document(date);

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
                                arrayList.add(new student(entry.getKey(),entry.getValue().toString()));
                            }
                            studentsNameAdapter adapter = new studentsNameAdapter(studentsName.this,arrayList);
                            lvsn.setAdapter(adapter);
                            pd.dismiss();
                        }
                        else{
                            pd.dismiss();
                            Toast.makeText(studentsName.this, "No data present", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        pd.dismiss();
                }
                else{
                    pd.dismiss();
                    Toast.makeText(studentsName.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.students_name_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.delete_students:
                startActivity(new Intent(this,deleteStudents.class)
                .putExtra("subject",subject));
                break;
            case R.id.check_attendance:
                startActivity(new Intent(studentsName.this,checkAttendance.class)
                .putExtra("subject",subject));
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
