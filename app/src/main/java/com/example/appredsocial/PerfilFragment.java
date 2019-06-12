package com.example.appredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appredsocial.Adapters.AdaptadorPosts;
import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.appredsocial.Objetos.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;

import java.util.ArrayList;

public class PerfilFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;
    private CarouselView carouselView;
    private TextView txtNombre,txtCiudad,txtTelefono,txtCorreo,txtGenero,txtPrimaria,txtSecundaria,txtUniversidad,txtFechaNac;
    private ImageView imageViewFotoPerfil;

    private StorageReference refStorage;
    private FirebaseFirestore refFirestore;
    private FirebaseAuth firebaseAuth;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_perfil, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        inicializarRecyclerView();

        txtNombre = rootView.findViewById(R.id.txtNombre);
        txtCiudad = rootView.findViewById(R.id.txtCiudad);
        txtCorreo = rootView.findViewById(R.id.txtCorreo);
        txtTelefono = rootView.findViewById(R.id.txtTelefono);
        txtPrimaria = rootView.findViewById(R.id.txtPrimaria);
        txtSecundaria = rootView.findViewById(R.id.txtSecundaria);
        txtUniversidad = rootView.findViewById(R.id.txtUniversidad);
        txtGenero = rootView.findViewById(R.id.txtGenero);
        txtFechaNac = rootView.findViewById(R.id.txtFechaNacimiento);
        imageViewFotoPerfil = rootView.findViewById(R.id.imgPerfil);

        refStorage = FirebaseStorage.getInstance().getReference(ReferenciasFirebase.REFERENCIA_FOTOS_PERFIL);
        refFirestore = FirebaseFirestore.getInstance();

        cargarPerfil();


        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),NuevoPost.class));
            }
        });
        return rootView;
    }

    private void inicializarRecyclerView(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = rootView.findViewById(R.id.recyclerViewPerfil);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ArrayList<Post> posts=new ArrayList<Post>();
        firebaseFirestore.collection("Posts").document(firebaseAuth.getCurrentUser().getEmail()).collection("Post").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
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
                        posts.add(post);
                    }
                }
            });
        TextView noPublicaciones= rootView.findViewById(R.id.noPublicaciones);
        if(posts.isEmpty())
            noPublicaciones.setVisibility(View.VISIBLE);
        else
            noPublicaciones.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(new AdaptadorPosts(getContext(),posts,firebaseAuth, firebaseFirestore));
    }

    private void CarruselFotos(){
        /*Codigo del carrusel de fotos
        carouselView = findViewById(R.id.carrouselView);
        final Drawable[] sampleImages = {};     //Las fotos que aparecen en el carrusel de fotos
                                                //tambien se puede hacer con ints, y se meten los
                                                //ids, pero como lo agarramos de la base de datos
                                                //mejor con Drawable

        carouselView.setPageCount(sampleImages.length); //La cantidad de paginas del carrusel

        // Que hace cuando se cambia la imagen
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageDrawable(sampleImages[position]); //Tambien se puede usar setImageResource y usar
                                                                    //ids, pero como estamos agarrando las fotos de
                                                                    //base de datos mejor con Drawable
            }
        });

        // Que hace cuando toca una de las imagenes
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                //Abrir pantalla de esa foto
            }
        });*/
    }

    private void cargarPerfil() {
        refFirestore.collection(ReferenciasFirebase.REFERENCIA_PERFILES).document(firebaseAuth.getCurrentUser().getEmail()).get()
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
                            Picasso.with(getContext()).load(urlFirebase).fit().centerCrop().into(imageViewFotoPerfil);
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
