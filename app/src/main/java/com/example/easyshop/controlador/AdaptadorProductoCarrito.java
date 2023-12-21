package com.example.easyshop.controlador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easyshop.ProductosEstablecimiento;
import com.example.easyshop.R;
import com.example.easyshop.modelo.ProductoCarrito;

import java.util.List;

public class AdaptadorProductoCarrito extends RecyclerView.Adapter<AdaptadorProductoCarrito.ViewHolder> {
    private List<ProductoCarrito> listaProductoCarrito;
    private ProductosEstablecimiento productosEstablecimiento = new ProductosEstablecimiento();

    public  AdaptadorProductoCarrito(List<ProductoCarrito> listaProductoCarrito){
        this.listaProductoCarrito=listaProductoCarrito;
    }
    @NonNull
    @Override
    public AdaptadorProductoCarrito.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_carrito, parent, false);
        return new AdaptadorProductoCarrito.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorProductoCarrito.ViewHolder holder, int position) {
        ProductoCarrito productoCarrito=listaProductoCarrito.get(position);
        Glide.with(holder.itemView).load(productoCarrito.getLogo()).into(holder.logoProductoCarrito);
        holder.nombreProductoCarritoCard.setText(productoCarrito.getNombre());
        String precio = String.valueOf(productoCarrito.getPrecio());
        holder.precioProductoCarritoCard.setText(precio + "€");
        holder.unidadesProductoCarritoCard.setText(String.valueOf(productoCarrito.getUnidades()));
        holder.precioTotalProductoCarritoCard.setText(String.valueOf(productoCarrito.getPrecioTotalProductos())+"€");
        holder.agregarUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productoCarrito.aniadirUnidad();
                productoCarrito.obtenerPrecioTotal();
                holder.precioTotalProductoCarritoCard.setText(String.valueOf(productoCarrito.getPrecioTotalProductos())+"€");
                holder.unidadesProductoCarritoCard.setText(String.valueOf(productoCarrito.getUnidades()));
                productosEstablecimiento.totalApagar.setText(String.valueOf(obtenerTotal())+"€");
                Toast.makeText(view.getContext(), "Se ha añadido una unidad de este producto", Toast.LENGTH_SHORT).show();
            }
        });
        holder.restarUnidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productoCarrito.getUnidades()>1){
                    productoCarrito.restarUnidad();
                    productoCarrito.obtenerPrecioTotal();
                    holder.precioTotalProductoCarritoCard.setText(String.valueOf(productoCarrito.getPrecioTotalProductos())+"€");
                    holder.unidadesProductoCarritoCard.setText(String.valueOf(productoCarrito.getUnidades()));
                    productosEstablecimiento.totalApagar.setText(String.valueOf(obtenerTotal())+"€");
                    Toast.makeText(view.getContext(), "Se ha eliminado una unidad de este producto", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(view.getContext(), "No se pueden restar más unidades a este producto", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.eliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaProductoCarrito.remove(position);
                notifyDataSetChanged();
                Toast.makeText(view.getContext(), "Producto retirado de la compra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Double obtenerTotal(){
        Double totalApagar = 0.0;
        if(listaProductoCarrito.size()==0 || listaProductoCarrito ==null){
            return totalApagar;
        }else{
            for(int i=0; i<listaProductoCarrito.size(); i++){
                ProductoCarrito productoCarrito=listaProductoCarrito.get(i);
                totalApagar=totalApagar+productoCarrito.getPrecioTotalProductos();
            }
        }
        return totalApagar;
    }

    @Override
    public int getItemCount() {
        return listaProductoCarrito.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView logoProductoCarrito;
        TextView nombreProductoCarritoCard, precioProductoCarritoCard, unidadesProductoCarritoCard, precioTotalProductoCarritoCard;
        ImageButton agregarUnidad, restarUnidad, eliminarProducto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logoProductoCarrito=itemView.findViewById(R.id.logoProductoCarritoCard);
            nombreProductoCarritoCard=itemView.findViewById(R.id.nombreProductoCarritoCard);
            precioProductoCarritoCard=itemView.findViewById(R.id.precioProductoCarritoCard);
            unidadesProductoCarritoCard=itemView.findViewById(R.id.unidadesProductoCarritoCard);
            precioTotalProductoCarritoCard=itemView.findViewById(R.id.precioTotalProductoCard);
            agregarUnidad=itemView.findViewById(R.id.agregarUnidadButton);
            restarUnidad=itemView.findViewById(R.id.restarUnidadButton);
            eliminarProducto=itemView.findViewById(R.id.borrarProductoCarrito);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
