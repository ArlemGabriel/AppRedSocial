package com.example.appredsocial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appredsocial.Objetos.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.synnapps.carouselview.CarouselView;

import java.util.ArrayList;

public class PerfilFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;
    private CarouselView carouselView;
    private TextView txtNombre;
    FirebaseAuth firebaseAuth;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_perfil, container, false);

        inicializarRecyclerView();

        firebaseAuth = FirebaseAuth.getInstance();
        txtNombre = rootView.findViewById(R.id.txtNombre);

        String current = firebaseAuth.getCurrentUser().getEmail();
        txtNombre.setText(current);



        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return rootView;
    }

    private void inicializarRecyclerView(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ArrayList<Post> posts=new ArrayList<Post>();
        firebaseFirestore.collection("Publicaciones").document(firebaseAuth.getCurrentUser().getEmail()).collection("Publicacion").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Post post=new Post();
                        post.setCorreoUsuario(documentSnapshot.getString("Email"));
                        post.setDescripcion("Prueba");
                        posts.add(post);
                    }
                }
            });
        recyclerView.setAdapter(new AdaptadorPosts(getContext(),posts,firebaseAuth));
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
}
