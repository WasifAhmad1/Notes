package com.example.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class noteAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private ArrayList<Note> noteList;
    private final MainActivity mainAct;


    public noteAdapter(ArrayList<Note> noteList, MainActivity mainAct) {
        this.noteList = noteList;
        this.mainAct = mainAct;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //in this function we can change the text of our textview
        //we find the position in the array of a note and can get the content
        Date date = null;
        Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.desc.setText(note.getDesc());
        holder.time.setText(note.getDate());
        //holder.time.setText(note.getTimeStamp());

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
