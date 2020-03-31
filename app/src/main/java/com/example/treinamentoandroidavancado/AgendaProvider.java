package com.example.treinamentoandroidavancado;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class AgendaProvider extends ContentProvider {
    AgendaDbHelper dbHelper;

    public static final String AUTHORITY = "com.example.androidintermediario";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String ID_PATH = "id/*";

    public static final int CONTATO = 1;
    public static final int BY_ID = 2;

    static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(AUTHORITY, null, CONTATO);
        matcher.addURI(AUTHORITY, ID_PATH, BY_ID);
        matcher.addURI(AUTHORITY, "#", CONTATO);
    }

    public AgendaProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = matcher.match(uri);

        if (match != CONTATO){
            return 0;
        }else {
            return db.delete(AgendaDbHelper.TABLE, selection, selectionArgs);
        }
    }

    @Override
    public String getType(Uri uri) {
        int match = matcher.match(uri);

        if (match == CONTATO){
            return "vnd.android.cursor.dir/vnd.example.androidintermediario";
        }else{
            return "vnd.android.cursor.item/vnd.example.androidintermediario";
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = matcher.match(uri);
        long newId = 0;

        if (match != CONTATO){
            throw new UnsupportedOperationException("Not yet implemented");
        }

        if (values != null){
            newId = db.insert(AgendaDbHelper.TABLE, null, values);
            return Uri.withAppendedPath(uri, String.valueOf(newId));
        }else {
            return null;
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new AgendaDbHelper(getContext());
        if (dbHelper == null){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String order = null;
        Cursor result = null;

        if (sortOrder!=null){
            order = sortOrder;
        }

        int match = matcher.match(uri);

        try {
            switch (match){
                case CONTATO:
                    result = db.query(AgendaDbHelper.TABLE, projection, selection, selectionArgs, null, null, order);
                    break;
                case BY_ID:
                    result = db.query(AgendaDbHelper.TABLE, projection, AgendaDbHelper.C_ID+"=?", new String[]{uri.getLastPathSegment()}, null, null, order);
                    break;
                default:
                    throw new UnsupportedOperationException("Not yet implemented");
            }
        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }

        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = matcher.match(uri);
        int rows = 0;

        if (match != CONTATO){
            throw new UnsupportedOperationException("Not yet implemented");
        }

        if (values != null){
            rows = db.update(AgendaDbHelper.TABLE, values, selection, selectionArgs);
        }

        return rows;
    }
}
