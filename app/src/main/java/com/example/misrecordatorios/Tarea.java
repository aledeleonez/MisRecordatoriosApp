package com.example.misrecordatorios;

import android.provider.BaseColumns;

public class Tarea {
    public static final String BD_NOMBRE = "com.aziflaj.todolist.db";
    public static final int BD_VERSION = 1;

    public class TareaEntrada implements BaseColumns {
        public static final String TABLA = "tareas";
        public static final String COL_TAREA_TITULO = "titulo";
    }
}
