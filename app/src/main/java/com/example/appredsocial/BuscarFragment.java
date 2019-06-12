package com.example.appredsocial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.appredsocial.Adapters.AdaptadorAmigos;
import com.example.appredsocial.Objetos.Amigo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BuscarFragment extends Fragment {

    View rootView;
    private EditText txtBusqueda;
    private Button btnBusqueda;

    LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    ArrayList<Amigo> perfiles = new ArrayList<Amigo>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_buscar,container,false);

        txtBusqueda = rootView.findViewById(R.id.txtBusquedaPerfiles);
        btnBusqueda = rootView.findViewById(R.id.btnBusquedaPerfiles);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = rootView.findViewById(R.id.recyclerViewPerfil);

        btnBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfiles = new ArrayList<Amigo>();
                final String busca = txtBusqueda.getText().toString();

                firebaseFirestore.collection("Perfiles").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if(documentSnapshot.getString("Nombre").contains(busca) |
                                    documentSnapshot.getString("Apellidos").contains(busca)) {
                                Amigo perfil = new Amigo();
                                perfil.setNombre(documentSnapshot.getString("Nombre"));
                                perfil.setApellido(documentSnapshot.getString("Apellidos"));
                                perfil.setUrlImagen(documentSnapshot.getString("Url"));
                                perfiles.add(perfil);
                            }
                        }
                        linearLayoutManager = new LinearLayoutManager(getContext());

                        recyclerView.setLayoutManager(linearLayoutManager);

                        AdaptadorAmigos adapter = new AdaptadorAmigos(getContext(), perfiles, firebaseAuth, firebaseFirestore);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        });
        return  rootView;
    }
}
