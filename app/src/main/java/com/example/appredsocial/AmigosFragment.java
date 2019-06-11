package com.example.appredsocial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appredsocial.Adapters.AdaptadorAmigos;

import java.util.ArrayList;
import java.util.List;

public class AmigosFragment extends Fragment {
    View rootView;

    //Esta es la lista de nombres para el recycler view de amigos
    ArrayList<String> nombres = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_amigos,container,false);

        RecyclerView recyclerView = rootView.findViewById(R.id.RecyclerView);

        /*Se agregan los objetos desde la base de datos a la lista de nombres*/
        nombres.add("Alejandro Garita");
        nombres.add("Arlem Brenes");
        nombres.add("Fabian Piedra");

        final AdaptadorAmigos adaptador = new AdaptadorAmigos(this.getContext(), nombres);

        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int posicion = viewHolder.getAdapterPosition();
                nombres.remove(posicion);

                adaptador.notifyItemRemoved(posicion);
                adaptador.notifyItemRangeChanged(posicion, adaptador.getItemCount());

                adaptador.deleteAmigo(posicion);
            }
        }).attachToRecyclerView(recyclerView);

        return rootView;
    }

}
