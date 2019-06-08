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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logueo extends AppCompatActivity {
    Button btnLogueo,btnRegistrarseEmail;
    TextView txtEmail,txtContrasena;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogueo = findViewById(R.id.btnLogueo);
        txtEmail = findViewById(R.id.txtLogueoEmail);
        txtContrasena = findViewById(R.id.txtLogueoPass);
        btnRegistrarseEmail = findViewById(R.id.btnRegistrarEmail);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario!=null){
                    Log.i("Estado Sesion",": Activa");
                }else{
                    Log.i("Estado Sesion",": Inactiva");
                }
            }
        };

        btnLogueo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = txtEmail.getText().toString();
                String contra = txtContrasena.getText().toString();
                if(!contra.isEmpty() && !correo.isEmpty()){
                    loguearse(correo,contra);
                }else{
                    Toast.makeText(Logueo.this,"Ingrese sus datos",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegistrarseEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logueo.this,RegistrarseEmail.class);
                startActivity(intent);
            }
        });
    }
    private void loguearse(String email,String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Logueo.this,"Sesión Iniciada",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Logueo.this,Perfil.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Logueo.this,task.getException().getMessage()+"",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
    }
