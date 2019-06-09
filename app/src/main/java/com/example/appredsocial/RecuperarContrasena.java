package com.example.appredsocial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContrasena extends AppCompatActivity {
    TextView txtEmail,txtVolver;
    Button btnRecuperar;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        txtEmail = findViewById(R.id.txtEmail);
        txtVolver = findViewById(R.id.txtAtras);

        btnRecuperar = findViewById(R.id.btnRecuperarContrasena);
        firebaseAuth = FirebaseAuth.getInstance();


        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = txtEmail.getText().toString();
                if(!correo.isEmpty()) {
                    recuperarContraseña(correo);
                }else{
                    Toast.makeText(RecuperarContrasena.this,"Ingrese su correo",Toast.LENGTH_LONG).show();
                }
            }
        });

        txtVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecuperarContrasena.this,Logueo.class);
                startActivity(intent);
            }
        });
    }
    public void recuperarContraseña(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RecuperarContrasena.this, "Se ha enviado un correo para reestablecer su contraseña", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RecuperarContrasena.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
