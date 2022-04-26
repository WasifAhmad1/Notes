package com.example.notes;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class MyViewHolder extends RecyclerView.ViewHolder{

    TextView title;
    TextView time;
    TextView desc;

    public MyViewHolder(@NonNull View view) {

        super(view);
        title = view.findViewById(R.id.title);
        time = view.findViewById(R.id.time);
        desc = view.findViewById(R.id.desc);




    }


}
