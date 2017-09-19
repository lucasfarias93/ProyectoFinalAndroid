package com.example.lfarias.actasdigitales.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.lfarias.actasdigitales.Entities.Usuarios;

/**
 * Created by lfarias on 9/8/17.
 */

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ActasDigitales.db";
    private static final String TABLE_NAME_DETAILS = "users";
    private static final String ID = "id";
    private static final String ID_USER = "iduser";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_CONTRASEÑA = "contraseña";
    private static final String COLUMN_ID_TRAMITE = "id_tramite";
    private static final String COLUMN_DNI = "dni";
    private static final String COLUMN_NOMBRES = "nombres";
    private static final String COLUMN_APELLIDO = "apellido";
    private static final String COLUMN_EMAIL = "email";
    SQLiteDatabase db;
    Context context;

    private static final String TABLE_CREATE_DETAILS = "create table users (id integer primary key not null , " +
            "iduser text not null , login text not null , contraseña text not null , " +
            "id_tramite text not null , dni text not null , nombres text not null, " +
            "apellido text not null, email text not null);";

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_DETAILS);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DETAILS);
        onCreate(db);
    }

    public void insertUser(Usuarios mData) {
        db = this.getWritableDatabase();
        String id = Integer.toString(mData.getId());
        String login = mData.getLogin();
        String contraseña = mData.getContraseña();
        String nombres = mData.getNombres();
        String apellido = mData.getApellido();
        String email = mData.getEmail();
        String idTramite = mData.getIdTramite();
        String dni = mData.getDni();
        ContentValues values = new ContentValues();
        String query = " select * from users ";
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();

        values.put(ID, count);
        values.put(ID_USER, id);
        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_CONTRASEÑA,contraseña);
        values.put(COLUMN_NOMBRES, nombres);
        values.put(COLUMN_APELLIDO,apellido);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ID_TRAMITE, idTramite);
        values.put(COLUMN_DNI, dni);

        db.insert(TABLE_NAME_DETAILS, null , values);
        cursor.close();
        db.close();
    }

    public Usuarios getUserByUser(String usuarioLogin) {
        Usuarios user = new Usuarios();
        db = this.getReadableDatabase();
        String query = " select * from users where login = '"+usuarioLogin+"'";
        Cursor cursor = db.rawQuery(query, null);
        String a;
        if (cursor.moveToLast()) {
                a = cursor.getString(2);
                if (a.equals(usuarioLogin)) {
                    user.setId(cursor.getInt(0));
                    user.setLogin(cursor.getString(2));
                    user.setContraseña(cursor.getString(3));
                    user.setNombres(cursor.getString(6));
                    user.setApellido(cursor.getString(7));
                    user.setEmail(cursor.getString(8));
                    user.setIdTramite(cursor.getString(4));
                    user.setDni(cursor.getString(5));
                } else {
                    Toast.makeText(context, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                }
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public Usuarios getUserByEmail(String usuarioEmail) {
        Usuarios user = new Usuarios();
        db = this.getReadableDatabase();
        String query = " select * from users where email = '"+usuarioEmail+"'";
        Cursor cursor = db.rawQuery(query, null);
        String a;
        if (cursor.moveToFirst()) {
            a = cursor.getString(8);
            if (a.equals(usuarioEmail)) {
                user.setId(cursor.getInt(0));
                user.setLogin(cursor.getString(2));
                user.setContraseña(cursor.getString(3));
                user.setNombres(cursor.getString(6));
                user.setApellido(cursor.getString(7));
                user.setEmail(cursor.getString(8));
                user.setIdTramite(cursor.getString(4));
                user.setDni(cursor.getString(5));
            } else {
                Toast.makeText(context, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public void onUpdate(Usuarios u, String email) {
        db = this.getWritableDatabase();
        String query = "select email from " + TABLE_NAME_DETAILS;
        Cursor cursor = db.rawQuery(query, null);
        String a;
        int id = cursor.getCount();
        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);
                if (a.equals(email)) {
                    ContentValues valores = new ContentValues();
                    valores.put(ID_USER,u.getId());
                    valores.put(COLUMN_LOGIN,u.getLogin());
                    valores.put(COLUMN_CONTRASEÑA,u.getContraseña());
                    valores.put(COLUMN_NOMBRES, u.getNombres());
                    valores.put(COLUMN_APELLIDO, u.getApellido());
                    valores.put(COLUMN_EMAIL, u.getEmail());
                    valores.put(COLUMN_ID_TRAMITE, u.getIdTramite());
                    valores.put(COLUMN_DNI, u.getDni());

                    insertUser(u);
                    db.close();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

   /* public void updateUser(Usuarios users, String email){
        ContentValues valores = new ContentValues();



        db.update("users", valores, "iduser ="+users.getId(), null);

    }
    /*public List<PersistentMovieData> getFavoriteMovies() {
        List <PersistentMovieData> movies = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = " select * from " + TABLE_NAME_DETAILS;
        Cursor cursor = db.rawQuery(query, null);
        if( cursor != null && cursor.moveToFirst() ) {
            do{
                PersistentMovieData movieObject = new PersistentMovieData();
                movieObject.setId(cursor.getInt(1));
                movieObject.setTitle(cursor.getString(2));
                movieObject.setOverview(cursor.getString(3));
                movieObject.setBackdrop_path(cursor.getString(4));
                movieObject.setVote_average(cursor.getDouble(5));
                movieObject.setRelease_date(cursor.getString(6));
                movieObject.setPoster_path(cursor.getString(7));
                movies.add(movieObject);
            }  while (cursor.moveToNext());
            cursor.close();
        }
        return movies;
    }*/
}
