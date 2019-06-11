package com.example.appredsocial;

import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appredsocial.Objetos.Post;
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

    public AdaptadorPosts (Context context, ArrayList<Post> posts, FirebaseAuth firebaseAuth){
        this.context=context;
        this.posts=posts;
        this.firebaseAuth=firebaseAuth;
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
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        StorageReference firebaseStorage=FirebaseStorage.getInstance().getReference();
        final String[] nombre = {"*Nombre de usuario no encontrado*"};
        final String[] urlImagen = new String[1];
        int tiempoDesdePublicacion;
        firebaseFirestore.collection("Perfiles").document(posts.get(position).getCorreoUsuario()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        nombre[0] =documentSnapshot.getString("Nombre")+documentSnapshot.getString("Apellidos");
                        urlImagen[0] =documentSnapshot.getString("Url");
                    }
                });

        try {
            if(!urlImagen[0].isEmpty() && urlImagen!=null) {
                StorageReference imgFotoPerfilRef = firebaseStorage.child(urlImagen[0]);
                final File imgFotoPerfil = File.createTempFile("images", "jpg");
                imgFotoPerfilRef.getFile(imgFotoPerfil)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Uri uri = Uri.fromFile(imgFotoPerfil);
                                ((Item) holder).fotoPerfil.setBackground(null);
                                ((Item) holder).fotoPerfil.setImageURI(uri);
                            }
                        });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        ((Item)holder).nombre.setText(nombre[0]);
        ((Item)holder).descripcion.setText(posts.get(position).getDescripcion());

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
