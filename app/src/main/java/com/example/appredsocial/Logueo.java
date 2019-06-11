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

import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Logueo extends AppCompatActivity {
    Button btnLogueo,btnRegistrarseEmail,btnIngresarGoogle;
    TextView txtEmail,txtContrasena, txtRecuperarContra;
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore refUsuarioBD;
    static final int GOOGLE_SIGN =123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        btnLogueo = findViewById(R.id.btnLogueo);
        txtEmail = findViewById(R.id.txtLogueoEmail);
        txtContrasena = findViewById(R.id.txtLogueoPass);
        btnRegistrarseEmail = findViewById(R.id.btnRegistrarEmail);
        btnIngresarGoogle = findViewById(R.id.btnRegistrarseGoogle);
        txtRecuperarContra = findViewById(R.id.txtViewRecuperarContra);
        refUsuarioBD = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

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
        txtRecuperarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logueo.this,RecuperarContrasena.class);
                startActivity(intent);
            }
        });
        btnIngresarGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });
    }
    private void signInGoogle(){
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN){
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account!=null){
                    firebaseAuthWithGoogle(account);
                }
            }catch(ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG","firebaseAuthWithGoogle: "+account.getId());

        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(),null);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("TAG","Signin success");
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(isNew){
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = user.getEmail();

                                Usuario nuevoUsuario = new Usuario("","",email,"","",null,"","","","");


                                refUsuarioBD.collection(ReferenciasFirebase.REFERENCIA_RAIZBD)
                                        .document(ReferenciasFirebase.REFERENCIA_PERFILES)
                                        .collection(user.getEmail())
                                        .document("Datos Perfil").set(nuevoUsuario)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Logueo.this,"Usuario Creado Exitosamente",Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(Logueo.this, EditarPerfil.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Logueo.this,"Falló el registro de usuario",Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }else{
                                Intent intent = new Intent(Logueo.this,MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(Logueo.this,"Sesion iniciada",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Log.w("TAG","Signin failed");
                            Toast.makeText(Logueo.this,"Signin Failed",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void loguearse(String email, String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Logueo.this,"Sesión Iniciada",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Logueo.this, MainActivity.class);
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
