package com.example.appredsocial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appredsocial.Adapters.AdaptadorPosts;
import com.example.appredsocial.Objetos.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PostsFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private FirebaseFirestore refFirestore;
    private FirebaseAuth firebaseAuth;
    int cantPublicaciones;

    TextView noPublicaciones;
    private AdaptadorPosts adaptadorPosts;
    final ArrayList<Post> posts=new ArrayList<Post>();
    DocumentSnapshot ultimaCarga;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_posts, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        refFirestore = FirebaseFirestore.getInstance();

        recyclerView = rootView.findViewById(R.id.recyclerViewPosts);
        linearLayoutManager = new LinearLayoutManager(getContext());

        //inicializarRecyclerView();
        recyclerView.setLayoutManager(linearLayoutManager);

        noPublicaciones= rootView.findViewById(R.id.noPublicacionesPosts);

        return rootView;
    }
}
