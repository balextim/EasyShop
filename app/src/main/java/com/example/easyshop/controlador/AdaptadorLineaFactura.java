package com.example.easyshop.controlador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyshop.R;
import com.example.easyshop.modelo.LineaFactura;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorLineaFactura extends RecyclerView.Adapter<AdaptadorLineaFactura.ViewHolder> {
    private List<LineaFactura> lineasFactura = new ArrayList<>();
    public AdaptadorLineaFactura(List<LineaFactura> lineasFactura) {
        this.lineasFactura = lineasFactura;
    }

    @NonNull
    @Override
    public AdaptadorLineaFactura.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_factura_card, parent, false);
        return new AdaptadorLineaFactura.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorLineaFactura.ViewHolder holder, int position) {
        LineaFactura lineaFactura=lineasFactura.get(position);
        holder.id.setText(String.valueOf(lineaFactura.getId()));
        holder.nombre.setText(lineaFactura.getNombre());
        holder.precio.setText(String.valueOf(lineaFactura.getPrecio())+"€");
        holder.unidades.setText("X"+String.valueOf(lineaFactura.getUnidades()));
        holder.precioTotal.setText(String.valueOf(lineaFactura.getPrecioTotal())+"€");


    }

    @Override
    public int getItemCount() {
        return lineasFactura.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, nombre, precio, unidades, precioTotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.idLineaFacturaCard);
            nombre=itemView.findViewById(R.id.nombreFacturaCard);
            precio=itemView.findViewById(R.id.precioFacturaCard);
            unidades=itemView.findViewById(R.id.unidadesProductoFacturaCard);
            precioTotal=itemView.findViewById(R.id.precioTotalProductoFacturaCard);
        }
    }
}
