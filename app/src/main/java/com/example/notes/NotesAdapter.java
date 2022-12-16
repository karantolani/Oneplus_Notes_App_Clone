package com.example.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    List<NoteInfo> noteInfoList = Collections.emptyList();
    Context context;
    boolean isSelectionMode;
    public HashSet<Integer> selectedItems;
    SelectedItemsCountCallBack selItemsCallBack;

    public NotesAdapter(List<NoteInfo> data, Context context, SelectedItemsCountCallBack callBack) {
        this.noteInfoList = data;
        this.context = context;
        this.isSelectionMode = false;
        this.selectedItems = new HashSet<Integer>();
        this.selItemsCallBack = callBack;
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
        final int index = holder.getAdapterPosition();

        holder.noteTitle.setText(noteInfoList.get(holder.getAdapterPosition()).title);
        try {
            holder.noteDate.setText(Utils.formatDate(noteInfoList.get(holder.getAdapterPosition()).date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(!isSelectionMode){
            holder.checkBox.setVisibility(View.INVISIBLE);
            holder.checkBox.setChecked(false);

            holder.isSelected = false;

            ((MaterialCardView) holder.view).setCardBackgroundColor(ContextCompat.getColor(holder.view.getContext(), R.color.card_bg_color));
        }


        holder.view.setOnClickListener(view -> {

            if (!isSelectionMode) {
                Intent intent = new Intent(view.getContext(), AddNoteActivity.class);
                intent.putExtra("id", noteInfoList.get(holder.getAdapterPosition()).id);

                view.getContext().startActivity(intent);
            } else {
                if (!holder.isSelected){
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.checkBox.setChecked(true);

                    holder.isSelected = true;
                    selectedItems.add(noteInfoList.get(holder.getAdapterPosition()).id);
                    selItemsCallBack.onSelectedItemsCountChanged(selectedItems.size());

                    ((MaterialCardView) holder.view).setCardBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.ripple_color));
                }
                else{
                    holder.checkBox.setVisibility(View.INVISIBLE);
                    holder.checkBox.setChecked(false);

                    holder.isSelected = false;

                    ((MaterialCardView) holder.view).setCardBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.card_bg_color));

                    selectedItems.remove(noteInfoList.get(holder.getAdapterPosition()).id);
                    selItemsCallBack.onSelectedItemsCountChanged(selectedItems.size());

                    if(selectedItems.size() == 0)
                        isSelectionMode = false;
                }

            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (!holder.isSelected) {

                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.checkBox.setChecked(true);

                    holder.isSelected = true;

                    ((MaterialCardView) holder.view).setCardBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.ripple_color));

                    isSelectionMode = true;
                    selectedItems.add(noteInfoList.get(holder.getAdapterPosition()).id);
                    selItemsCallBack.onSelectedItemsCountChanged(selectedItems.size());
                }

                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return noteInfoList.size();
    }

    public void deleteSelectedItems() {

        for(int selId: selectedItems){
            int pos = noteInfoList.indexOf(new NoteInfo(selId));
            noteInfoList.remove(pos);
            notifyItemRemoved(pos);
        }

        selectedItems.clear();
        isSelectionMode = false;

        selItemsCallBack.onSelectedItemsCountChanged(selectedItems.size());
    }

    public void clearSelectedItems(){
        selectedItems.clear();
        isSelectionMode = false;

        selItemsCallBack.onSelectedItemsCountChanged(selectedItems.size());
    }
}
