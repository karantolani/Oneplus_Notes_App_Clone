package com.example.notes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView noteTitle, noteDate, noteContent;
    AppCompatCheckBox checkBox;
    View view;
    boolean isSelected;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        noteTitle = itemView.findViewById(R.id.text_note_title);
        noteDate = itemView.findViewById(R.id.text_note_date);
        noteContent = itemView.findViewById(R.id.text_note_content);
        checkBox = itemView.findViewById(R.id.checkbox);
        view = itemView;

        isSelected = false;

    }
}
