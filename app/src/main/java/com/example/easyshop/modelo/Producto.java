package com.example.easyshop.modelo;

import android.net.Uri;

public class Producto {
    String nombre,tipo, descripcion, marca;
    Uri logo;
    int stock;
    double precio, peso;

    public Producto() {
    }

    public Producto(String nombre, String tipo, String descripcion, String marca, Uri logo, int stock, double precio, double peso) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.marca = marca;
        this.logo = logo;
        this.stock = stock;
        this.precio = precio;
        this.peso = peso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }


    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Uri getLogo() {
        return logo;
    }

    public void setLogo(Uri logo) {
        this.logo = logo;
    }
}
