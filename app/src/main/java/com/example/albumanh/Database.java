package com.example.albumanh;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //truy van khong tra ket qua: create, insert, update, delete...
    public void QueryData(String sql){
        SQLiteDatabase database= getWritableDatabase();
        database.execSQL(sql);
    }
    //truy van co tra kq: select
    public Cursor GetData(String sql){
        SQLiteDatabase database= getWritableDatabase();//write vhay read deu dc
        return database.rawQuery(sql, null);

    }

    public void insert_dovat(String ten, String mota, byte[]hinh){
        SQLiteDatabase database= getWritableDatabase();
        String sql="INSERT INTO DoVat VALUES(null,?,?,?)";
        SQLiteStatement sqLiteStatement=database.compileStatement(sql);
        sqLiteStatement.clearBindings();

        sqLiteStatement.bindString(1, ten);
        sqLiteStatement.bindString(2, mota);
        sqLiteStatement.bindBlob(3, hinh);

        sqLiteStatement.executeInsert();
    }

    public void insert_hinh(byte[]hinh, String ngay,String tenDB){
        SQLiteDatabase database= getWritableDatabase();
        String sql="INSERT INTO '"+tenDB+"' VALUES(null,?,?)";
        SQLiteStatement sqLiteStatement=database.compileStatement(sql);

        sqLiteStatement.clearBindings();

        sqLiteStatement.bindBlob(1, hinh);
        sqLiteStatement.bindString(2, ngay);

        sqLiteStatement.executeInsert();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
