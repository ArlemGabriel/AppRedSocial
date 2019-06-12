package com.example.appredsocial.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appredsocial.Objetos.Amigo;
import com.example.appredsocial.PerfilActivity;
import com.example.appredsocial.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorAmigos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Amigo> amigos;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    public AdaptadorAmigos(Context context, ArrayList<Amigo> amigos, FirebaseAuth firebaseAuth,
                           FirebaseFirestore firebaseFirestore){
        this.context = context;
        this.amigos = amigos;
        this.firebaseAuth = firebaseAuth;
        this.firebaseFirestore = firebaseFirestore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.i("Info", "Inicializa");
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.item_amigos, parent, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((Item)holder).textNombre.setText(amigos.get(position).getNombreCompleto());
        String url = amigos.get(position).getUrlImagen();
        
            ((Item)holder).imgFoto.getLayoutParams().height=450;
            ((Item)holder).imgFoto.requestLayout();
            Picasso.with(context).load(url).centerInside().fit().into(((Item)holder).imgFoto);

            ((Item)holder).imgFoto.setImageURI(null);

        ((Item)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPerfil(amigos.get(position).getEmail());
            }
        });
    }

    @Override
    public int getItemCount() {return amigos.size(); }

    public void deleteAmigo(int posicion){
        //Borrar amigo de la base de datos
    }
    private void abrirPerfil(String correo){
        Intent i = new Intent(context, PerfilActivity.class);
        i.putExtra("Email",correo);
        context.startActivity(i);
    }
    public class Item extends RecyclerView.ViewHolder{
        TextView textNombre;
        ImageView imgFoto;

        public Item(View viewItem){
            super(viewItem);

            textNombre = (TextView) viewItem.findViewById(R.id.txtNombreAmigos);
            imgFoto = (ImageView) viewItem.findViewById(R.id.imgFotoAmigos);
        }
    }


}
