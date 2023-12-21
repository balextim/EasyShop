package com.example.easyshop.modelo;

import java.util.ArrayList;
import java.util.Date;

public class Factura {
    int idFactura=0;
    String nombreFactura="ejemplo nombre factura", fecha="01/10/2000";
    ArrayList<LineaFactura> lineasFactura=new ArrayList<>();
    double totalApagar=0.0;
    public Factura() {

    }

    public Factura(int idFactura,String nombreFactura, String fecha, ArrayList<LineaFactura> lineaFactura) {
        this.idFactura = idFactura;
        this.nombreFactura=nombreFactura;
        this.fecha=fecha;
        this.lineasFactura = lineaFactura;
        this.totalApagar=obtenerTotalFactura();
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public ArrayList<LineaFactura> getLineaFactura() {
        return lineasFactura;
    }

    public void setLineaFactura(ArrayList<LineaFactura> lineaFactura) {
        this.lineasFactura = lineaFactura;
    }

    public double getTotalApagar() {
        return totalApagar;
    }

    public void setTotalApagar(double totalApagar) {
        this.totalApagar = totalApagar;
    }

    private double obtenerTotalFactura(){
        double precioTotal=0;
        if(lineasFactura.size()>0 || lineasFactura !=null){
            for(int i=0; i< lineasFactura.size(); i++){
                LineaFactura lineaFactura=lineasFactura.get(i);
                precioTotal=precioTotal+lineaFactura.getPrecioTotal();
            }
        }
        return precioTotal;
    }

    public String getNombreFactura() {
        return nombreFactura;
    }

    public void setNombreFactura(String nombreFactura) {
        this.nombreFactura = nombreFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
