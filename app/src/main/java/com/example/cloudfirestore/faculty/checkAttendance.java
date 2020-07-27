package com.example.cloudfirestore.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    TextView tv,tv2;
    EditText et1,et2;
    long a,b;
    Button btn;
    View rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);

        tv = findViewById(R.id.text_view2);
        tv2 = findViewById(R.id.tv2);
        rv = findViewById(R.id.rv);
        et1 = findViewById(R.id.below_edittext);
        et2 = findViewById(R.id.above_edittext);
        btn = findViewById(R.id.find_attendance);

        subject = Objects.requireNonNull(getIntent().getExtras()).getString("subject");
        lvca = findViewById(R.id.lvca);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty()))
                    checkAttendances();
                else
                    Toast.makeText(checkAttendance.this,"Attendance percentage can not be null.",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void checkAttendances(){
        a = Long.parseLong(et1.getText().toString().trim());
        b = Long.parseLong(et2.getText().toString().trim());

        tv2.setText(" Students having attendance below "+a+"% and above "+b+"% are listed below:");
        if(a>100)
            et1.setText("100");
        if(b>100)
            et2.setText("100");

        mref = db.collection("faculty").document(firebaseAuth.getUid()).collection("subjects").document(subject)
                .collection("Date");
        mref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                int total = queryDocumentSnapshots.size();
                tv.setText(" Total attendance till today : "+total);

                Map<String,Integer> map = new HashMap<>();

                for(QueryDocumentSnapshot qs : queryDocumentSnapshots){

                    Map<String,Object> map1 = new HashMap<>();
                    map1 = qs.getData();

                    for (Map.Entry<String , Object> entry : map1.entrySet()) {
                        String key = entry.getKey();
                        String value = (String) entry.getValue();

                        if(map.get(key) != null) {

                            int a = map.get(key);

                            if(value.equals("P"))
                                map.put(key,a+1);
                        }
                        else{

                            if(value.equals("P"))
                                map.put(key,1);
                            else
                                map.put(key,0);
                        }
                    }
                }

                ArrayList<student> arrayList = new ArrayList<>();

                for(Map.Entry<String,Integer> entry : map.entrySet()){
                    long percentage = entry.getValue()*100/total;
                    if(percentage<=a && percentage >=b)
                        arrayList.add(new student(entry.getKey(),entry.getValue()+" = "+percentage+"%"));
                }
                studentsNameAdapter adapter = new studentsNameAdapter(checkAttendance.this,arrayList);
                lvca.setAdapter(adapter);
            }
        });
        lvca.setEmptyView(rv);
    }
}
