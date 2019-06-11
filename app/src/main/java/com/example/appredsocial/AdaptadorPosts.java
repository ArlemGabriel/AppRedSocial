package com.example.appredsocial;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appredsocial.Objetos.Post;

public class AdaptadorPosts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Post[] posts;

    public AdaptadorPosts (Context context, Post[] posts){
        this.context=context;
        this.posts=posts;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View row= inflater.inflate(R.layout.item_post, parent,false);
        Item item=new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //((Item)holder).nombre.setText();
    }

    @Override
    public int getItemCount() {
        return posts.length;
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
