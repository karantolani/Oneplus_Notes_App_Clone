package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.factor.bouncy.BouncyNestedScrollView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectedItemsCountCallBack {

    List<NoteInfo> noteInfoList;
    RecyclerView notesRV;
    NotesAdapter notesRVAdapter;
    RelativeLayout selectionModeMenu;
    DBHandler dbHandler;
    FloatingActionButton addNoteFab;
    BouncyNestedScrollView noNoteLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ImageButton searchBtn = findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        dbHandler = new DBHandler(this);
        noteInfoList = getData();

        selectionModeMenu = findViewById(R.id.selection_mode_menu);
        noNoteLayout = findViewById(R.id.no_note_layout);
        noNoteLayout.setBindSpringToParent(true);

        notesRV = findViewById(R.id.recycler_notes);
        notesRVAdapter = new NotesAdapter(noteInfoList, getApplicationContext(), this);
        notesRV.setAdapter(notesRVAdapter);
        notesRV.setLayoutManager(new LinearLayoutManager(this));

        if(noteInfoList.size() == 0){
            noNoteLayout.setVisibility(View.VISIBLE);
            notesRV.setVisibility(View.GONE);
        }

        addNoteFab = findViewById(R.id.floating_action_button);

        addNoteFab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            intent.putExtra("id", -1);
            startActivity(intent);
        });

        MaterialButton deleteBtn = findViewById(R.id.btn_delete);
        deleteBtn.setOnClickListener(view -> deleteBtnClick());


    }

    @Override
    protected void onResume() {
        super.onResume();

        noteInfoList.clear();
        noteInfoList.addAll(getData());

        if(noteInfoList.size() == 0){
            noNoteLayout.setVisibility(View.VISIBLE);
            notesRV.setVisibility(View.GONE);
        }else{
            noNoteLayout.setVisibility(View.GONE);
            notesRV.setVisibility(View.VISIBLE);
        }

        notesRVAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        if (notesRVAdapter.selectedItems.size() > 0){
            notesRVAdapter.clearSelectedItems();
            notesRVAdapter.notifyDataSetChanged();
        }else{
            super.onBackPressed();
        }
    }

    private List<NoteInfo> getData() {
        List<NoteInfo> list = new ArrayList<>(dbHandler.getNotesInfo());

        return list;
    }


    @Override
    public void onSelectedItemsCountChanged(int count) {
        ViewGroup parent = findViewById(R.id.parent);

        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(375);
        transition.addTarget(R.id.selection_mode_menu);

        if (count > 0) {
            addNoteFab.hide();

            TransitionManager.beginDelayedTransition(parent, transition);
            selectionModeMenu.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(parent, transition);

            selectionModeMenu.setVisibility(View.GONE);
            addNoteFab.show();
        }
    }

    public void deleteBtnClick(){

        new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
                .setTitle("Reminder")
                .setMessage("Selected Notes will be deleted")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHandler.deleteRowsWithId(notesRVAdapter.selectedItems);
                        notesRVAdapter.deleteSelectedItems();

                        if(noteInfoList.size() == 0){
                            noNoteLayout.setVisibility(View.VISIBLE);
                            notesRV.setVisibility(View.GONE);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();

    }
}