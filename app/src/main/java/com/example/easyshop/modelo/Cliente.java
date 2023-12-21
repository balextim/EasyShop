package com.example.easyshop.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    String nomnbre="ejemplo", apellido="apellido";
    List<Factura> facturas=new ArrayList<>();
    public Cliente() {
    }

    public Cliente(String nomnbre, String apellido) {
        this.nomnbre = nomnbre;
        this.apellido = apellido;
        this.facturas.add(new Factura());
    }

    public String getNomnbre() {
        return nomnbre;
    }

    public void setNomnbre(String nomnbre) {
        this.nomnbre = nomnbre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }
    public int numFacturas(){
        return facturas.size();
    }
}
