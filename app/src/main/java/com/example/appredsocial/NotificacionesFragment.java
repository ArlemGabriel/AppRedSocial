package com.example.appredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appredsocial.Adapters.AdaptadorSolicitudes;
import com.example.appredsocial.Objetos.Solicitud;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificacionesFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;
    private ArrayList<Solicitud> solicitudes = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private AdaptadorSolicitudes adaptador;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_notificaciones, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewSolicitudes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        cargarSolicitudes();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adaptador.notifyDataSetChanged();
            }
        }, 500);
        return rootView;
    }
    public void cargarSolicitudes(){
        firebaseFirestore.collection("Solicitudes")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .collection("Pendientes")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String correo = documentSnapshot.getString("Correo");
                    Solicitud solicitud = new Solicitud();
                    solicitud.setCorreo(correo);
                    solicitudes.add(solicitud);
                }
            }
        });
        adaptador = new AdaptadorSolicitudes(getContext(),solicitudes,firebaseFirestore,firebaseAuth);
        recyclerView.setAdapter(adaptador);

    }


}