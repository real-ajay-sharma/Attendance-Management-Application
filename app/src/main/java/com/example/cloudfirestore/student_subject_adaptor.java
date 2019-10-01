package com.example.cloudfirestore;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class student_subject_adaptor extends ArrayAdapter<student_subject> {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DocumentReference mref;

    public student_subject_adaptor(@NonNull Context context, ArrayList<student_subject> arrayList) {

        super(context, 0,arrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.student_subject_list_item, parent,false);

        final student_subject obj = getItem(position);

        TextView status = listItemView.findViewById(R.id.status);
        status.setText("Status: "+obj.getStatus());



        TextView percntage = listItemView.findViewById(R.id.attendance_percentage);
        percntage.setText(obj.getPercentage());

        TextView attendance_measurement = listItemView.findViewById(R.id.attendance_measurement);
        attendance_measurement.setText("Attendance :"+obj.getAttended_class()+"/"+obj.getTotal_class());

        TextView subject_name = listItemView.findViewById(R.id.subject_name);
        subject_name.setText(obj.getSubject());

        mref =  db.collection("students").document(firebaseAuth.getUid()).collection("subjects")
                .document(obj.getSubject());

        final ImageView done = listItemView.findViewById(R.id.done_image);
        final View finalListItemView = listItemView;
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                done.setVisibility(View.INVISIBLE);
                mref =  db.collection("students").document(firebaseAuth.getUid()).collection("subjects")
                        .document(obj.getSubject());
               mref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Long total_class = documentSnapshot.getLong("total class");
                        Long attended_class = documentSnapshot.getLong("attended class");


                        Map<String,Object> map = new HashMap<>();
                        map.put("total class",total_class+ 1L);
                        map.put("attended class",attended_class+1L);

                        Long attendance_percentage = (attended_class+1)*100/(total_class+1);

                        if(attendance_percentage >= 75) {
                            map.put("status", "On track, you may leave next " + (4 * attended_class - 3 * total_class + 1) / 3 + " classes.");
                        }else {
                            map.put("status", "Attend next " + (-4 * attended_class + 3 * total_class - 1) + " classes to get back on track.");
                        }
                        map.put("attendance percentage",attendance_percentage+"%");

                        mref.set(map, SetOptions.merge());
                        done.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        final ImageView clear = listItemView.findViewById(R.id.clear_image);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clear.setVisibility(View.INVISIBLE);
                mref =  db.collection("students").document(firebaseAuth.getUid()).collection("subjects")
                        .document(obj.getSubject());
                mref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Long total_class = documentSnapshot.getLong("total class");
                        Long attended_class = documentSnapshot.getLong("attended class");

                        Map<String,Object> map = new HashMap<>();
                        map.put("total class",total_class+ 1L);
                        Long attendance_percentage = (attended_class)*100/(total_class+1);
                        Long next_attendance_percentage = (attended_class)*100/(total_class+2);
                        map.put("attendance percentage",attendance_percentage+"%");

                        if(attendance_percentage < 75) {
                            map.put("color", "r");
                            map.put("status", "Attend next " + (-4 * attended_class + 3 * total_class + 3) + " classes to get back on track.");
                        }else {
                            map.put("color", "g");
                            map.put("status", "On track, you may leave next " + ((4 * attended_class - 3 * total_class - 3) / 3) + " classes.");
                        }
                        mref.set(map, SetOptions.merge());
                        clear.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        return listItemView;
    }
}
