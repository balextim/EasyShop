package com.example.easyshop.controlador;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easyshop.ProductosEstablecimiento;
import com.example.easyshop.R;
import com.example.easyshop.modelo.Producto;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorProducto extends RecyclerView.Adapter<AdaptadorProducto.ViewHolder> {

    private List<Producto> listaProductos = new ArrayList<>();
    private OnItemClickListener listener;
    ImageView logoProductoInfo;
    TextView nombreProductoInfo, precioCantidadInfo, marcaProductoInfo, tipoProductoIndo, pesoProductoInfo, descripcionProductoInfo;
    public AdaptadorProducto(List<Producto> listaProductos){
        this.listaProductos=listaProductos;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos);
    }
    public void setOnItemClickListener(AdaptadorProducto.OnItemClickListener listenerclick){
        listener = listenerclick;
    }

    @NonNull
    @Override
    public AdaptadorProducto.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_establecimiento_card, parent, false);
        return new AdaptadorProducto.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorProducto.ViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.nombreProducto.setText(producto.getNombre());
        holder.stock.setText(String.valueOf(producto.getStock()));
        holder.precio.setText((String.valueOf(producto.getPrecio())+"€"));
        holder.tipoProducto.setText(producto.getTipo());
        Glide.with(holder.itemView).load(producto.getLogo()).into(holder.logoProducto);

        holder.infoProductoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View customDialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.informacion_producto, null);
                logoProductoInfo=customDialogView.findViewById(R.id.logoProductoInfo);
                nombreProductoInfo=customDialogView.findViewById(R.id.nombreProductoInfo);
                precioCantidadInfo=customDialogView.findViewById(R.id.precioCantidadInfo);
                marcaProductoInfo=customDialogView.findViewById(R.id.contenidoMarca);
                tipoProductoIndo=customDialogView.findViewById(R.id.contenidoTipo);
                pesoProductoInfo=customDialogView.findViewById(R.id.contenidoPeso);
                descripcionProductoInfo=customDialogView.findViewById(R.id.contenidoDescripcion);

                Glide.with(customDialogView).load(producto.getLogo()).into(logoProductoInfo);
                nombreProductoInfo.setText(producto.getNombre());
                precioCantidadInfo.setText(String.valueOf(producto.getPrecio())+"€");
                marcaProductoInfo.setText(producto.getMarca());
                tipoProductoIndo.setText(producto.getTipo());
                pesoProductoInfo.setText(String.valueOf(producto.getPeso())+"g");
                descripcionProductoInfo.setText(producto.getDescripcion());
                Animation slideUpAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.deslizar);
                //customDialogView.startAnimation(slideUpAnimation);
                customDialogView.setAnimation(slideUpAnimation);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(customDialogView)
                        .setTitle("Información del producto")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                //AlertDialog customDialog = builder.create();
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView logoProducto;
        private TextView nombreProducto, tipoProducto, stock, precio;

        private ImageButton infoProductoButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            infoProductoButton = itemView.findViewById(R.id.infoProductoButton);
            logoProducto=itemView.findViewById(R.id.logoProductoCard);
            nombreProducto=itemView.findViewById(R.id.nombreProducto);
            tipoProducto=itemView.findViewById(R.id.tipoProducto);
            stock=itemView.findViewById(R.id.stockProducto);
            precio=itemView.findViewById(R.id.precioProducto);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
