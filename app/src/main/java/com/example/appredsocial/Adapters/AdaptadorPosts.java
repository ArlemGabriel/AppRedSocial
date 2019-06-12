package com.example.appredsocial.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appredsocial.Objetos.Post;
import com.example.appredsocial.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AdaptadorPosts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Post>  posts;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    public AdaptadorPosts (Context context, ArrayList<Post> posts, FirebaseAuth firebaseAuth, FirebaseFirestore firebaseFirestore){
        this.context=context;
        this.posts=posts;
        this.firebaseAuth=firebaseAuth;
        this.firebaseFirestore=firebaseFirestore;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View row= inflater.inflate(R.layout.item_post, parent,false);
        Item item=new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final StorageReference firebaseStorage=FirebaseStorage.getInstance().getReference();
        firebaseFirestore.collection("Perfiles").document(posts.get(position).getCorreoUsuario()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                          @Override
                                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                                              String nombre = documentSnapshot.getString("Nombre") + " " + documentSnapshot.getString("Apellidos");
                                              ((Item) holder).nombre.setText(nombre);
                                              String urlImagen = documentSnapshot.getString("Url");
                                              if(!urlImagen.isEmpty() && urlImagen!="null")
                                                Picasso.with(context).load(urlImagen).fit().centerCrop().into(((Item) holder).fotoPerfil);
                                          }
                                      }
                )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error= e.getMessage();
                        Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                    }
                });

        ((Item)holder).descripcion.setText(posts.get(position).getDescripcion());
        ((Item)holder).tiempoPublicacion.setText(posts.get(position).tiempoDePublicacion());
        String url="";
        url=posts.get(position).getUrlImagen();
        if(!url.isEmpty() && !url.equals("null")) {
            ((Item)holder).imagenPublicacion.getLayoutParams().height=450;
            ((Item)holder).imagenPublicacion.requestLayout();
            Picasso.with(context).load(url).centerInside().fit().into(((Item) holder).imagenPublicacion);
        }else
            ((Item)holder).imagenPublicacion.setImageURI(null);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class Item extends RecyclerView.ViewHolder{
        TextView nombre, tiempoPublicacion, descripcion;
        ImageView fotoPerfil,imagenPublicacion;
        public Item(View itemView) {
            super(itemView);
            nombre= (TextView) itemView.findViewById(R.id.textNombreUsuario);
            tiempoPublicacion= (TextView) itemView.findViewById(R.id.textTimePassed);
            descripcion= (TextView) itemView.findViewById(R.id.textNewPostDescription);
            fotoPerfil=(ImageView) itemView.findViewById(R.id.imgUsuarioPost);
            imagenPublicacion=(ImageView) itemView.findViewById(R.id.imgNewPost);
        }
    }
}
