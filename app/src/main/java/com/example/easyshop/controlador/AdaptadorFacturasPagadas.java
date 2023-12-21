package com.example.easyshop.controlador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyshop.R;
import com.example.easyshop.modelo.Factura;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorFacturasPagadas extends RecyclerView.Adapter<AdaptadorFacturasPagadas.ViewHolder> {

    private List<Factura> listaFacturas = new ArrayList<>();
    private OnItemClickListener listenerClick;

    public AdaptadorFacturasPagadas(List<Factura> listaFacturas){
        this.listaFacturas=listaFacturas;
    }

    public interface OnItemClickListener {
        void onItemClick(String id);
    }
    public void setOnitemClickListener(OnItemClickListener listener){
        listenerClick=listener;
    }
    @NonNull
    @Override
    public AdaptadorFacturasPagadas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.facturas_pagadas_card,parent,false);
        return new AdaptadorFacturasPagadas.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorFacturasPagadas.ViewHolder holder, int position) {
        Factura factura=listaFacturas.get(position);
        holder.idFactura.setText(String.valueOf(factura.getIdFactura()));
        holder.nombre.setText(factura.getNombreFactura());
        holder.fecha.setText(String.valueOf(factura.getFecha()));
        holder.precioTotal.setText(String.valueOf(factura.getTotalApagar())+"€");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listenerClick != null){
                    listenerClick.onItemClick(holder.idFactura.getText().toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaFacturas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idFactura, nombre, fecha, precioTotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idFactura=itemView.findViewById(R.id.idFacturaPagadaCard);
            nombre=itemView.findViewById(R.id.establecimientoFacturaCard);
            fecha=itemView.findViewById(R.id.fechaFacturaCard);
            precioTotal=itemView.findViewById(R.id.precioTotalFacturaCard);
        }
    }
}
