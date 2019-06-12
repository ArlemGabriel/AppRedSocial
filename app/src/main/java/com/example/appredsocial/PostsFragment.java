package com.example.appredsocial;

import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    final ArrayList<Post> postsCargados=new ArrayList<Post>();
    ArrayList<String> amigos=new ArrayList<>();
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

        inicializarRecyclerView();
        recyclerView.setLayoutManager(linearLayoutManager);

        noPublicaciones= rootView.findViewById(R.id.noPublicacionesPosts);

        adaptadorPosts=new AdaptadorPosts(getContext(),postsCargados,firebaseAuth, refFirestore);
        recyclerView.setAdapter(adaptadorPosts);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Collections.sort(posts, new Comparator<Post>() {
                    @Override
                    public int compare(Post rhs, Post lhs) {
                        return (Integer.valueOf(lhs.getIdPost())).compareTo(Integer.valueOf(rhs.getIdPost()));
                    }
                });
                cargarPosts();
            }
        }, 1000);

        //Detectar cuando el recycler view esta al final del scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(adaptadorPosts.getItemCount()<cantPublicaciones)
                                cargarPosts();
                        }
                    },2000);
                }
            }
        });

        return rootView;
    }

    private void inicializarRecyclerView(){
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Amigos").document(firebaseAuth.getCurrentUser().getEmail()).collection("Amigo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<String> a=new ArrayList<>();
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            a.add(documentSnapshot.getId());
                        }
                        amigos=a;
                        for(String amigo : amigos) {
                            firebaseFirestore.collection("Posts").document(amigo).collection("Post").get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            int cont = 0;
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                cont++;
                                            }
                                            cantPublicaciones = cont;
                                        }
                                    });

                            firebaseFirestore.collection("Posts").document(amigo).collection("Post").orderBy("id", Query.Direction.DESCENDING).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                noPublicaciones.setVisibility(View.INVISIBLE);
                                                Post post = new Post();
                                                post.setCorreoUsuario(documentSnapshot.getString("EmailUsuario"));
                                                post.setDescripcion(documentSnapshot.getString("Descripcion"));
                                                post.setAnno(Integer.valueOf(documentSnapshot.get("Anno").toString()));
                                                post.setMes(Integer.valueOf(documentSnapshot.get("Mes").toString()));
                                                post.setDia(Integer.valueOf(documentSnapshot.get("Dia").toString()));
                                                post.setHora(Integer.valueOf(documentSnapshot.get("Hora").toString()));
                                                post.setMinutos(Integer.valueOf(documentSnapshot.get("Minutos").toString()));
                                                post.setSegundos(Integer.valueOf(documentSnapshot.get("Segundos").toString()));
                                                post.setCantLikes(Integer.valueOf(documentSnapshot.get("Likes").toString()));
                                                post.setCantDislikes(Integer.valueOf(documentSnapshot.get("Dislikes").toString()));
                                                post.setUrlImagen(documentSnapshot.getString("ImgUrl"));
                                                post.setIdPost(documentSnapshot.getString("id"));
                                                ultimaCarga = documentSnapshot;
                                                posts.add(post);
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    public void cargarPosts(){
        if(!posts.isEmpty()){
            int cont=0;
            for(Post post :posts){
                postsCargados.add(post);
                cont++;

                if(cont==10)
                    break;
            }
            for(Post post: postsCargados){
                if(posts.contains(post))
                    posts.remove(post);
            }
            adaptadorPosts.notifyDataSetChanged();
        }
    }
}
