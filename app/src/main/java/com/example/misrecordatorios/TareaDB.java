package com.example.misrecordatorios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TareaDB extends SQLiteOpenHelper {

    public TareaDB(Context context){
        super(context, Tarea.BD_NOMBRE, null, Tarea.BD_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase bd) {
        String crearTabla = "CREATE TABLE " + Tarea.TareaEntrada.TABLA + " ( " +
                Tarea.TareaEntrada._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Tarea.TareaEntrada.COL_TAREA_TITULO + " TEXT NOT NULL);";

        bd.execSQL(crearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
        bd.execSQL("DROP TABLE IF EXISTS " + Tarea.TareaEntrada.TABLA);
        onCreate(bd);

    }
}
