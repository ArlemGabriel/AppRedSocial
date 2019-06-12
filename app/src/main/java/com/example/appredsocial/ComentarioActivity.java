package com.example.appredsocial;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.appredsocial.Adapters.AdaptadorComentarios;
import com.example.appredsocial.Objetos.Comentario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComentarioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText txtComentario;
    private Button btnComentario;

    private FirebaseFirestore refFirestore;
    private FirebaseAuth firebaseAuth;

    private AdaptadorComentarios adaptadorComentarios;
    final ArrayList<Comentario> comentarios = new ArrayList<Comentario>();

    private String correoUsuario,idPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);

        firebaseAuth = FirebaseAuth.getInstance();


        Intent i=getIntent();
        correoUsuario=i.getExtras().getString("Email");
        idPost=i.getExtras().getString("idPost");

        recyclerView = findViewById(R.id.recyclerViewAmigos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        inicializarRecyclerView();

        txtComentario = findViewById(R.id.txtBusquedaAmigos);
        btnComentario = findViewById(R.id.btnBusquedaAmigos);

        refFirestore = FirebaseFirestore.getInstance();

        btnComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarComentario();
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adaptadorComentarios.notifyDataSetChanged();
            }
        }, 1000);

    }

    private void agregarComentario() {
        if(!txtComentario.getText().toString().trim().isEmpty()) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final Map<String, Object> nuevoComentario = new HashMap<>();
            nuevoComentario.put("Email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
            nuevoComentario.put("Comentario", txtComentario.getText().toString());

            firebaseFirestore.collection("Posts").document(correoUsuario).collection("Post").document(idPost).collection("Comentarios").add(nuevoComentario)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(),"Se ha registrado el comentario exitosamente", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(this,"Debe ingresar algun comentario", Toast.LENGTH_SHORT).show();

    }

    private void inicializarRecyclerView(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Posts").document(correoUsuario).collection("Post").document(idPost).collection("Comentarios").get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            final Comentario comentario = new Comentario();

                            String mailComentario = documentSnapshot.getString("Email");
                            comentario.setComentario(documentSnapshot.getString("Comentario"));

                            refFirestore.collection("Perfiles").document(mailComentario).
                                    get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot2) {
                                    String nombre = documentSnapshot2.getString("Nombre");
                                    comentario.setNombre(nombre);
                                    String apellido = documentSnapshot2.getString("Apellidos");
                                    comentario.setApellido(apellido);
                                    String url = documentSnapshot2.getString("Url");
                                    comentario.setUrlImagen(url);
                                    Log.i("Info", comentario.getNombreCompleto());
                                    comentario.setEmail(documentSnapshot2.getString("Email"));
                                }
                            });
                            comentarios.add(comentario);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        adaptadorComentarios = new AdaptadorComentarios(this,comentarios,firebaseAuth,firebaseFirestore);
        recyclerView.setAdapter(adaptadorComentarios);
    }
}
