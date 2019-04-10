package com.upgrad.internshipapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

public class TagsHelper extends SQLiteOpenHelper {

    private static final String database="tags.db";
    private static final String TAGS_TABLE="tags";
    private static final String ID="id";
    private static final String NAME="name";
    private static final String COUNT="count";
    private static final String SELECTED="selected";
    Context context;

    public TagsHelper(Context context) {
        super(context, database, null, 1);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TAGS_TABLE+" (id INTEGER PRIMARY KEY NOT NULL, name TEXT, count TEXT, selected TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TAGS_TABLE);
        onCreate(db);
    }
    public long addTag(Tags tag){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(NAME,tag.getName());
        values.put(COUNT,tag.getCount());

        values.put(SELECTED,tag.getSelected());



        return db.insert(TAGS_TABLE,null,values);
    }
    public ArrayList<Tags> getAllTags(){
        ArrayList<Tags> tagitems=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        // db.execSQL("create table if not exists "+FEEDS_TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, sender TEXT, senderclass TEXT,msg TEXT, dtime TEXT, type TEXT, img TEXT, pdf TEXT, pdfdown TEXT, show TEXT)");

        Cursor cursor=db.rawQuery("Select * from "+TAGS_TABLE+" ORDER BY id DESC",null);
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                String name=cursor.getString(cursor.getColumnIndex(NAME));
                String count=cursor.getString(cursor.getColumnIndex(COUNT));
                String sel=cursor.getString(cursor.getColumnIndex(SELECTED));



                tagitems.add(new Tags(name,count,sel));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return tagitems;
    }
    public long setSelected(Tags tags){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SELECTED,"YES");
        return db.update(TAGS_TABLE,values,"name=?",new String[]{tags.getName()});
    }
    public long setUnSelected(Tags tags){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SELECTED,"NO");
        return db.update(TAGS_TABLE,values,"name=?",new String[]{tags.getName()});
    }
    public void drop(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TAGS_TABLE);
        db.execSQL("create table if not exists "+TAGS_TABLE+" (id INTEGER PRIMARY KEY NOT NULL, name TEXT, count TEXT, selected TEXT)");

    }
}
