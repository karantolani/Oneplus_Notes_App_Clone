package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.SpannableString;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "notesdb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "notes";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String TITLE_COL = "title";

    // below variable id for our course duration column.
    private static final String CONTENT_COL = "content";

    // below variable for our course description column.
    private static final String DATE_COL = "date";


    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT,"
                + CONTENT_COL + " TEXT,"
                + DATE_COL + " TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    public long addNewNote(String title, String content){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(TITLE_COL, title);
        values.put(CONTENT_COL, content);
        values.put(DATE_COL, currentDateandTime);

        // after adding all values we are passing
        // content values to our table.
        long id = db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();

        return id;
    }

    public ArrayList getNotesInfo(){

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<NoteInfo> noteInfosList = new ArrayList<>();

        Cursor res = db.rawQuery("SELECT "+ID_COL+", "+TITLE_COL+", " + DATE_COL + " FROM "+TABLE_NAME+" ORDER BY datetime(" +
                DATE_COL+") DESC", null);
        res.moveToFirst();

        while (!res.isAfterLast()){
            noteInfosList.add(new NoteInfo(
                    res.getInt(0), res.getString(1), res.getString(2)
            ));
            res.moveToNext();
        }

        return noteInfosList;
    }

    public NoteContent getNoteData(int id){
        String title = "", content = "";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT "+TITLE_COL+", "+CONTENT_COL+" FROM "+TABLE_NAME+" WHERE "+ID_COL+"="+id+" LIMIT 1;", null);

        if(res.moveToFirst()){
            title = res.getString(0);
            content = res.getString(1);
        }

        return new NoteContent(title, content);
    }

    public ArrayList getAllNotesData(){
        ArrayList<SearchNoteInfo> searchNoteInfoList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT "+ID_COL+", "+TITLE_COL+", "+CONTENT_COL+", " + DATE_COL + " FROM "+TABLE_NAME+" ORDER BY datetime(" +
                DATE_COL+") DESC", null);

        res.moveToFirst();

        while(!res.isAfterLast()){
            searchNoteInfoList.add(new SearchNoteInfo(
                    res.getInt(0), new SpannableString(res.getString(1)), new SpannableString(res.getString(2)), res.getString(3)
            ));
            res.moveToNext();
        }

        return searchNoteInfoList;

    }

    public void updateNote(int id, String newTitle, String newContent){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE_COL, newTitle);
        values.put(CONTENT_COL, newContent);
        values.put(DATE_COL, currentDateandTime);

        db.update(TABLE_NAME, values, ID_COL+"=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllRows(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "", null);
        db.close();
    }

    public void deleteRowsWithId(HashSet<Integer> ids){
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = ID_COL + " IN (" + TextUtils.join(",", ids) + ")";
        db.delete(TABLE_NAME, whereClause, null);
        db.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
