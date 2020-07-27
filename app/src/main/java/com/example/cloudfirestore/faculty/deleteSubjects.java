package com.example.cloudfirestore.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cloudfirestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class deleteSubjects extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference mdocref2;
    ListView lvdsub;
    Button done;

    @Override
    protected void onStart()
    {
        super.onStart();
        mdocref2 = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects");
        mdocref2.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<student> arrayList = new ArrayList<>();

                if(e != null)
                {
                    Toast.makeText(deleteSubjects.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {

                        arrayList.add(new student(qds.getId(), R.drawable.ic_delete_black_24dp));
                        //  Toast.makeText(facultyProfile.this, qds.getId()+"", Toast.LENGTH_SHORT).show();
                    }
                    studentDeleteAdapter adapter = new studentDeleteAdapter(deleteSubjects.this,arrayList);
                    lvdsub.setAdapter(adapter);
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_subjects);
        lvdsub = findViewById(R.id.lvdsub);
        done = findViewById(R.id.done2);

        displaySubjects();

        lvdsub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                student s = (student) parent.getItemAtPosition(position);
                db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects")
                        .document(s.getName()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(deleteSubjects.this, "successfully deleted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(deleteSubjects.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(deleteSubjects.this,facultyProfile.class));
            }
        });
    }

    private void displaySubjects()
    {
        mdocref2 = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects");

        mdocref2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ArrayList<student> arrayList = new ArrayList<>();

                if(queryDocumentSnapshots.isEmpty())
                {

                }
                else
                {
                    for(QueryDocumentSnapshot qds: queryDocumentSnapshots){

                        arrayList.add(new student(qds.getId(),R.drawable.ic_delete_black_24dp));
                        //  Toast.makeText(facultyProfile.this, qds.getId()+"", Toast.LENGTH_SHORT).show();
                    }

                    studentDeleteAdapter adapter = new studentDeleteAdapter(deleteSubjects.this,arrayList);
                    lvdsub.setAdapter(adapter);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(deleteSubjects.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
