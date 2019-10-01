package com.example.cloudfirestore.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloudfirestore.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class checkAttendance extends AppCompatActivity {

    ListView lvca;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    CollectionReference mref;
    String subject;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);

        tv = findViewById(R.id.text_view2);

        subject = Objects.requireNonNull(getIntent().getExtras()).getString("subject");
        lvca = findViewById(R.id.lvca);

        mref = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects").document(subject)
        .collection("Date");
        mref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                int total = queryDocumentSnapshots.size();
               tv.setText("Total attendance till today : "+total);

                Map<String,Integer> map = new HashMap<>();

               for(QueryDocumentSnapshot qs : queryDocumentSnapshots){

                    Map<String,Object> map1 = new HashMap<>();
                    map1 = qs.getData();

                   for (Map.Entry<String , Object> entry : map1.entrySet()) {
                       String key = entry.getKey();
                       String value = (String) entry.getValue();

                       if(map.get(key) != null) {

                           int a = map.get(key);

                           if(value.equals("p"))
                               map.put(key,a+1);
                       }
                       else{

                           if(value.equals("p"))
                               map.put(key,1);
                           else
                               map.put(key,0);
                       }
                   }
               }

                ArrayList<student> arrayList = new ArrayList<>();

               for(Map.Entry<String,Integer> entry : map.entrySet()){
                   arrayList.add(new student(entry.getKey(),entry.getValue()+" = "+entry.getValue()*100/total+"%"));
               }
                studentsNameAdapter adapter = new studentsNameAdapter(checkAttendance.this,arrayList);
                               lvca.setAdapter(adapter);
            }
        });
    }
}
