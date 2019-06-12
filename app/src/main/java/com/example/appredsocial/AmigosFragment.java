package com.example.appredsocial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appredsocial.Adapters.AdaptadorAmigos;
import com.example.appredsocial.Objetos.Amigo;
import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AmigosFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EditText txtBuscar;
    private Button btnBuscar;

    private FirebaseFirestore refFirestore;
    private FirebaseAuth firebaseAuth;

    private AdaptadorAmigos adaptadorAmigos;
    final ArrayList<Amigo> amigos = new ArrayList<Amigo>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_amigos,container,false);

        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = rootView.findViewById(R.id.RecyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());

        inicializarRecyclerView();
        recyclerView.setLayoutManager(linearLayoutManager);

        txtBuscar = rootView.findViewById(R.id.txtNombreAmigos);
        btnBuscar = rootView.findViewById(R.id.btnBusquedaAmigos);

        refFirestore = FirebaseFirestore.getInstance();

        return rootView;
    }

    private void inicializarRecyclerView(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Amigos").document(firebaseAuth.getCurrentUser().
                getEmail()).collection("Amigo").get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            final Amigo amigo = new Amigo();

                            String mailAmigo = documentSnapshot.getId();

                            refFirestore.collection("Perfiles").document(mailAmigo).
                                    get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot2) {
                                    String nombre = documentSnapshot2.getString("Nombre");
                                    amigo.setNombre(nombre);
                                    String apellido = documentSnapshot2.getString("Apellidos");
                                    amigo.setApellido(apellido);
                                    String url = documentSnapshot2.getString("Url");
                                    amigo.setUrlImagen(url);
                                    Log.i("Info", amigo.getNombreCompleto());
                                }
                            });
                            amigos.add(amigo);
                        }
                    }
                });

        adaptadorAmigos = new AdaptadorAmigos(getContext(),amigos,firebaseAuth,firebaseFirestore);
        recyclerView.setAdapter(adaptadorAmigos);
    }
}
