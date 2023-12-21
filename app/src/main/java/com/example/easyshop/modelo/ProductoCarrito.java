package com.example.easyshop.modelo;

import android.net.Uri;

import java.io.Serializable;

public class ProductoCarrito implements Serializable {
    int id, unidades;
    transient Uri logo;
    String nombre;
    double precio, precioTotalProductos;

    public ProductoCarrito() {
    }

    public ProductoCarrito(int id, int unidades, Uri logo, String nombre, double precio) {
        this.id = id;
        this.unidades = unidades;
        this.logo = logo;
        this.nombre = nombre;
        this.precio = precio;
        this.precioTotalProductos = this.precio*this.unidades;
    }

    public int getId() {
        return id;
    }

    public int getUnidades() {
        return unidades;
    }

    public Uri getLogo() {
        return logo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public double getPrecioTotalProductos() {
        return precioTotalProductos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCantidad(int cantidad) {
        this.unidades = cantidad;
    }

    public void setLogo(Uri logo) {
        this.logo = logo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setPrecioTotalProductos(double precioTotalProductos) {
        this.precioTotalProductos = precioTotalProductos;
    }
    public void obtenerPrecioTotal(){
        double precioTotal= this.getUnidades()*this.getPrecio();
        this.precioTotalProductos=precioTotal;
    }
    public void aniadirUnidad(){
        this.unidades=this.unidades+1;
        this.obtenerPrecioTotal();
    }
    public void restarUnidad(){
        this.unidades=this.unidades-1;
        this.obtenerPrecioTotal();
    }
}
