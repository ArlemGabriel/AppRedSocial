package com.example.appredsocial;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NuevoPost extends AppCompatActivity {

    Button btnAceptar;
    ImageView imgFotoPublicacion;
    TextView txtDescripcion;
    private Uri uriImagen;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference refStorage;
    private FirebaseFirestore refFirestore;
    private FirebaseAuth firebaseAuth;
    private ProgressBar prbSubidaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_post);

        btnAceptar = findViewById(R.id.btnPublicar);
        imgFotoPublicacion = findViewById(R.id.imgPublicacion);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        prbSubidaDatos = findViewById(R.id.progress_bar_publicacion);

        refStorage = FirebaseStorage.getInstance().getReference(ReferenciasFirebase.REFERENCIA_FOTOS_POSTS);
        refFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String descripcion = txtDescripcion.getText().toString();
                guardarInformacionBD(descripcion);
            }
        });

        imgFotoPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirBusquedaImg();
            }
        });
    }

    private void abrirBusquedaImg(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data !=null && data.getData() != null){
            uriImagen = data.getData();
            Picasso.with(this).load(uriImagen).into(imgFotoPublicacion);
        }
    }

    private void guardarInformacionBD(final String descripcion){

        final Map<String, Object> nuevoPost = new HashMap<>();

        if(descripcion.isEmpty() && uriImagen == null) {
            Toast.makeText(NuevoPost.this, "Debe ingresar una descripción o subir una foto", Toast.LENGTH_LONG).show();
        }else{
            if (uriImagen != null){
                final StorageReference refArchivo = refStorage.child(System.currentTimeMillis()+
                        "."+obtenerExtensionArchivo(uriImagen));
                refArchivo.putFile(uriImagen)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        prbSubidaDatos.setProgress(0);
                                    }
                                },500);

                                Toast.makeText(NuevoPost.this,"Carga Exitosa",Toast.LENGTH_LONG).show();


                                refArchivo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        String email = user.getEmail();

                                        Uri url= uri;
                                        String pathUrl = url.toString();
                                        int anno=getYear();
                                        int mes=getMonth();
                                        int dia=getDay();
                                        int hora=getHour();
                                        int minutos=getMinute();
                                        int segundos=getSecond();
                                        String idPost=String.valueOf(anno)+String.valueOf(mes)+String.valueOf(dia)+String.valueOf(hora)+String.valueOf(segundos);

                                        nuevoPost.put("id", idPost);
                                        nuevoPost.put("EmailUsuario", firebaseAuth.getCurrentUser().getEmail());
                                        nuevoPost.put("Descripcion", descripcion);
                                        nuevoPost.put("Likes", 0);
                                        nuevoPost.put("Dislikes", 0);
                                        nuevoPost.put("Anno", anno);
                                        nuevoPost.put("Mes", mes);
                                        nuevoPost.put("Dia", dia);
                                        nuevoPost.put("Hora", hora);
                                        nuevoPost.put("Minutos", minutos);
                                        nuevoPost.put("Segundos", segundos);
                                        nuevoPost.put("ImgUrl",pathUrl);

                                        DocumentReference reference = refFirestore.collection(ReferenciasFirebase.REFERENCIA_POSTS).document(firebaseAuth.getCurrentUser().getEmail()).collection("Post").document(idPost);

                                        reference.set(nuevoPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Intent intent = new Intent(NuevoPost.this,MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else{
                                                    Toast.makeText(NuevoPost.this,"Error al publicar post",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NuevoPost.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                btnAceptar.setEnabled(false);
                                double progreso = (100.00*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                prbSubidaDatos.setProgress((int) progreso);
                            }
                        });

            }else{
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String email = user.getEmail();

                String pathUrl = "null";
                int anno=getYear();
                int mes=getMonth();
                int dia=getDay();
                int hora=getHour();
                int minutos=getMinute();
                int segundos=getSecond();
                String idPost=String.valueOf(anno)+String.valueOf(mes)+String.valueOf(dia)+String.valueOf(hora)+String.valueOf(segundos);

                nuevoPost.put("id", idPost);
                nuevoPost.put("EmailUsuario", firebaseAuth.getCurrentUser().getEmail());
                nuevoPost.put("Descripcion", descripcion);
                nuevoPost.put("Likes", 0);
                nuevoPost.put("Dislikes", 0);
                nuevoPost.put("Anno", anno);
                nuevoPost.put("Mes", mes);
                nuevoPost.put("Dia", dia);
                nuevoPost.put("Hora", hora);
                nuevoPost.put("Minutos", minutos);
                nuevoPost.put("Segundos", segundos);
                nuevoPost.put("ImgUrl",pathUrl);

                DocumentReference reference = refFirestore.collection(ReferenciasFirebase.REFERENCIA_POSTS).document(firebaseAuth.getCurrentUser().getEmail()).collection("Post").document(idPost);

                reference.set(nuevoPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(NuevoPost.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(NuevoPost.this,"Error al publicar post",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    private String obtenerExtensionArchivo(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    //---------------------- Obtener Fecha ---------------------------
    private int getHour(){
        Calendar c = Calendar.getInstance();
        int Hora = c.get(Calendar.HOUR_OF_DAY);
        return Hora;
    }
    private int getMinute(){
        Calendar c = Calendar.getInstance();
        int Minuto = c.get(Calendar.MINUTE);
        return Minuto;
    }
    private int getDay(){
        Calendar c = Calendar.getInstance();
        int Dia = c.get(Calendar.DAY_OF_MONTH);
        return Dia;
    }
    private int getMonth(){
        Calendar c = Calendar.getInstance();
        int Mes = c.get(Calendar.MONTH)+1;
        return Mes;
    }
    private int getYear(){
        Calendar c = Calendar.getInstance();
        int Año = c.get(Calendar.YEAR);
        return Año;
    }
    private int getSecond(){
        Calendar c = Calendar.getInstance();
        int Segundo = c.get(Calendar.SECOND);
        return Segundo;
    }
}
