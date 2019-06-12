package com.example.appredsocial;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appredsocial.Adapters.AdaptadorPosts;
import com.example.appredsocial.Objetos.Post;
import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class PerfilActivity extends AppCompatActivity {


    private String correoUsuario;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private CarouselView carouselView;
    private TextView txtNombre,txtCiudad,txtTelefono,txtCorreo,txtGenero,txtPrimaria,txtSecundaria,txtUniversidad,txtFechaNac;
    private ImageView imageViewFotoPerfil;

    private StorageReference refStorage;
    private FirebaseFirestore refFirestore;
    private FirebaseAuth firebaseAuth;
    int cantPublicaciones;

    TextView noPublicaciones;
    private AdaptadorPosts adaptadorPosts;
    final ArrayList<Post> posts=new ArrayList<Post>();
    DocumentSnapshot ultimaCarga;

    ArrayList<String> urlPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_perfil);

        Intent i=getIntent();
        correoUsuario=i.getExtras().getString("Email");

        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerViewPerfil);
        linearLayoutManager = new LinearLayoutManager(this);

        inicializarRecyclerView();
        recyclerView.setLayoutManager(linearLayoutManager);

        noPublicaciones= findViewById(R.id.noPublicaciones);
        txtNombre = findViewById(R.id.txtNombre);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtPrimaria = findViewById(R.id.txtPrimaria);
        txtSecundaria = findViewById(R.id.txtSecundaria);
        txtUniversidad = findViewById(R.id.txtUniversidad);
        txtGenero = findViewById(R.id.txtGenero);
        txtFechaNac = findViewById(R.id.txtFechaNacimiento);
        imageViewFotoPerfil = findViewById(R.id.imgPerfil);
        carouselView = findViewById(R.id.carrouselView);

        inicializarCarrusel();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        refStorage = FirebaseStorage.getInstance().getReference(ReferenciasFirebase.REFERENCIA_FOTOS_PERFIL);
        refFirestore = FirebaseFirestore.getInstance();

        cargarCarrusel();
        cargarPerfil();

        //Detectar cuando el recycler view esta al final del scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(adaptadorPosts.getItemCount()<cantPublicaciones)
                                cargarMasPosts();
                        }
                    },2000);
                }
            }
        });

        ActualizarLabelNoPublicaciones();
    }

    private void ActualizarLabelNoPublicaciones() {

        if(adaptadorPosts.getItemCount()<1) {
            noPublicaciones.setVisibility(View.VISIBLE);
            noPublicaciones.requestLayout();
        }
        else {
            noPublicaciones.setVisibility(View.INVISIBLE);
            noPublicaciones.requestLayout();
        }
    }

    private void inicializarRecyclerView(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Posts").document(correoUsuario).collection("Post").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int cont=0;
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            cont++;
                        }
                        cantPublicaciones=cont;
                    }
                });

        firebaseFirestore.collection("Posts").document(correoUsuario).collection("Post").orderBy("id", Query.Direction.DESCENDING).limit(10).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            noPublicaciones.setVisibility(View.INVISIBLE);
                            Post post=new Post();
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
                            ultimaCarga=documentSnapshot;
                            posts.add(post);
                        }
                    }
                });
        adaptadorPosts=new AdaptadorPosts(this,posts,firebaseAuth, firebaseFirestore);
        recyclerView.setAdapter(adaptadorPosts);
    }

    private void cargarMasPosts() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").document(correoUsuario).collection("Post").orderBy("id", Query.Direction.DESCENDING).startAfter(ultimaCarga).limit(10).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            noPublicaciones.setVisibility(View.INVISIBLE);
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
                            ultimaCarga = documentSnapshot;
                            posts.add(post);
                        }
                        adaptadorPosts.notifyDataSetChanged();
                    }
                });
    }

    private void inicializarCarrusel(){
        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        /*Codigo del carrusel de fotos*/
        urlPosts = new ArrayList<String>();

        ff.collection("Posts").document(correoUsuario).collection("Post").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            String url = documentSnapshot.getString("ImgUrl");

                            urlPosts.add(url);
                        }
                        cargarCarrusel();

                    }
                });
    }

    private void cargarCarrusel(){
        Log.i("Info", "cantidad de paginas: "+String.valueOf(urlPosts.size()));
        carouselView.setPageCount(urlPosts.size()); //La cantidad de paginas del carrusel
        Log.i("Info", "Sigue");

        // Que hace cuando se cambia la imagen
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.with(getApplicationContext()).load(urlPosts.get(position)).fit().centerCrop().into(imageView);
            }
        });

    }

    private void cargarPerfil() {
        refFirestore.collection(ReferenciasFirebase.REFERENCIA_PERFILES).document(correoUsuario).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String nombreCompleto = documentSnapshot.getString("Nombre")+" "+documentSnapshot.getString("Apellidos");
                        txtNombre.setText(nombreCompleto);
                        txtCorreo.setText("Correo: "+" "+documentSnapshot.getString("Email"));
                        txtCiudad.setText("Ciudad: "+" "+documentSnapshot.getString("Ciudad"));
                        txtTelefono.setText("Telefono: "+" "+documentSnapshot.getString("Telefono"));
                        txtPrimaria.setText("Primaria: "+" "+documentSnapshot.getString("Primaria"));
                        txtSecundaria.setText("Secundaria: "+" "+documentSnapshot.getString("Secundaria"));
                        txtUniversidad.setText("Universidad: "+" "+documentSnapshot.getString("Universidad"));
                        txtGenero.setText("Genero: "+" "+documentSnapshot.getString("Genero"));
                        if(documentSnapshot.getString("FechaNacimiento")!=null){
                            txtFechaNac.setText("Fecha de Nacimiento: "+" "+documentSnapshot.getString("FechaNacimiento"));
                        }
                        String urlFirebase = documentSnapshot.getString("Url");
                        if(!urlFirebase.isEmpty()){
                            Picasso.with(getApplicationContext()).load(urlFirebase).fit().centerCrop().into(imageViewFotoPerfil);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Error: ",e.getMessage());
                        //Toast.makeText(PerfilFragment.this,"Carga de perfil fallida",Toast.LENGTH_LONG).show();
                    }
                });

    }
}
