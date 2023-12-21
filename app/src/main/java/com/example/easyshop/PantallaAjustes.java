package com.example.easyshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PantallaAjustes extends AppCompatActivity {

    SwitchCompat switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DrawerLayout drawerLayout;
    ImageView menu;
    Button inicio, facturas, ajustes, cerrarSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_ajustes);

        switchMode=findViewById(R.id.switchMode);
        sharedPreferences=getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode=sharedPreferences.getBoolean("nightMode", false);

        if(nightMode){
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nightMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor=sharedPreferences.edit();
                    editor.putBoolean("nightMode", false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor=sharedPreferences.edit();
                    editor.putBoolean("nightMode", true);
                }
                editor.apply();
            }
        });

        //menú lateral
        drawerLayout = findViewById(R.id.drawer_layout_ajustes);
        menu = findViewById(R.id.menu);
        inicio = findViewById(R.id.inicio_bt);
        facturas = findViewById(R.id.facturas_bt);
        ajustes = findViewById(R.id.ajustes_bt);
        cerrarSesion = findViewById(R.id.cerrar_sesion_bt);


        //redireccionamiento de pantallas
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirigirActividad(PantallaAjustes.this,PantallaInicio.class);
            }
        });

        facturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirigirActividad(PantallaAjustes.this, FacturasPagadas.class);
            }
        });

        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer(drawerLayout);
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PantallaAjustes.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                redirigirActividad(PantallaAjustes.this, InicioSesion.class);
            }
        });

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