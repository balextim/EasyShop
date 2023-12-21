package com.example.easyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyshop.controlador.AdaptadorFacturasPagadas;
import com.example.easyshop.controlador.AdaptadorLineaFactura;
import com.example.easyshop.controlador.AdaptadorProductoCarrito;
import com.example.easyshop.modelo.Cliente;
import com.example.easyshop.modelo.Factura;
import com.example.easyshop.modelo.LineaFactura;
import com.example.easyshop.modelo.ProductoCarrito;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FacturasPagadas extends AppCompatActivity {
    int idFactura=0;
    private FirebaseDatabase database;
    private DatabaseReference referenceCliente;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    List<Factura> listaFacturasPagadas=new ArrayList<>();
    public RecyclerView recyclerViewFacturasPagadas;
    AdaptadorFacturasPagadas adaptadorFacturasPagadas;
    DrawerLayout drawerLayout;
    ImageView menu;
    Button inicio, facturas, ajustes, cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas_pagadas);

        //menú lateral
        drawerLayout = findViewById(R.id.drawer_layout_facturasPagadas);
        menu = findViewById(R.id.menu);
        inicio = findViewById(R.id.inicio_bt);
        facturas = findViewById(R.id.facturas_bt);
        ajustes = findViewById(R.id.ajustes_bt);
        cerrarSesion = findViewById(R.id.cerrar_sesion_bt);

        //redireccionamiento de pantallas del menú lateral
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirigirActividad(FacturasPagadas.this,PantallaInicio.class);
            }
        });

        facturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });

        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirigirActividad(FacturasPagadas.this, PantallaAjustes.class);
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FacturasPagadas.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                redirigirActividad(FacturasPagadas.this, InicioSesion.class);
            }
        });

        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        referenceCliente=database.getReference("clientes").child(user.getUid());
        //listaFacturasPagadas.add(new Factura());
        referenceCliente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Cliente cliente=snapshot.getValue(Cliente.class);
                listaFacturasPagadas=cliente.getFacturas();
                recyclerViewFacturasPagadas=findViewById(R.id.recyclerViewFacturasPagadas);
                adaptadorFacturasPagadas=new AdaptadorFacturasPagadas(listaFacturasPagadas);
                recyclerViewFacturasPagadas.setAdapter(adaptadorFacturasPagadas);
                recyclerViewFacturasPagadas.setLayoutManager(new LinearLayoutManager(FacturasPagadas.this));
                adaptadorFacturasPagadas.notifyDataSetChanged();

                adaptadorFacturasPagadas.setOnitemClickListener(new AdaptadorFacturasPagadas.OnItemClickListener() {
                    @Override
                    public void onItemClick(String id) {
                        idFactura= Integer.parseInt(id);
                        abriLineaFactura(cliente, id);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//Este método muestra la información de la factura clicada, cargando un recyclerview de un layout inflado en un AlertDialog personalizado
    public void abriLineaFactura(Cliente cliente, String id){
        RecyclerView recyclerLineaFactura;
        AdaptadorLineaFactura adaptadorLineaFactura;
        TextView establecimiento, totalApagar;
        Button compartir, descargar, volver2;
        PaymentButtonContainer paymentButtonContainer;

        List<Factura> listaFactura=cliente.getFacturas();
        Factura factura=listaFactura.get(Integer.parseInt(id)-1);
        List<LineaFactura>lineaFacturas=factura.getLineaFactura();
        // Crear el diálogo
        Dialog customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Para eliminar el título del diálogo
        customDialog.setContentView(R.layout.activity_realizar_compra);//cargamos el layout en el alert
        recyclerLineaFactura=customDialog.findViewById(R.id.recyclerFactura);
        recyclerLineaFactura.setLayoutManager(new LinearLayoutManager(this));
        adaptadorLineaFactura=new AdaptadorLineaFactura(lineaFacturas);
        recyclerLineaFactura.setAdapter(adaptadorLineaFactura);
        establecimiento=customDialog.findViewById(R.id.nombreEstablecimientoFactura);
        establecimiento.setText(factura.getNombreFactura());
        totalApagar=customDialog.findViewById(R.id.totalApagarFactura);
        totalApagar.setText(String.valueOf(factura.getTotalApagar())+"€");
//        volver=customDialog.findViewById(R.id.terminarCompra);
//        volver.setText("Volver");
        volver2=customDialog.findViewById(R.id.volverACarritoCompra);
        volver2.setVisibility(View.GONE);
        compartir=customDialog.findViewById(R.id.compartir);
        compartir.setVisibility(View.VISIBLE);
        descargar=customDialog.findViewById(R.id.descargarPdf);
        descargar.setVisibility(View.VISIBLE);
        paymentButtonContainer=customDialog.findViewById(R.id.payment_button_container);
        paymentButtonContainer.setVisibility(View.GONE);
        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Factura factura1=listaFactura.get(idFactura-1);
                crearPdf(factura1);
                String path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/facturasPdf";
                File dir=new File(path);
                File archivo=new File(dir, factura.getIdFactura()+factura.getNombreFactura()+factura.getFecha()+".pdf");
                compartirContenido(archivo);
                //Toast.makeText(customDialog.getContext(), "compartir", Toast.LENGTH_SHORT).show();
            }
        });

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder descargarFactura = new AlertDialog.Builder(customDialog.getContext());
                descargarFactura.setTitle("Guardar Factura");
                descargarFactura.setMessage("¿Deasea descargar su factura de compra?");
                descargarFactura.setCancelable(false);
                descargarFactura.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        crearPdf(factura);
                        Toast.makeText(customDialog.getContext(), "Factura descargada correctamente", Toast.LENGTH_SHORT).show();
                    }
                });
                descargarFactura.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Toast.makeText(customDialog.getContext(), "Factura guardada en base de datos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                descargarFactura.show();
            }
        });

        Window window = customDialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Mostrar el diálogo
        customDialog.show();
    }

    private void compartirContenido(File file) {
        // Crear un Intent con la acción ACTION_SEND
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf"); // Establecer el tipo de contenido que vamos a compartir
        String authority = "com.example.easyshop.provider";
        Uri uriArchivoPdf=FileProvider.getUriForFile(this, authority, file);
        intent.putExtra(Intent.EXTRA_STREAM,uriArchivoPdf);
        // Crea un Intent chooser para mostrar las opciones de compartir
        Intent chooser = Intent.createChooser(intent, "Compartir con");

        // Asegurarse de que haya al menos una aplicación que pueda manejar el intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        } else {
            Toast.makeText(this, "No se encontró una aplicación para manejar la acción", Toast.LENGTH_SHORT).show();
        }
    }
    //método para crear el pdf
    public void crearPdf(Factura factura){
        ArrayList<LineaFactura> lineasFactura=factura.getLineaFactura();
        try{
            //creamor la ruta en la que queremos guardar las facturas
            String carpeta ="/facturasPdf";
            String path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+carpeta;

            //comprobamos si la carpeta existe y en caso de que no la creamos
            File dir=new File(path);
            if(!dir.exists()){
                dir.mkdirs();
                Toast.makeText(this,"Carpeta creada", Toast.LENGTH_SHORT).show();
            }
            //Creamos el archivo y le pasamos la ruta en la que va a estar y el nombre que tendrá
            File archivo=new File(dir, factura.getIdFactura()+factura.getNombreFactura()+factura.getFecha()+".pdf");

            //creamos el flujo de datos del archivo
            FileOutputStream fos=new FileOutputStream(archivo);
            //creamos el documento
            Document documento=new Document();
            //obtenemos la instacia para poder escribir
            PdfWriter.getInstance(documento, fos);

            //abrimos el documento
            documento.open();
            //insertamos un parrafo que será un título y le damos formato con FontFactory
            Paragraph titulo=new Paragraph("Factura de compra\n\n\n", FontFactory.getFont("arial", 22, Font.BOLD, BaseColor.BLUE));
            //agregamos el título al archivo
            documento.add(titulo);

            Paragraph establecimiento=new Paragraph("Establecimiento: "+factura.getNombreFactura()+"\n\n\n");
            documento.add(establecimiento);
            //en este caso haré que la factura sea en formato tabla
            //creamos el objeto tabla especificando el número de columnas que tendrá
            PdfPTable facturaTabla=new PdfPTable(5);
            facturaTabla.addCell("Id producto");
            facturaTabla.addCell("Nombre");
            facturaTabla.addCell("Precio");
            facturaTabla.addCell("Unidades");
            facturaTabla.addCell("Precio total");
            //recorremos la lista de las líneas de factura para agregarlas al documento
            for(int i=0; i<lineasFactura.size(); i++){
                facturaTabla.addCell(String.valueOf(lineasFactura.get(i).getId()));
                facturaTabla.addCell(String.valueOf(lineasFactura.get(i).getNombre()));
                facturaTabla.addCell(String.valueOf(lineasFactura.get(i).getPrecio()));
                facturaTabla.addCell(String.valueOf(lineasFactura.get(i).getUnidades()));
                facturaTabla.addCell(String.valueOf(lineasFactura.get(i).getPrecioTotal()));
            }
            //añadimos la tabla al documento
            documento.add(facturaTabla);

            //creamos un parrafo para añadir el precio total
            Paragraph precioTotalpdf=new Paragraph("\n\n\nTotal a pagar     "+factura.getTotalApagar()+"€");
            //añadimos el total al documento
            documento.add(precioTotalpdf);
            //cerramos el documento
            documento.close();



        }catch (FileNotFoundException fne){
            fne.printStackTrace();
        }catch (DocumentException de){
            de.printStackTrace();
        }
    }

    //métodos para el control de la navegación del menú lateral, es decir el que abre y cierra las diferentes pantallas
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirigirActividad(Activity activity, Class clase){
        Intent intent = new Intent(activity, clase);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}