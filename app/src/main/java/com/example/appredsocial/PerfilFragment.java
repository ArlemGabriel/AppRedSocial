package com.example.appredsocial;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;

import java.util.ArrayList;
import java.util.List;

public class PerfilFragment extends Fragment {
    View rootView;
    private CarouselView carouselView;
    private TextView txtNombre,txtCiudad,txtTelefono,txtCorreo,txtGenero,txtPrimaria,txtSecundaria,txtUniversidad;
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
        txtNombre = rootView.findViewById(R.id.txtNombre);
        txtCiudad = rootView.findViewById(R.id.txtCiudad);
        txtCorreo = rootView.findViewById(R.id.txtCorreo);
        txtTelefono = rootView.findViewById(R.id.txtTelefono);
        txtPrimaria = rootView.findViewById(R.id.txtPrimaria);
        txtSecundaria = rootView.findViewById(R.id.txtSecundaria);
        txtUniversidad = rootView.findViewById(R.id.txtUniversidad);
        imageViewFotoPerfil = rootView.findViewById(R.id.imgPerfil);

        refStorage = FirebaseStorage.getInstance().getReference(ReferenciasFirebase.REFERENCIA_FOTOS_PERFIL);
        refFirestore = FirebaseFirestore.getInstance();

        cargarPerfil();
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
