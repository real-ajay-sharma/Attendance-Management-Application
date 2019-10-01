package com.example.cloudfirestore.faculty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cloudfirestore.R;

import java.util.ArrayList;

public class studentDeleteAdapter extends ArrayAdapter<student> {

    public studentDeleteAdapter(@NonNull Context context, ArrayList<student> students) {
        super(context, 0, students);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.custom_list_item, parent, false);
        }

        student obj = getItem(position);

        TextView tv = listItemView.findViewById(R.id.student_name);
        tv.setText(obj.getName());

        ImageView iv = listItemView.findViewById(R.id.delete_image);

            iv.setImageResource(obj.getImageId());

        return listItemView;

    }
}
