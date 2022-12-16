package com.example.notes;

public class NoteInfo extends Object{
    String title, date;
    int id;

    NoteInfo(int id, String title, String date){
        this.id = id;
        this.title = title;
        this.date = date;
    }

    NoteInfo(int id){
        this.id = id;
        this.title = "";
        this.date = "";
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof NoteInfo){
            NoteInfo p = (NoteInfo) o;
            return this.id == p.id;
        } else
            return false;
    }
}
