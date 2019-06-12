package com.example.appredsocial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appredsocial.Adapters.AdaptadorSolicitudes;
import com.example.appredsocial.Objetos.Solicitud;
import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==4){
                    final int posicion = viewHolder.getAdapterPosition();
                    firebaseFirestore.collection("Solicitudes")
                            .document(firebaseAuth.getCurrentUser().getEmail())
                            .collection("Pendientes").document(solicitudes.get(posicion).getId()).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    solicitudes.remove(solicitudes.get(posicion));
                                    adaptador.notifyDataSetChanged();
                                    Toast.makeText(getContext(),"Solicitud de Amistad Rechazada",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                if(direction==8){
                    final int posicion = viewHolder.getAdapterPosition();
                    final Map<String, Object> amigo = new HashMap<>();
                    amigo.put("Correo",solicitudes.get(posicion).getCorreo());
                    firebaseFirestore.collection("Amigos")
                            .document(firebaseAuth.getCurrentUser().getEmail())
                            .collection("Amigo")
                            .document(solicitudes.get(posicion).getCorreo()).set(amigo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    adaptador.notifyDataSetChanged();
                                    Toast.makeText(getContext(),"Solicitud de Amistad Aceptada",Toast.LENGTH_SHORT).show();
                                }
                            });
                    firebaseFirestore.collection("Solicitudes")
                            .document(firebaseAuth.getCurrentUser().getEmail())
                            .collection("Pendientes").document(solicitudes.get(posicion).getId()).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    solicitudes.remove(solicitudes.get(posicion));
                                    adaptador.notifyDataSetChanged();
                                }
                            });

                }

            }
        }).attachToRecyclerView(recyclerView);
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
                    String id = documentSnapshot.getId();
                    String correo = documentSnapshot.getString("Correo");
                    Solicitud solicitud = new Solicitud();
                    solicitud.setCorreo(correo);
                    solicitud.setId(id);
                    solicitudes.add(solicitud);
                }
            }
        });
        adaptador = new AdaptadorSolicitudes(getContext(),solicitudes,firebaseFirestore,firebaseAuth);
        recyclerView.setAdapter(adaptador);

    }


}