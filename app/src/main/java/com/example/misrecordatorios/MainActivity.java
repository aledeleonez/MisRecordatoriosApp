package com.example.misrecordatorios;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TareaDB tareaDB;
    private ListView ListaTareas;
    private ArrayAdapter<String> arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tareaDB = new TareaDB(this);
        ListaTareas = (ListView) findViewById(R.id.list_todo);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void borrarTarea(View view){
        View parent = (View) view.getParent();
        TextView tareaTextView = (TextView) parent.findViewById(R.id.titulo_tarea);
        String tarea = String.valueOf(tareaTextView.getText());
        SQLiteDatabase bd = tareaDB.getWritableDatabase();
        bd.delete(Tarea.TareaEntrada.TABLA, Tarea.TareaEntrada.COL_TAREA_TITULO + " = ?", new String[]{tarea});
        bd.close();
        updateUI();
    }

    private void updateUI(){
        ArrayList<String> listaTareas = new ArrayList<>();
        SQLiteDatabase bd = tareaDB.getReadableDatabase();
        Cursor cursor = bd.query(Tarea.TareaEntrada.TABLA, new String[]{Tarea.TareaEntrada._ID, Tarea.TareaEntrada.COL_TAREA_TITULO},
                null, null, null, null, null);
        while(cursor.moveToNext()){
            int idx = cursor.getColumnIndex(Tarea.TareaEntrada.COL_TAREA_TITULO);
            listaTareas.add(cursor.getString(idx));
        }

        if (arrayAdapter == null){
            arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_nueva_tarea, listaTareas);
            ListaTareas.setAdapter(arrayAdapter);
        } else {
            arrayAdapter.clear();
            arrayAdapter.addAll(listaTareas);
            arrayAdapter.notifyDataSetChanged();
        }

        cursor.close();
        bd.close();
    }


}