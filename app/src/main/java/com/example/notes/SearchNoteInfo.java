package com.example.notes;

import android.text.SpannableString;

import androidx.annotation.NonNull;

public class SearchNoteInfo extends Object implements Cloneable{
    SpannableString title, content;
    String date;
    int id;

    SearchNoteInfo(int id, SpannableString title, SpannableString content, String date){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

//    NoteInfo(int id){
//        this.id = id;
//        this.title = "";
//        this.date = "";
//    }


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof SearchNoteInfo){
            SearchNoteInfo p = (SearchNoteInfo) o;
            return this.id == p.id;
        } else
            return false;
    }
}
