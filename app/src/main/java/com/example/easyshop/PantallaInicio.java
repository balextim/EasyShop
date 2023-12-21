package com.example.easyshop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyshop.controlador.AdaptadorEstablecimiento;
import com.example.easyshop.modelo.Establecimiento;
import com.example.easyshop.modelo.Producto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PantallaInicio extends AppCompatActivity{
    public static Activity cerrarActividad;
    public RecyclerView recyclerEstablecimiento;
    public AdaptadorEstablecimiento adaptadorEstablecimiento;
    List<Establecimiento> listaEstablecimiento = new ArrayList<>();
    StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    DrawerLayout drawerLayout;
    ImageView menu;
    Button inicio, facturas, ajustes, cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicio);

        drawerLayout = findViewById(R.id.drawer_layout_inicio);
        menu = findViewById(R.id.menu);
        inicio = findViewById(R.id.inicio_bt);
        facturas = findViewById(R.id.facturas_bt);
        ajustes = findViewById(R.id.ajustes_bt);
        cerrarSesion = findViewById(R.id.cerrar_sesion_bt);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });

        facturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirigirActividad(PantallaInicio.this,FacturasPagadas.class);
            }
        });

        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirigirActividad(PantallaInicio.this, PantallaAjustes.class);
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PantallaInicio.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                redirigirActividad(PantallaInicio.this, InicioSesion.class);
            }
        });

        cerrarActividad=this;

        recyclerEstablecimiento = findViewById(R.id.recyclerEstablecimiento);
        adaptadorEstablecimiento = new AdaptadorEstablecimiento(listaEstablecimiento);

        recyclerEstablecimiento.setAdapter(adaptadorEstablecimiento);
        recyclerEstablecimiento.setLayoutManager(new LinearLayoutManager(this));

        infoEstablecimiento(listaEstablecimiento);

        adaptadorEstablecimiento.setOnItemClickListener(new AdaptadorEstablecimiento.OnItemClickListener() {
            @Override
            public void onItemClick(String supermercado) {
                String s = supermercado;
                Intent intent = new Intent(PantallaInicio.this, ProductosEstablecimiento.class);
                intent.putExtra("supermercado", s);
                startActivity(intent);
            }
        });
    }

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

    private void infoEstablecimiento(List<Establecimiento> listaEstablecimiento) {

        //Realtime database.
        database = FirebaseDatabase.getInstance();
        reference =database.getReference("supermercados");

        //Firebase storage
        storageReference = FirebaseStorage.getInstance().getReference().child("supermercados");

        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for(StorageReference s : listResult.getPrefixes()){
                    reference.child(s.getName()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Establecimiento establecimiento = snapshot.getValue(Establecimiento.class);

                            String nombre = establecimiento.getNombre();
                            String direccion = establecimiento.getDireccion();
                            String logo = establecimiento.getLogo();
                            String horaApertura = establecimiento.getHoraApertura();
                            String horaCierre = establecimiento.getHoraCierre();

                            listaEstablecimiento.add(new Establecimiento(direccion, horaApertura, horaCierre, logo, nombre));
                            adaptadorEstablecimiento.notifyDataSetChanged();
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
