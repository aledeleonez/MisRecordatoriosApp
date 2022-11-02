package com.example.misrecordatorios;

import static com.example.misrecordatorios.MainActivity.Estado.VER_HECHAS;
import static com.example.misrecordatorios.MainActivity.Estado.VER_PENDIENTES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Tarea> tareas;
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
            case R.id.ivImagen:
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, FOTO_TAREA);
                }
                break;
            default:
                break;
        }

    }
}