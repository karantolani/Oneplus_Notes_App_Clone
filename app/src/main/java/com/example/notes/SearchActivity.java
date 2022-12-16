package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    DBHandler handler;
    RecyclerView searchNotesRV;
    SearchNoteRVAdapter searchNoteRVAdapter;
    List<SearchNoteInfo> searchNoteInfoList;
    ConstraintLayout layoutNoResults;
    ArrayList<SearchNoteInfo> allNotes;
    ImageButton clearSearch;
    EditText edSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        layoutNoResults = findViewById(R.id.layout_no_results);

        handler = new DBHandler(this);
        clearSearch = findViewById(R.id.btn_clear_search);
        ImageButton backBtn = findViewById(R.id.btn_search_back);
        allNotes = handler.getAllNotesData();

        edSearch = findViewById(R.id.ed_search);
        edSearch.requestFocus();

        searchNoteInfoList = new ArrayList<>();

        searchNotesRV = findViewById(R.id.recycler_snotes);
        searchNoteRVAdapter = new SearchNoteRVAdapter(searchNoteInfoList, getApplicationContext());
        searchNotesRV.setAdapter(searchNoteRVAdapter);
        searchNotesRV.setLayoutManager(new LinearLayoutManager(this));


        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edSearch.getText().length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);

                    searchNoteInfoList.clear();
                    try {
                        searchNoteInfoList.addAll(findNotes(edSearch.getText().toString()));
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                    if (searchNoteInfoList.size() > 0) {
                        searchNotesRV.setVisibility(View.VISIBLE);
                        layoutNoResults.setVisibility(View.GONE);
                    } else {
                        searchNotesRV.setVisibility(View.INVISIBLE);
                        layoutNoResults.setVisibility(View.VISIBLE);
                    }

                    searchNoteRVAdapter.notifyDataSetChanged();

                } else {
                    clearSearch.setVisibility(View.INVISIBLE);

                    layoutNoResults.setVisibility(View.GONE);
                    searchNoteInfoList.clear();
                    searchNoteRVAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        clearSearch.setOnClickListener(view -> edSearch.setText(""));

        backBtn.setOnClickListener(view -> SearchActivity.super.onBackPressed());
    }

    private ArrayList findNotes(String search) throws CloneNotSupportedException {
        ArrayList<SearchNoteInfo> searchedNotes = new ArrayList<>();

        for (SearchNoteInfo noteInfo : allNotes) {


            if (noteInfo.title.toString().indexOf(search) != -1 || noteInfo.content.toString().indexOf(search) != -1) {
                SearchNoteInfo searchNoteInfo = (SearchNoteInfo) noteInfo.clone();

                int tIndex = noteInfo.title.toString().indexOf(search);

                if (tIndex != -1) {
                    searchNoteInfo.title = new SpannableString(noteInfo.title.toString());
                    searchNoteInfo.title.setSpan(new ForegroundColorSpan(Color.rgb(147, 86, 21)), tIndex, tIndex + search.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                int cIndex = noteInfo.content.toString().indexOf(search);

                if (cIndex != -1) {
                    searchNoteInfo.content = new SpannableString(noteInfo.content.toString());
                    searchNoteInfo.content.setSpan(new ForegroundColorSpan(Color.rgb(147, 86, 21)), cIndex, cIndex + search.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    searchNoteInfo.content = new SpannableString("");
                }
                searchedNotes.add(searchNoteInfo);
            }
        }

        return searchedNotes;

    }

    @Override
    protected void onResume() {
        super.onResume();

        allNotes.clear();
        allNotes.addAll(handler.getAllNotesData());

        if (edSearch.getText().length() > 0) {
            clearSearch.setVisibility(View.VISIBLE);

            searchNoteInfoList.clear();
            try {
                searchNoteInfoList.addAll(findNotes(edSearch.getText().toString()));
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            if (searchNoteInfoList.size() > 0) {
                searchNotesRV.setVisibility(View.VISIBLE);
                layoutNoResults.setVisibility(View.GONE);
            } else {
                searchNotesRV.setVisibility(View.INVISIBLE);
                layoutNoResults.setVisibility(View.VISIBLE);
            }

            searchNoteRVAdapter.notifyDataSetChanged();

        } else {
            clearSearch.setVisibility(View.INVISIBLE);

            layoutNoResults.setVisibility(View.GONE);
            searchNoteInfoList.clear();
            searchNoteRVAdapter.notifyDataSetChanged();

        }


    }
}