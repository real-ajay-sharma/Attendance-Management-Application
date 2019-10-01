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
import android.widget.Toast;

import com.example.cloudfirestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import javax.annotation.Nullable;

public class facultyProfile extends AppCompatActivity {

    FloatingActionButton fab;
    ListView lvf;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference mdocref;
    CollectionReference mcolref;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final String FACULTY_KEY = "faculty";
    private final String SUBJECTS_KEY = "subjects";
    ProgressDialog pd ;
    View v;

    @Override
    protected void onStart()
    {
        super.onStart();

        displaySubjects();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_profile);

        fab = findViewById(R.id.fabf);
        lvf  = findViewById(R.id.lvf);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        v = findViewById(R.id.empty_view);
        displaySubjects();

        lvf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(facultyProfile.this,dateActivity.class)
                        .putExtra("subject",parent.getItemAtPosition(position)+""));

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(facultyProfile.this,addSubject.class));

            }
        });

        lvf.setEmptyView(v);

    }

    private void displaySubjects()
    {
        pd.show();
        db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<String> arrayList = new ArrayList<>();

                        if(queryDocumentSnapshots.isEmpty())
                        {
                            pd.dismiss();
                            Toast.makeText(facultyProfile.this, "Nothing to show", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            for(QueryDocumentSnapshot qds: queryDocumentSnapshots){

                                arrayList.add(qds.getId());
                                //  Toast.makeText(facultyProfile.this, qds.getId()+"", Toast.LENGTH_SHORT).show();
                            }

                            ArrayAdapter adapter = new ArrayAdapter(facultyProfile.this,android.R.layout.simple_list_item_1,arrayList);
                            lvf.setAdapter(adapter);
                            pd.dismiss();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(facultyProfile.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.subjects_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.delete_subject:
                finish();
                startActivity(new Intent(facultyProfile.this,deleteSubjects.class));
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(facultyProfile.this,facultyLLogin.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
