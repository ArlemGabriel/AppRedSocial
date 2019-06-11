package com.example.appredsocial;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrarseEmail extends AppCompatActivity {
    Button btnRegistro;
    TextView txtEmail,txtContrasena,txtAtras;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore refUsuarioBD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_email);

        refUsuarioBD = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        btnRegistro = findViewById(R.id.btnRecuperarContrasena);
        txtEmail = findViewById(R.id.txtEmail);
        txtContrasena = findViewById(R.id.txtPass);
        txtAtras = findViewById(R.id.txtAtras);

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
        txtAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrarseEmail.this,Logueo.class);
                startActivity(intent);
            }
        });


    }
    private void registrarse(final String email, String password){
        final Map<String, Object> usuario = new HashMap<>();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    usuario.put("Nombre", "");
                    usuario.put("Apellidos", "");
                    usuario.put("Ciudad", "");
                    usuario.put("Email",email);
                    usuario.put("Telefono","");
                    usuario.put("Primaria","");
                    usuario.put("Secundaria","");
                    usuario.put("Universidad","");
                    usuario.put("Url","");

                    refUsuarioBD.collection(ReferenciasFirebase.REFERENCIA_PERFILES)
                            .document(usuario.get("Email").toString()).set(usuario)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegistrarseEmail.this,"Usuario Creado Exitosamente",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(RegistrarseEmail.this, EditarPerfil.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegistrarseEmail.this,"Falló el registro de usuario",Toast.LENGTH_LONG).show();
                                }
                            });
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

