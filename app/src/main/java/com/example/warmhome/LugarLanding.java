package com.example.warmhome;

public class LugarLanding {
    private int imagenFondo;
    private String nombre;
    private String notificacion;

    //Alimentar lista RecyclerView
    public LugarLanding(int imagenFondo, String nombre, String notificacion) {
        this.imagenFondo = imagenFondo;
        this.nombre = nombre;
        this.notificacion = notificacion;
    }

    public int getImagenFondo() {
        return imagenFondo;
    }

    public void setImagenFondo(int imagenFondo) {
        this.imagenFondo = imagenFondo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }
}
