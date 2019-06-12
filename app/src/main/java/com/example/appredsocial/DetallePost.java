package com.example.appredsocial;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appredsocial.Adapters.AdaptadorPosts;
import com.example.appredsocial.Objetos.Post;
import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetallePost extends AppCompatActivity {

    int numPost = 0;
    String correo;

    ArrayList<Post> posts = new ArrayList<Post>();

    ImageView fotoPerfil, imagen;
    TextView txtnombre, txtfecha, txtdescripcion, txtcantLikes, txtcantDislikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_post);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if(b != null){
            numPost = Integer.parseInt(b.getString("pos"));
            correo = b.getString("correo");
        }

        fotoPerfil = findViewById(R.id.imgUsuarioPost);
        txtnombre = findViewById(R.id.textNombreUsuario);
        txtfecha = findViewById(R.id.textTimePassed);
        imagen = findViewById(R.id.imgNewPost);
        txtdescripcion = findViewById(R.id.textNewPostDescription);

        txtcantLikes = findViewById(R.id.cantLikes);
        txtcantDislikes = findViewById(R.id.cantDislikes);

        cargarPost();
        mostrarPost();
    }

    private void cargarPost(){
        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        ff.collection("Posts").document(correo).collection("Post").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String url = documentSnapshot.getString("ImgUrl");
                            if(!url.equals("null")){
                                //Log.i("Info", url);
                                Post post = new Post();
                                post.setCorreoUsuario(documentSnapshot.getString("EmailUsuario"));
                                post.setDescripcion(documentSnapshot.getString("Descripcion"));
                                post.setAnno(Integer.valueOf(documentSnapshot.get("Anno").toString()));
                                post.setMes(Integer.valueOf(documentSnapshot.get("Mes").toString()));
                                post.setDia(Integer.valueOf(documentSnapshot.get("Dia").toString()));
                                post.setHora(Integer.valueOf(documentSnapshot.get("Hora").toString()));
                                post.setMinutos(Integer.valueOf(documentSnapshot.get("Minutos").toString()));
                                post.setSegundos(Integer.valueOf(documentSnapshot.get("Segundos").toString()));
                                post.setCantLikes(Integer.valueOf(documentSnapshot.get("Likes").toString()));
                                post.setCantDislikes(Integer.valueOf(documentSnapshot.get("Dislikes").toString()));
                                post.setUrlImagen(documentSnapshot.getString("ImgUrl"));
                                post.setIdPost(documentSnapshot.getString("id"));
                                posts.add(post);
                            }
                        }
                        mostrarPost();
                    }
                });
    }

    private void mostrarPost(){
        if(posts.size()>0){
            //Log.i("Info", "Post: "+numPost);
            final Post actual = posts.get(numPost);
            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Perfiles").document(actual.getCorreoUsuario()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                              @Override
                                              public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                  String nombre = documentSnapshot.getString("Nombre") + " " + documentSnapshot.getString("Apellidos");
                                                  txtnombre.setText(nombre);
                                                  String urlImagen = documentSnapshot.getString("Url");
                                                  if(!urlImagen.isEmpty() && urlImagen!="null")
                                                      Picasso.with(getApplicationContext()).load(urlImagen).
                                                              fit().centerCrop().into(fotoPerfil);
                                              }
                                          }
                    );

            txtdescripcion.setText(actual.getDescripcion());
            txtfecha.setText(actual.tiempoDePublicacion());
            txtcantLikes.setText(String.valueOf(actual.getCantLikes()));
            txtcantDislikes.setText(String.valueOf(actual.getCantDislikes()));

            Picasso.with(getApplicationContext()).load(actual.getUrlImagen()).centerInside().fit().
                    into(imagen);

            imagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(numPost++>=(posts.size()-1)){
                        numPost = 0;
                        mostrarPost();
                    }
                    else{
                        mostrarPost();
                    }

                }
            });
        }
        //Log.i("Info", "No hay posts :(");
    }
}
