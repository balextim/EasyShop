package com.example.easyshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyshop.modelo.Cliente;
import com.example.easyshop.modelo.Factura;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegistroUser extends AppCompatActivity {

    TextView txtNombre, txtApellido, txtEmailReg, txtUserReg, txtContrReg;
    Button btIniReg, btRegistReg;
    String nombre, apellido, email, usuario, contrasenia;

    FirebaseDatabase database;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_user);

        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtEmailReg = findViewById(R.id.txtEmailReg);
        txtUserReg = findViewById(R.id.txtUserReg);
        txtContrReg = findViewById(R.id.txtContrReg);

        btRegistReg = findViewById(R.id.btnRegReg);
        btIniReg = findViewById(R.id.btnIniReg);

        database = FirebaseDatabase.getInstance();

        //instancia del usuario
        mAuth = FirebaseAuth.getInstance();

        btIniReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegistroUser.this, InicioSesion.class);
                startActivity(intent);
                finish();

            }
        });

        btRegistReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Se puede hacer un método para controlar esto y no añadir mucho código en la clase del activity.
                //Esto posteriormente se controlará mejor, ya que tienen un formato para firebase.
                if(txtNombre.getText().toString().isEmpty()){
                    txtNombre.requestFocus();
                    txtNombre.setSelected(true);
                    txtNombre.setError("Introduce un nombre");
                }else if(txtApellido.getText().toString().isEmpty()) {
                    txtApellido.requestFocus();
                    txtApellido.setSelected(true);
                    txtApellido.setError("Introduce un nombre");
                }else if(txtEmailReg.getText().toString().isEmpty()){
                    txtEmailReg.requestFocus();
                    txtEmailReg.setSelected(true);
                    txtEmailReg.setError("Introduce tu email");
                }else if(txtUserReg.getText().toString().isEmpty()){
                    txtUserReg.requestFocus();
                    txtUserReg.setSelected(true);
                    txtUserReg.setError("Introduce tu usuario");
                }else if(txtContrReg.getText().toString().isEmpty()) {
                    txtContrReg.requestFocus();
                    txtContrReg.setSelected(true);
                    txtContrReg.setError("Introduce una contraseña");
                }else{
                    //después de comprobar que todos los campos están rellenos, procedemos al registro del usuario
                    nombre = txtNombre.getText().toString();
                    apellido = txtApellido.getText().toString();
                    email = txtEmailReg.getText().toString();
                    usuario = txtUserReg.getText().toString();
                    contrasenia = txtContrReg.getText().toString();

                    mAuth.createUserWithEmailAndPassword(email,contrasenia).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = mAuth.getCurrentUser();
                                Cliente cliente = new Cliente(nombre, apellido);
                                database.getReference("clientes").child(user.getUid()).setValue(cliente);
                                Intent inicio = new Intent(RegistroUser.this, PantallaInicio.class);
                                startActivity(inicio);
                                Toast.makeText(RegistroUser.this, "Usuario registado correctamente", Toast.LENGTH_SHORT).show();
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
