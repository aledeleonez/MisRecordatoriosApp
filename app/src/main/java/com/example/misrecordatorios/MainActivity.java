package com.example.misrecordatorios;

import static com.example.misrecordatorios.MainActivity.Estado.VER_HECHAS;
import static com.example.misrecordatorios.MainActivity.Estado.VER_PENDIENTES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Tarea> tareas;
    private TareaAdaptador adaptador;
    public enum Estado {
        VER_HECHAS, VER_PENDIENTES
    }
    private Estado estado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btAnadir = findViewById(R.id.btAnadir);
        btAnadir.setOnClickListener(this);
        Button btHechas = findViewById(R.id.btHechas);
        btHechas.setOnClickListener(this);
        Button btPendientes = findViewById(R.id.btPendientes);
        btPendientes.setOnClickListener(this);

        tareas = new ArrayList<>();
        ListView lvTareas = findViewById(R.id.lvTareas);
    }

    @Override
    protected void onResume() {
        super.onResume();

        BaseDatos db = new BaseDatos(this);
        tareas.clear();
        tareas.addAll(db.getTareas());
        adaptador.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btAnadir:
                EditText etTarea = findViewById(R.id.etTarea);
                String nombre = etTarea.getText().toString();
                if (nombre.equals("")) {
                    Toast.makeText(this, R.string.mensaje_texto, Toast.LENGTH_SHORT).show();
                    return;
                }

                Tarea tarea = new Tarea(nombre);

                BaseDatos db = new BaseDatos(this);
                db.nuevaTarea(tarea);

                if (estado == VER_PENDIENTES) {
                    tareas.clear();
                    tareas.addAll(db.getTareasPendientes());
                    adaptador.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, R.string.mTareaAnadidaPendientes,
                            Toast.LENGTH_SHORT).show();
                }
                etTarea.setText("");
                etTarea.requestFocus();
                break;
            case R.id.btHechas:
                // Actualiza la lista de tareas vistas
                tareas.clear();
                db = new BaseDatos(this);
                tareas.addAll(db.getTareasHechas());
                adaptador.notifyDataSetChanged();
                estado = VER_HECHAS;
                cambiarEstadoBotones();
                break;
            case R.id.btPendientes:
                // Actualiza la lista de tareas vistas
                tareas.clear();
                db = new BaseDatos(this);
                tareas.addAll(db.getTareasPendientes());
                adaptador.notifyDataSetChanged();
                estado = VER_PENDIENTES;
                cambiarEstadoBotones();
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int posicion = menuInfo.position;

        switch (item.getItemId()) {
            case R.id.itemEliminar:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Estás seguro?")
                        .setTitle("Eliminar tarea")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Tarea tareaEliminada = tareas.remove(posicion);
                                BaseDatos db = new BaseDatos(getApplicationContext());
                                db.eliminarTarea(tareaEliminada);
                                adaptador.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                return true;
            case R.id.itemHecho:
                Tarea tarea = tareas.get(posicion);
                tarea.hacer();
                BaseDatos db = new BaseDatos(this);
                db.modificarTarea(tarea);
                adaptador.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void cambiarEstadoBotones() {
        Button btHechas = findViewById(R.id.btHechas);
        Button btPendientes = findViewById(R.id.btPendientes);
        switch (estado) {
            case VER_HECHAS:
                btHechas.setBackgroundColor(Color.BLACK);
                btHechas.setTextColor(Color.WHITE);
                btPendientes.setBackgroundColor(Color.GRAY);
                btPendientes.setTextColor(Color.BLACK);
                break;
            case VER_PENDIENTES:
                btPendientes.setBackgroundColor(Color.BLACK);
                btPendientes.setTextColor(Color.WHITE);
                btHechas.setBackgroundColor(Color.GRAY);
                btHechas.setTextColor(Color.BLACK);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        switch (estado) {
            case VER_HECHAS:
                getMenuInflater().inflate(R.menu.menu_contextual_hechas, menu);
                break;
            case VER_PENDIENTES:
                getMenuInflater().inflate(R.menu.menu_contextual_pendientes, menu);
                break;
        }
    }
}