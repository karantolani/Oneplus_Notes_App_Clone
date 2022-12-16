package com.example.notes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    private EditText title, content;
    DBHandler dbHandler;
    int id;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ImageButton backBtn = findViewById(R.id.btn_back);
        ImageButton saveBtn = findViewById(R.id.btn_save);
        content = findViewById(R.id.ed_content);
        title = findViewById(R.id.ed_title);

        dbHandler = new DBHandler(this);

        loadNote();

        if(id == -1)
            content.requestFocus();

        backBtn.setOnClickListener(view -> AddNoteActivity.super.onBackPressed());

        saveBtn.setEnabled(content.getText().length() > 0 || title.getText().length() > 0);
        saveBtn.setOnClickListener(view -> {
            String t = title.getText().toString();
            String c = content.getText().toString();

            if (id == -1)
                id = (int) dbHandler.addNewNote(t, c);
            else
                dbHandler.updateNote(id, t, c);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
            
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBtn.setEnabled(content.getText().length() > 0 || title.getText().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBtn.setEnabled(content.getText().length() > 0 || title.getText().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setOnClickListener(view -> content.requestFocus());

    }

    private void loadNote() {

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        if (id == -1) return;

        NoteContent data = dbHandler.getNoteData(id);
        title.setText(data.title);
        content.setText(data.content);
    }


}