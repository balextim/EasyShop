package com.example.easyshop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyshop.controlador.AdaptadorProducto;
import com.example.easyshop.controlador.AdaptadorProductoCarrito;
import com.example.easyshop.modelo.Producto;
import com.example.easyshop.modelo.ProductoCarrito;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductosEstablecimiento extends AppCompatActivity {

    public static Activity cerrarActividad;
    public RecyclerView recyclerProductos;
    public RecyclerView recyclerCarritoCompra;
    public AdaptadorProducto adaptadorProductos;
    public AdaptadorProductoCarrito adaptadorProductoCarrito;
    ArrayList<Producto> listaProductos = new ArrayList<>();
    List<ProductoCarrito> listaProductoCarrito = new ArrayList<>();
    List<String>listaIdentificadoresProducto= new ArrayList<>();
    String[] lista1;
    StorageReference storageReference;
    private FirebaseDatabase database;
    private ImageButton escanerProductos, carritoCompra;
    private DatabaseReference reference;
    public TextView establecimientoClickado;
    public static TextView totalApagar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos_establecimiento);

        cerrarActividad=this;

        adaptadorProductoCarrito=new AdaptadorProductoCarrito(listaProductoCarrito);
        establecimientoClickado = findViewById(R.id.nombreEstablecimientoClickado);
        carritoCompra = findViewById(R.id.carritoCompra);
        carritoCompra.setImageResource(R.drawable.carrito_compra);
        recyclerProductos=findViewById(R.id.recyclerProductosEstablecimiento);
        escanerProductos = findViewById(R.id.escanerProductos);
        adaptadorProductos=new AdaptadorProducto(listaProductos);
        recyclerProductos.setAdapter(adaptadorProductos);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));
        escanerProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escanerCodigo();
            }
        });
        carritoCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCarritoCompra();
            }
        });

        String supermercado = getIntent().getStringExtra("supermercado");
        mostrarProductos(supermercado, listaProductos);
    }
    private void escanerCodigo() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("botón de subir volumen para encender flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLlaucher.launch(options);
    };

    // Método para abrir el diálogo personalizado que contiene el carrito de la compra
    private void abrirCarritoCompra() {

        Button comprar, cancelarCompra;
        // Crear el diálogo
        Dialog customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Para eliminar el título del diálogo
        customDialog.setContentView(R.layout.carrito_compra);
        //View vistaCarritoCompra=LayoutInflater.from(this).inflate(R.layout.carrito_compra, null);
        recyclerCarritoCompra=customDialog.findViewById(R.id.recyclerViewCarritoCompra);
        recyclerCarritoCompra.setLayoutManager(new LinearLayoutManager(this));
        recyclerCarritoCompra.setAdapter(adaptadorProductoCarrito);
        comprar=customDialog.findViewById(R.id.confirmarCompra);
        cancelarCompra=customDialog.findViewById(R.id.cancelarCompra);
        totalApagar = customDialog.findViewById(R.id.cantidadTotalCarrito);
        totalApagar.setText(String.valueOf(obtenerTotalApagar())+"€");

        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listaProductoCarrito.size()==0 || listaProductoCarrito == null){
                    Toast.makeText(ProductosEstablecimiento.this,"No tiene ningún producto en el carrito de compra", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(ProductosEstablecimiento.this, RealizarCompra.class);
                    intent.putExtra("listaProductosCarrito", (Serializable) listaProductoCarrito);
                    intent.putExtra("establecimiento",establecimientoClickado.getText().toString());
                    startActivity(intent);
                }
            }
        });
        cancelarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deseaCancelarCompra(customDialog);
            }
        });

        // Configurar el tamaño del diálogo
        Window window = customDialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Mostrar el diálogo
        customDialog.show();
    }
    public void deseaCancelarCompra(Dialog dialog){
        if(listaProductoCarrito.size()==0 || listaProductoCarrito==null){
            dialog.dismiss();
        }else {
            AlertDialog.Builder cancelarCompra = new AlertDialog.Builder(this);
            cancelarCompra.setTitle("Cancelar compra");
            cancelarCompra.setMessage("¿Está seguro que quiere cancelar su compra?");
            cancelarCompra.setCancelable(false);
            cancelarCompra.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    listaProductoCarrito.clear();
                    listaIdentificadoresProducto.clear();
                    adaptadorProductoCarrito.notifyDataSetChanged();
                    Toast.makeText(ProductosEstablecimiento.this, "Se ha cancelado su compra.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            cancelarCompra.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    return;
                }
            });
            cancelarCompra.show();
        }
    }
    //Crea el escaner y lo muestra por pantalla, además añade el producto escaneado a la lista del carrito
    ActivityResultLauncher<ScanOptions> barLlaucher = registerForActivityResult(new ScanContract(), result -> {
        String establecimiento= (String) establecimientoClickado.getText();
        String resultadoQr, auxQr, auxE, producto, auxEstablecimiento;
        try{
            if (result.getContents() != null){
                resultadoQr=result.getContents();
                auxQr=resultadoQr.substring(resultadoQr.indexOf(":")+1);
                auxE=establecimiento.substring(0,3).toUpperCase();
                auxEstablecimiento=establecimiento.toLowerCase();
                producto=resultadoQr.substring(0,resultadoQr.indexOf(":"));
                if(auxQr.toUpperCase().equals(auxE.toUpperCase())){
                    database = FirebaseDatabase.getInstance();
                    reference =database.getReference("supermercados");
                    StorageReference st = FirebaseStorage.getInstance().getReference().child("supermercados").child(establecimiento.toLowerCase()).child("productos");
                    reference.child(auxEstablecimiento).child("productos").child(producto).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Producto producto1 = snapshot.getValue(Producto.class);
                            String nombre = producto1.getNombre();
                            String tipo = producto1.getTipo();
                            String descripción = producto1.getDescripcion();
                            String marca = producto1.getMarca();
                            int existe=0, stock = producto1.getStock();
                            double precio = producto1.getPrecio();
                            double peso = producto1.getPeso();
                            existe=existeProducto(resultadoQr);
                            if(existe!=-1){
                                listaProductoCarrito.get(existe).aniadirUnidad();
                                listaProductoCarrito.get(existe).obtenerPrecioTotal();
                                Toast.makeText(ProductosEstablecimiento.this, "Producto añadido", Toast.LENGTH_SHORT).show();
                            }else{
                                //Logo producto desde la base de datos.
                                st.child(producto + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        int tamanio=0;
//                                        if(listaProductoCarrito.size()<=0 || listaProductoCarrito==null){
//                                            tamanio=1;
//                                        }else{
//                                            tamanio=listaProductoCarrito.size();
//                                        }
                                        Uri[] logo = new Uri[1];
                                        logo[0] = uri;
                                        listaIdentificadoresProducto.add(resultadoQr);
                                        System.out.println(resultadoQr);
                                        listaProductoCarrito.add(new ProductoCarrito(tamanio+1,1,logo[0],nombre,precio));
                                        adaptadorProductoCarrito.notifyDataSetChanged();
                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("ERROR NO ENCONTRADO PNG");
                                    }
                                });

                                st.child(producto + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        int tamanio=0;
                                        Uri[] logo = new Uri[1];
                                        logo[0] = uri;
                                        listaIdentificadoresProducto.add(resultadoQr);
                                        listaProductoCarrito.add(new ProductoCarrito(tamanio+1,1,logo[0],nombre,precio));
                                        adaptadorProductoCarrito.notifyDataSetChanged();
                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("ERROR NO ENCONTRADO JPG");
                                    }
                                });


                                //listaProductoCarrito.add(producto1);
                                Toast.makeText(ProductosEstablecimiento.this,producto1.getNombre()+" añadido al carrito", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    Toast.makeText(this,"Algo salió mal al escanear el código", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"Algo salió mal al escanear el código", Toast.LENGTH_LONG).show();
            }
        }catch (Exception exception){
            Toast.makeText(this,"Error inesperado", Toast.LENGTH_LONG).show();
        }
    });

    public int existeProducto(String resultadoQR){
        int i=0, existe=-1;
        String identificador;
        if(listaIdentificadoresProducto.size()>0 || listaIdentificadoresProducto != null){
            while (i<listaIdentificadoresProducto.size()){
                identificador=listaIdentificadoresProducto.get(i);
                if(identificador.equals(resultadoQR)){
                    existe=i;
                    break;
                }else{
                    i++;
                }
            }
        }else{
            existe=-1;
        }
        return existe;
    }
    public Double obtenerTotalApagar(){
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
    //método para mostrar en el recyclerview todos los productos del supermercado que se clica
    public void mostrarProductos(String supermercado, List<Producto> listaProductos) {
        database = FirebaseDatabase.getInstance();
        reference =database.getReference("supermercados");
        establecimientoClickado.setText(supermercado);
        storageReference = FirebaseStorage.getInstance().getReference().child("supermercados").child(supermercado.toLowerCase()).child("productos");
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference productos : listResult.getItems()){
                    reference.child(supermercado.toLowerCase()).child("productos").child(productos.getName().substring(0, productos.getName().indexOf("."))).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Producto producto = snapshot.getValue(Producto.class);
                            String nombre = producto.getNombre();
                            String tipo = producto.getTipo();
                            String descripcion = producto.getDescripcion();
                            String marca = producto.getMarca();
                            int stock = producto.getStock();
                            double precio = producto.getPrecio();
                            double peso = producto.getPeso();
                            //Logo producto desde la base de datos.
                            productos.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri[] logo = new Uri[1];
                                    logo[0] = uri;
                                    listaProductos.add(new Producto(nombre, tipo, descripcion, marca, logo[0], stock, precio, peso));
                                    adaptadorProductos.notifyDataSetChanged();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

}