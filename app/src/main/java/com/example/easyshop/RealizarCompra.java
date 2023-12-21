package com.example.easyshop;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyshop.controlador.AdaptadorLineaFactura;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RealizarCompra extends AppCompatActivity {
    public PaymentButtonContainer paymentButtonContainer;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final String TAG = "PayPalActivity";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "AYNU_ehvS40LbLFXypaupk0oZPa55VEO_gEjlAcGRE6tI-WLpYrsiFa05OEplLxo3KkVrujfYjLcfctj";

    private static final int REQUEST_PAYPAL_PAYMENT = 123;
    private PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(CONFIG_CLIENT_ID);
    ArrayList<LineaFactura> lineasFactura=new ArrayList<>();
    ArrayList<ProductoCarrito> listaProductosCarrito;
    public RecyclerView recyclerViewFactura;
    AdaptadorLineaFactura adaptadorLineaFactura;
    private FirebaseDatabase database;
    private DatabaseReference referenceCliente;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    SimpleDateFormat formatoFecha;
    String fechaFormateada;
    Calendar calendar;
    Date fechaActual;
    Factura factura;
    TextView totalApagar, nombreEstablecimientoFactura;
    Button volverAcarrito, terminar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_compra);
        Intent intent=getIntent();

        paymentButtonContainer=findViewById(R.id.payment_button_container);

        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        //reference=database.getReference("clientes");
        referenceCliente=database.getReference("clientes").child(user.getUid());

        listaProductosCarrito= (ArrayList<ProductoCarrito>) intent.getSerializableExtra("listaProductosCarrito");
        recyclerViewFactura=findViewById(R.id.recyclerFactura);
        adaptadorLineaFactura=new AdaptadorLineaFactura(lineasFactura);
        recyclerViewFactura.setAdapter(adaptadorLineaFactura);
        recyclerViewFactura.setLayoutManager(new LinearLayoutManager(this));
        totalApagar=findViewById(R.id.totalApagarFactura);
        nombreEstablecimientoFactura=findViewById(R.id.nombreEstablecimientoFactura);
        nombreEstablecimientoFactura.setText(intent.getStringExtra("establecimiento"));
        volverAcarrito=findViewById(R.id.volverACarritoCompra);
//        terminar=findViewById(R.id.terminarCompra);
        //obtenemos las líneas de factura para generar la factura de la compra
        obtenerLineasFactura();
        totalApagar.setText("Total a pagar"+String.valueOf(factura.getTotalApagar())+"€");
        volverAcarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //inicio de método de pago con paypal
        if (paymentButtonContainer == null){
            Toast.makeText(RealizarCompra.this, "es nulo", Toast.LENGTH_SHORT).show();
        }else{
            //Configura el botón de pago con la lógica de pago proporcionada en los siguientes argumentos.
        paymentButtonContainer.setup(
                /*Se crea una instancia anónima de la interfaz CreateOrder, que tiene un método create. Este método se llama cuando se está creando la orden (transacción).*/
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        Log.i(TAG, "create: ");
                        //Se crea una lista de unidades de compra.
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();

                        //Se agrega una unidad de compra a la lista. En este caso, se está creando una unidad de compra con una cantidad y moneda específicas.
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.EUR)
                                                        .value(String.valueOf(factura.getTotalApagar()))
                                                        .build()
                                        )
                                        .build()
                        );

                        /* Se crea una solicitud de orden (OrderRequest) con la intención de capturar el pago, una acción de usuario de "PAY_NOW"
                        y la lista de unidades de compra.*/
                        OrderRequest order = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        /*Se llama al método create en CreateOrderActions para completar la creación de la orden.
                        Aquí se pasa la solicitud de orden y un callback (en este caso, nulo).*/
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                /*Se crea una instancia anónima de la interfaz OnApprove, que tiene un método onApprove.
                Este método se llama cuando la orden es aprobada.*/
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        /*Captura la orden después de que ha sido aprobada. Se crea una instancia anónima de la interfaz OnCaptureComplete
                         para manejar el resultado de la captura.*/
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.d(TAG, String.format("CaptureOrderResult: %s", result));

                                //añadimos la factura de compra a las facturas del usuario

                                referenceCliente.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Cliente cliente=snapshot.getValue(Cliente.class);
                                        List<Factura>facturasCliente=cliente.getFacturas();
                                        Factura factura1=facturasCliente.get(0);
                                        int contador=0;
                                        if(factura1.getIdFactura()==0){
                                            facturasCliente.clear();
                                            Factura factura = new Factura(1,nombreEstablecimientoFactura.getText().toString(),fechaFormateada,lineasFactura);
                                            facturasCliente.add(factura);
                                            referenceCliente.child("facturas").setValue(facturasCliente);
                                            referenceCliente.removeEventListener(this);
                                            //termina la actividad de la factura
                                            finish();
                                        }else{
                                            Factura factura2=new Factura(facturasCliente.size()+1,nombreEstablecimientoFactura.getText().toString(),fechaFormateada,lineasFactura);
                                            facturasCliente.add(factura2);
                                            referenceCliente.child("facturas").setValue(facturasCliente);
                                            referenceCliente.removeEventListener(this);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(RealizarCompra.this, "Su compra ha sido cancelada Error inesperado", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Su compra se ha realizado con éxito", Toast.LENGTH_SHORT).show();
                                //cerramos la actividad para nos devuleva a la pantalla de inicio
                                finish();
                            }
                        });
                    }

                }


        );}
        //fin de método de pago con paypal

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//método que obtiene las líneas de factura, recorriendo la lista de productos y convirtiendo cada producto en una línea de factura
    private void obtenerLineasFactura() {
        for(int i=0; i<listaProductosCarrito.size(); i++){
            ProductoCarrito productoCarrito=listaProductosCarrito.get(i);
            LineaFactura lineaFactura= new LineaFactura(i+1,productoCarrito.getUnidades(),productoCarrito.getNombre(),productoCarrito.getPrecio(),productoCarrito.getPrecioTotalProductos());
            lineasFactura.add(lineaFactura);
            adaptadorLineaFactura.notifyDataSetChanged();
        }
        calendar = Calendar.getInstance();
        fechaActual = calendar.getTime();

        // Formatear la fecha como texto (opcional)
        formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
        fechaFormateada = formatoFecha.format(fechaActual);
        factura=new Factura(1,nombreEstablecimientoFactura.getText().toString(),fechaFormateada,lineasFactura);
    }
}