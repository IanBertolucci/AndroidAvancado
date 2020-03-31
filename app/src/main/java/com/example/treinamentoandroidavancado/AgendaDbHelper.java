package com.example.treinamentoandroidavancado;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AgendaDbHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "agenda.db";
    static final int DB_VERSION = 1;
    static final String TABLE = "contato";
    static final String C_ID = "_id";
    static final String C_NOME = "nome";
    static final String C_TELEFONE = "telefone";
    static final String C_FOTO = "foto";

    public AgendaDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE
                + "(" + C_ID + " integer primary key autoincrement, "
                + C_NOME + " text, "
                + C_TELEFONE + " text, "
                + C_FOTO + " blob )";
        try{
            db.execSQL(sql);
        }catch (Exception e) {
            Log.e("Error dbHelper", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            backup(db);
            db.execSQL("drop table if exists " + TABLE);
            onCreate(db);
            restore(db);
        }catch (Exception e) {
            Log.e("Error dbHelper", e.getMessage());
        }
    }

    private List<ContentValues> dados;

    private void backup(SQLiteDatabase db){
        dados = new ArrayList<ContentValues>();
        try{
            Cursor cursor = db.query(TABLE, null, null, null, null, null, null);

            try{
                while(cursor.moveToNext()){
                    String nome = cursor.getString(1);
                    String telefone = cursor.getString(2);
                    String foto = cursor.getString(3);

                    ContentValues values = new ContentValues();
                    values.put(AgendaDbHelper.C_NOME, nome);
                    values.put(AgendaDbHelper.C_TELEFONE, telefone);
                    values.put(AgendaDbHelper.C_FOTO, foto);

                    dados.add(values);
                }
            }finally{
                cursor.close();
            }

            Log.i("DbHelper", "Backup do banco de dados realizado!");
        }catch(Exception e){
            Log.e("Error DbHelper", e.getMessage());
        }
    }

    private void restore(SQLiteDatabase db){
        try{
            for(ContentValues values : dados){
                db.insertOrThrow(AgendaDbHelper.TABLE, null, values);
            }

            Log.i("DbHelper", "Restore do banco de dados realizado!");
        }catch(Exception e){
            Log.e("Error DbHelper", e.getMessage());
        }
    }
}
