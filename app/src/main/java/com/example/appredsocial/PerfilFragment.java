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

import com.google.firebase.auth.FirebaseAuth;
import com.synnapps.carouselview.CarouselView;

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

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        firebaseAuth = FirebaseAuth.getInstance();
        txtNombre = rootView.findViewById(R.id.txtNombre);

        String current = firebaseAuth.getCurrentUser().getEmail();
        txtNombre.setText(current);

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
}
