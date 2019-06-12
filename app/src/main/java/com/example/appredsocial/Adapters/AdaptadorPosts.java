package com.example.appredsocial.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appredsocial.ComentarioActivity;
import com.example.appredsocial.Objetos.Post;
import com.example.appredsocial.PerfilActivity;
import com.example.appredsocial.R;
import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
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
        ((Item)holder).cantLikes.setText(String.valueOf(posts.get(position).getCantLikes()));
        ((Item)holder).cantDislikes.setText(String.valueOf(posts.get(position).getCantDislikes()));//*/

        ((Item)holder).comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(context, ComentarioActivity.class);
                i.putExtra("Email", posts.get(position).getCorreoUsuario());
                i.putExtra("idPost", posts.get(position).getIdPost());
                context.startActivity(i);
            }
        });

        ((Item)holder).nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPerfil(posts.get(position).getCorreoUsuario());
            }
        });

        ((Item)holder).fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPerfil(posts.get(position).getCorreoUsuario());
            }
        });
        ((Item)holder).darLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DocumentReference reference = firebaseFirestore.collection(ReferenciasFirebase.REFERENCIA_POSTS).document(posts.get(position).getCorreoUsuario()).collection("Post").document(posts.get(position).getIdPost());
                final int cant=posts.get(position).getCantLikes()+1;
                final String emailCurrentUser=firebaseAuth.getCurrentUser().getEmail();

                if(cant-1>0) {
                    reference.collection("Likes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean existe=false;
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                if(emailCurrentUser.equals(documentSnapshot.getId()))
                                    existe =true;
                            }

                            if(!existe){
                                Map<String, Object> publicacion = new HashMap<>();
                                publicacion.put("Likes", cant);

                                Map<String, Object> usuario = new HashMap<>();
                                usuario.put("Email", emailCurrentUser);


                                reference.collection("Likes").document(emailCurrentUser).set(usuario);

                                reference.update(publicacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Se ha dado me gusta", Toast.LENGTH_SHORT).show();
                                        posts.get(position).setCantLikes(cant);
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }else
                                Toast.makeText(context, "Ya ha dado me gusta a esta publicación", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                else {
                    Map<String, Object> publicacion = new HashMap<>();
                    publicacion.put("Likes", cant);

                    Map<String, Object> usuario = new HashMap<>();
                    usuario.put("Email", emailCurrentUser);


                    reference.collection("Likes").document(emailCurrentUser).set(usuario);

                    reference.update(publicacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Se ha dado me gusta", Toast.LENGTH_SHORT).show();
                            posts.get(position).setCantLikes(cant);
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

        ((Item)holder).darDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DocumentReference reference = firebaseFirestore.collection(ReferenciasFirebase.REFERENCIA_POSTS).document(posts.get(position).getCorreoUsuario()).collection("Post").document(String.valueOf(posts.get(position).getIdPost()));
                final String emailCurrentUser=firebaseAuth.getCurrentUser().getEmail();
                final int cant=posts.get(position).getCantDislikes()+1;

                if(cant-1>0) {
                    reference.collection("Dislikes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean existe=false;
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                if(emailCurrentUser.equals(documentSnapshot.getId()))
                                    existe =true;
                            }

                            if(!existe){
                                Map<String, Object> publicacion = new HashMap<>();
                                publicacion.put("Dislikes", cant);

                                Map<String, Object> usuario = new HashMap<>();
                                usuario.put("Email", firebaseAuth.getCurrentUser().getEmail());



                                reference.collection("Dislikes").document(firebaseAuth.getCurrentUser().getEmail()).set(usuario);
                                reference.update(publicacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"Se ha dado no me gusta",Toast.LENGTH_SHORT).show();
                                        posts.get(position).setCantDislikes(cant);
                                    }
                                });
                            }
                            else
                                Toast.makeText(context, "Ya ha dado no me gusta a esta publicación", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                else {
                    Map<String, Object> publicacion = new HashMap<>();
                    publicacion.put("Dislikes", cant);

                    Map<String, Object> usuario = new HashMap<>();
                    usuario.put("Email", firebaseAuth.getCurrentUser().getEmail());


                    reference.collection("Dislikes").document(firebaseAuth.getCurrentUser().getEmail()).set(usuario);
                    reference.update(publicacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Se ha dado no me gusta", Toast.LENGTH_SHORT).show();
                            posts.get(position).setCantDislikes(cant);
                        }
                    });
                }

            }
        });


        String url="";
        url=posts.get(position).getUrlImagen();
        if(!url.isEmpty() && !url.equals("null")) {
            ((Item)holder).imagenPublicacion.getLayoutParams().height=450;
            ((Item)holder).imagenPublicacion.requestLayout();
            Picasso.with(context).load(url).centerInside().fit().into(((Item) holder).imagenPublicacion);
        }else
            ((Item)holder).imagenPublicacion.setImageURI(null);
    }

    private void abrirPerfil(String correo){
        Intent i = new Intent(context, PerfilActivity.class);
        i.putExtra("Email",correo);
        context.startActivity(i);
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class Item extends RecyclerView.ViewHolder{
        TextView nombre, tiempoPublicacion, descripcion, cantLikes, cantDislikes;
        ImageView fotoPerfil,imagenPublicacion, darLike, darDislike, comentar;
        public Item(View itemView) {
            super(itemView);
            comentar= itemView.findViewById(R.id.agregarComentario);
            cantLikes = itemView.findViewById(R.id.cantLikes);
            cantDislikes = itemView.findViewById(R.id.cantDislikes);
            darLike=itemView.findViewById(R.id.btnLike);
            darDislike=itemView.findViewById(R.id.btnDislike);//*/
            nombre= (TextView) itemView.findViewById(R.id.textNombreUsuario);
            tiempoPublicacion= (TextView) itemView.findViewById(R.id.textTimePassed);
            descripcion= (TextView) itemView.findViewById(R.id.textNewPostDescription);
            fotoPerfil=(ImageView) itemView.findViewById(R.id.imgUsuarioPost);
            imagenPublicacion=(ImageView) itemView.findViewById(R.id.imgNewPost);
        }
    }
}
