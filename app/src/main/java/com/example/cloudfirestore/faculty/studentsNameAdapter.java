package com.example.cloudfirestore.faculty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cloudfirestore.R;

import java.util.ArrayList;

public class studentsNameAdapter extends ArrayAdapter<student> {

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_list_item, parent,false);
        }

        student stud = getItem(position);
        TextView tv1 = listItemView.findViewById(R.id.student_name);
        tv1.setText(stud.getName());

        TextView tv2 = listItemView.findViewById(R.id.attendance);
        tv2.setText(stud.getAttendance());

        return listItemView;
    }

    public studentsNameAdapter(@NonNull Context context, ArrayList<student> studentArray) {
        super(context,0,studentArray);



    }
}
