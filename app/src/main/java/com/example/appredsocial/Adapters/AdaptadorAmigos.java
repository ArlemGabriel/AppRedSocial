package com.example.appredsocial.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appredsocial.R;

import java.util.List;

public class AdaptadorAmigos extends
        RecyclerView.Adapter<AdaptadorAmigos.ContenedorAmigos> {

    private Context contexto;
    private List<String> mNombres;

    public AdaptadorAmigos(Context context, List<String> nombres){
        contexto = context;
        mNombres = nombres;
    }

    @Override
    public ContenedorAmigos onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View vista = inflater.inflate(R.layout.item_amigos, parent, false);

        ContenedorAmigos viewHolder = new ContenedorAmigos(vista);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContenedorAmigos holder, int position) {
        String nombre = mNombres.get(position);
        holder.textNombre.setText(nombre);
        //holder.imgFoto.setImageResource();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void deleteAmigo(int posicion){
        //Borrar amigo de la base de datos
    }

    public class ContenedorAmigos extends RecyclerView.ViewHolder{
        public TextView textNombre;
        public ImageView imgFoto;

        public ContenedorAmigos(View viewItem){
            super(viewItem);

            textNombre = viewItem.findViewById(R.id.txtNombreAmigos);
            imgFoto = viewItem.findViewById(R.id.imgFotoAmigos);

        }
    }

}
