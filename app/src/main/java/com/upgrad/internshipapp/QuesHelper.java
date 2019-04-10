package com.upgrad.internshipapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class QuesHelper extends SQLiteOpenHelper {

    private static final String database="ques.db";
    private static final String QUES_TABLE="ques";
    private static final String ID="id";

    private static final String NAME="title";
    private static final String DTIME="dat";
    private static final String LINK="link";
    private static final String OWNER="own";
    private static final String OPIC="opic";
    private static final String TAGS="tags";

    Context context;

    public QuesHelper(Context context) {
        super(context, database, null, 1);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+QUES_TABLE+" (id INTEGER PRIMARY KEY NOT NULL, title TEXT, link TEXT, dat TEXT, own TEXT, opic TEXT, tags TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+QUES_TABLE);
        onCreate(db);
    }
    public long addTag(Questions ques){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(NAME,ques.getTitle());
        values.put(LINK,ques.getLink());
        values.put(DTIME,ques.getDat());
        values.put(OWNER,ques.getOname());
        values.put(OPIC,ques.getOimg());
        values.put(ID,Integer.parseInt(ques.getId()));
        values.put(TAGS,ques.getTags());



        return db.insert(QUES_TABLE,null,values);
    }
    public ArrayList<Questions> getAllQues(){
        ArrayList<Questions> questionsArrayList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        // db.execSQL("create table if not exists "+FEEDS_TABLE+" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, sender TEXT, senderclass TEXT,msg TEXT, dtime TEXT, type TEXT, img TEXT, pdf TEXT, pdfdown TEXT, show TEXT)");

        Cursor cursor=db.rawQuery("Select * from "+QUES_TABLE+" ORDER BY id DESC",null);
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                String name=cursor.getString(cursor.getColumnIndex(NAME));
                String id=cursor.getInt(cursor.getColumnIndex(ID))+"";
                String link=cursor.getString(cursor.getColumnIndex(LINK));
                String dat=cursor.getString(cursor.getColumnIndex(DTIME));
                String cwn=cursor.getString(cursor.getColumnIndex(OWNER));
                String opic=cursor.getString(cursor.getColumnIndex(OPIC));
                String tag=cursor.getString(cursor.getColumnIndex(TAGS));



                questionsArrayList.add(new Questions(id,name,link,dat,cwn,opic,tag));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return questionsArrayList;
    }

    public void drop(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + QUES_TABLE);
        db.execSQL("create table if not exists "+QUES_TABLE+" (id INTEGER PRIMARY KEY NOT NULL, title TEXT, link TEXT, dat TEXT, own TEXT, opic TEXT, tags TEXT) ");

    }
}
