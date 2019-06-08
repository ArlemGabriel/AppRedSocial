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

public class RegistrarseEmail extends AppCompatActivity {
    Button btnRegistro;
    TextView txtEmail,txtContrasena;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_email);

        btnRegistro = findViewById(R.id.btnRegistrar);
        txtEmail = findViewById(R.id.txtRegistrarEmail);
        txtContrasena = findViewById(R.id.txtRegistrarPass);

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

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = txtEmail.getText().toString();
                String contra = txtContrasena.getText().toString();
                if(!correo.isEmpty() && !contra.isEmpty()){
                    registrarse(correo,contra);
                }else{
                    Toast.makeText(RegistrarseEmail.this,"Ingrese sus datos",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void registrarse(String email,String password){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistrarseEmail.this,"Usuario Creado Exitosamente",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegistrarseEmail.this,Perfil.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegistrarseEmail.this,task.getException().getMessage()+"",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
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

