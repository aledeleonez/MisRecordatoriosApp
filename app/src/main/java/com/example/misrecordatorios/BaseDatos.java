package com.example.misrecordatorios;

import static android.provider.UserDictionary.Words._ID;

import static com.example.misrecordatorios.Constantes.BASEDATOS;
import static com.example.misrecordatorios.Constantes.HECHA;
import static com.example.misrecordatorios.Constantes.NOMBRE;
import static com.example.misrecordatorios.Constantes.TABLA_TAREAS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BaseDatos extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private final String[] SELECT = new String[]{_ID, NOMBRE, HECHA};

    public BaseDatos(Context contexto){
        super(contexto, BASEDATOS, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLA_TAREAS +
                " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NOMBRE + " TEXT," + HECHA + " INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLA_TAREAS);
        onCreate(sqLiteDatabase);
    }

    public void nuevaTarea(Tarea tarea) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(NOMBRE, tarea.getNombre());
        valores.put(HECHA, tarea.isHecha());
        db.insertOrThrow(TABLA_TAREAS, null, valores);
    }

    public ArrayList<Tarea> getLista(Cursor cursor) {
        ArrayList<Tarea> tareas = new ArrayList<>();
        while (cursor.moveToNext()) {
            Tarea tarea = new Tarea(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getInt(2) >= 1);
            tareas.add(tarea);
        }
        return tareas;
    }

    public ArrayList<Tarea> getTareas() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_TAREAS, SELECT, null, null, null, null, NOMBRE);
        return getLista(cursor);
    }

    public ArrayList<Tarea> getTareasHechas() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_TAREAS, SELECT, null, null, null, null, NOMBRE);
        return getLista(cursor);
    }

    public ArrayList<Tarea> getTareasPendientes() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_TAREAS, SELECT, null, null, null, null, NOMBRE);
        return getLista(cursor);
    }

    public ArrayList<Tarea> getTareas(String busqueda) {
        return null;
    }

    public void eliminarTarea(Tarea tareaEliminada) {

    }

    public void modificarTarea(Tarea tarea) {

    }
}
