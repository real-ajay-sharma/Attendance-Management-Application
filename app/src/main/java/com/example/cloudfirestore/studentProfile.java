package com.example.cloudfirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Objects;

import javax.annotation.Nullable;

public class studentProfile extends AppCompatActivity {

    ListView lv;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference mref;
    FloatingActionButton fabs;
    LinearLayout linearLayout;
    ProgressDialog pd ;
    View view;

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("students").document(Objects.requireNonNull(firebaseAuth.getUid())).collection("subjects").
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                        if (e != null)
                            Toast.makeText(studentProfile.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        else {
                            ArrayList<student_subject> arrayList1 = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(queryDocumentSnapshots)) {
                                String subject = document.getId();
                                Map<String, Object> map = new HashMap<>();
                                map = document.getData();
                                String status = (String) map.get("status");
                                String percentage = (String) map.get("attendance percentage");
                                Long total_class = (Long) map.get("total class");
                                Long attended_class = (Long) map.get("attended class");
                                arrayList1.add(new student_subject(status, percentage, total_class, attended_class, subject));
                            }

                            student_subject_adaptor adaptor = new student_subject_adaptor(studentProfile.this, arrayList1);
                            lv.setAdapter(adaptor);
                            Toast.makeText(studentProfile.this, "success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        lv = findViewById(R.id.lvdss);
        view = findViewById(R.id.empty_view);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");


        fabs = findViewById(R.id.fabs);
        fabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(studentProfile.this,addStudentSubject.class));

            }
        });

        display_subjects();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               final student_subject item = (student_subject) parent.getItemAtPosition(position);

                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View customView = layoutInflater.inflate(R.layout.popup,null);

                final PopupWindow popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);

                linearLayout = findViewById(R.id.linear_layout1);

                popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                TextView reset_attendance = customView.findViewById(R.id.reset_attendance);
                reset_attendance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String,Object> map = new HashMap<>();
                        map.put("total class",0);
                        map.put("attended class",0);
                        map.put("attendance percentage","0%");
                        map.put("status","");


                        db.collection("students").document(firebaseAuth.getUid()).collection("subjects")
                                .document(item.getSubject()).set(map);
                        popupWindow.dismiss();
                    }
                });

                TextView delete = customView.findViewById(R.id.delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.collection("students").document(firebaseAuth.getUid()).collection("subjects")
                                .document(item.getSubject()).delete();
                        popupWindow.dismiss();
                    }
                });
            }
        });

        lv.setEmptyView(view);
    }

    private void display_subjects() {

        pd.show();
        db.collection("students").document(firebaseAuth.getUid()).collection("subjects").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            ArrayList<student_subject> arrayList1 = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String subject = document.getId();
                                Map<String, Object> map = new HashMap<>();
                                map = document.getData();
                                String status = (String) map.get("status");
                                String percentage = (String) map.get("attendance percentage");
                                Long total_class = (Long) map.get("total class");
                                Long attended_class = (Long) map.get("attended class");
                                arrayList1.add(new student_subject(status, percentage, total_class, attended_class, subject));
                            }
                            student_subject_adaptor adaptor = new student_subject_adaptor(studentProfile.this, arrayList1);

                            lv.setAdapter(adaptor);
                            pd.dismiss();
                            //Toast.makeText(studentProfile.this, "success", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            pd.dismiss();
                            Toast.makeText(studentProfile.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.student_profile_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.logout1:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(studentProfile.this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
