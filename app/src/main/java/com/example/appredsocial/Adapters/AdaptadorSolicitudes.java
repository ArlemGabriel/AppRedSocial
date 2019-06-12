package com.example.appredsocial.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appredsocial.Objetos.Solicitud;
import com.example.appredsocial.R;
import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorSolicitudes extends RecyclerView.Adapter<AdaptadorSolicitudes.ContenedorSolicitudes>{

    private Context contexto;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Solicitud> solicitudes;

    public AdaptadorSolicitudes(Context contexto, ArrayList<Solicitud> solicitudes, FirebaseFirestore firebaseFirestore, FirebaseAuth firebaseAuth){
        this.contexto = contexto;
        this.solicitudes = solicitudes;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public ContenedorSolicitudes onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contexto).inflate(R.layout.item_solicitudes,parent,false);
        return new ContenedorSolicitudes(v);
    }

    @Override
    public void onBindViewHolder(final ContenedorSolicitudes holder, int position) {

        firebaseFirestore.collection(ReferenciasFirebase.REFERENCIA_PERFILES).document(solicitudes.get(position).getCorreo()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String nombreCompleto = documentSnapshot.getString("Nombre")+" "+documentSnapshot.getString("Apellidos");
                        holder.textViewNombre.setText(nombreCompleto);
                        String urlFirebase = documentSnapshot.getString("Url");
                        if(!urlFirebase.isEmpty()){
                            Picasso.with(contexto).load(urlFirebase).fit().centerCrop().into(((ContenedorSolicitudes) holder).imageView);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Error: ",e.getMessage());
                    }
                });



    }

    @Override
    public int getItemCount() {
        return solicitudes.size();
    }

    public Context getContexto() {
        return contexto;
    }
    /*public void deleteItem(int position){

        String key = keys.get(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(ReferenciasFirebase.REFERENCIA_BASEDATOSFIREBASE);
        ref.child(key).removeValue();
    }*/
    public class ContenedorSolicitudes extends RecyclerView.ViewHolder{
        public TextView textViewNombre;
        public ImageView imageView;
        public ContenedorSolicitudes(View itemView){
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.nombreSolicitudAmigo);
            imageView = itemView.findViewById(R.id.imageViewSolicitud);
        }
    }
}

