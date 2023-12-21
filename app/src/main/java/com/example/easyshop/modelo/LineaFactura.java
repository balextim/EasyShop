package com.example.easyshop.modelo;

public class LineaFactura {
    private int id=0, unidades=0;
    String nombre="Ejemplo Nombre";
    double precio=0.0, precioTotal=0.0;
    public LineaFactura() {
    }

    public LineaFactura(int id, int unidades, String nombre, double precio, double precioTotal) {
        this.id = id;
        this.unidades = unidades;
        this.nombre = nombre;
        this.precio = precio;
        this.precioTotal = precioTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
