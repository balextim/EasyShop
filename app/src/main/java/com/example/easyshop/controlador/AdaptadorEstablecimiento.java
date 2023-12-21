package com.example.easyshop.controlador;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easyshop.ProductosEstablecimiento;
import com.example.easyshop.R;
import com.example.easyshop.modelo.Establecimiento;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorEstablecimiento extends RecyclerView.Adapter<AdaptadorEstablecimiento.ViewHolder> {

    private List<Establecimiento> listaEstablecimientos = new ArrayList<>();
    private OnItemClickListener listenerClick;

    public AdaptadorEstablecimiento(List<Establecimiento> listaEstablecimientos){
        this.listaEstablecimientos = listaEstablecimientos;
    }

    public interface OnItemClickListener{
        void onItemClick(String supermercado);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        listenerClick = listener;
    }

    @NonNull
    @Override
    public AdaptadorEstablecimiento.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.establecimiento, parent, false);
        return new AdaptadorEstablecimiento.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEstablecimiento.ViewHolder holder, int position) {
        Establecimiento establecimiento = listaEstablecimientos.get(position);

        Glide.with(holder.itemView).load(establecimiento.getLogo()).into(holder.logoEstablecimiento);
        holder.nombreEstablecimiento.setText(establecimiento.getNombre());
        holder.direccionEstablecimientoCard.setText(establecimiento.getDireccion());
        holder.estadoEstablecimientoCard.setText("Abierto");

        holder.infoEstablecimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.logoEstablecimiento.getVisibility() == View.VISIBLE){
                    holder.logoEstablecimiento.setVisibility(View.GONE);
                    holder.nombreEstablecimiento.setVisibility(View.VISIBLE);
                    holder.direccionEstablecimientoCard.setVisibility(View.VISIBLE);
                    holder.estadoEstablecimientoCard.setVisibility(View.VISIBLE);
                }else {
                    holder.logoEstablecimiento.setVisibility(View.VISIBLE);
                    holder.nombreEstablecimiento.setVisibility(View.GONE);
                    holder.direccionEstablecimientoCard.setVisibility(View.GONE);
                    holder.estadoEstablecimientoCard.setVisibility(View.GONE);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listenerClick != null){
                    listenerClick.onItemClick(holder.nombreEstablecimiento.getText().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEstablecimientos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView logoEstablecimiento;
        private TextView nombreEstablecimiento, direccionEstablecimientoCard, estadoEstablecimientoCard;

        private ImageButton infoEstablecimiento;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            infoEstablecimiento=itemView.findViewById(R.id.infoEstablecimintoButton);
            logoEstablecimiento = itemView.findViewById(R.id.logoEstablecimientoCard);
            nombreEstablecimiento=itemView.findViewById(R.id.nombreEstablecimientoCard);
            direccionEstablecimientoCard=itemView.findViewById(R.id.direccionEstablecimientoCard);
            estadoEstablecimientoCard=itemView.findViewById(R.id.estadoEstablecimientoCard);
        }

        @Override
        public void onClick(View view) {

        }
    }
}