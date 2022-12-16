package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

public class SearchNoteRVAdapter extends RecyclerView.Adapter<NoteViewHolder>{

    List<SearchNoteInfo> searchNoteInfoList;
    Context context;

    public SearchNoteRVAdapter(List<SearchNoteInfo> data, Context context){
        this.searchNoteInfoList = data;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the layout
        View noteView = inflater.inflate(R.layout.card_note, parent, false);

        NoteViewHolder viewHolder = new NoteViewHolder(noteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.noteTitle.setText(searchNoteInfoList.get(holder.getAdapterPosition()).title);
        holder.noteContent.setText(searchNoteInfoList.get(holder.getAdapterPosition()).content);

        try {
            holder.noteDate.setText(Utils.formatDate(searchNoteInfoList.get(holder.getAdapterPosition()).date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNoteActivity.class);
                intent.putExtra("id", searchNoteInfoList.get(holder.getAdapterPosition()).id);

                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchNoteInfoList.size();
    }
}
