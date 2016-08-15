package com.YozziBeens.rivostaxi.modelo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table LUGAR.
 */
public class Lugar {

    private Long lugarID;
    /** Not-null value. */
    private String nombre;
    private double latitud;
    private double longitud;
    /** Not-null value. */
    private String imagen;
    /** Not-null value. */
    private String direccion;

    public Lugar() {
    }

    public Lugar(Long lugarID) {
        this.lugarID = lugarID;
    }

    public Lugar(Long lugarID, String nombre, double latitud, double longitud, String imagen, String direccion) {
        this.lugarID = lugarID;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.imagen = imagen;
        this.direccion = direccion;
    }

    public Long getLugarID() {
        return lugarID;
    }

    public void setLugarID(Long lugarID) {
        this.lugarID = lugarID;
    }

    /** Not-null value. */
    public String getNombre() {
        return nombre;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /** Not-null value. */
    public String getImagen() {
        return imagen;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /** Not-null value. */
    public String getDireccion() {
        return direccion;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
