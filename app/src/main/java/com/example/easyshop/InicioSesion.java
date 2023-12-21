package com.example.easyshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesion extends AppCompatActivity {
    TextView txtUserIni, txtContrIni;
    Button btIniIni, btRegistIni;

    String email, contrasenia;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);

        txtUserIni = findViewById(R.id.txtUserIni);
        txtContrIni = findViewById(R.id.txtContrIni);

        btIniIni = findViewById(R.id.btnIniIni);
        btRegistIni = findViewById(R.id.btnRegIni);

        btRegistIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(InicioSesion.this, RegistroUser.class);
                startActivity(intent);

            }
        });

        btIniIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtUserIni.getText().toString().isEmpty()){
                    txtUserIni.requestFocus();
                    txtUserIni.setSelected(true);
                    txtUserIni.setError("Introduce tu usuario");
                }else if(txtContrIni.getText().toString().isEmpty()){
                    txtContrIni.requestFocus();
                    txtContrIni.setSelected(true);
                    txtContrIni.setError("Introduce tu contraseña");
                }else{
                    //Inicio de sesión
                    mAuth = FirebaseAuth.getInstance();
                    mUser = mAuth.getCurrentUser();

                    email=txtUserIni.getText().toString();
                    contrasenia=txtContrIni.getText().toString();
                    mAuth.signInWithEmailAndPassword(email,contrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Intent inicio = new Intent(InicioSesion.this, PantallaInicio.class);
                                startActivity(inicio);
                                Toast.makeText(InicioSesion.this, "Bienvenido " +email, Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Log.d("ERROR", task.getException().toString());
                            }
                        }
                    });
                }

            }
        });

    }

}