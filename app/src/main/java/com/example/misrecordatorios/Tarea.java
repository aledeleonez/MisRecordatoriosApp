package com.example.misrecordatorios;

public class Tarea {

    private long id;
    private String nombre;
    private boolean hecha;

    public Tarea(long id, String nombre, boolean hecha) {
        this.id = id;
        this.nombre = nombre;
        this.hecha = hecha;
    }

    public Tarea(String nombre){
        this.nombre = nombre;
        hecha = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void hacer(){ hecha = true; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isHecha() {
        return hecha;
    }

    public void setHecha(boolean hecha) {
        this.hecha = hecha;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
