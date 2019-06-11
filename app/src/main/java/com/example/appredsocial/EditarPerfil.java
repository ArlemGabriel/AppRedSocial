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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appredsocial.Referencias.ReferenciasFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditarPerfil extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btnAceptar;
    ImageView imgFotoPerfil;
    TextView txtNombre,txtApellidos,txtCiudad,txtTelefono,txtFechaNac,txtPrimaria,txtSecundaria,txtUniversidad;
    private Uri uriImagen;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference refStorage;
    private FirebaseFirestore refUsuarioBD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        btnAceptar = findViewById(R.id.btnAceptar);
        radioGroup = findViewById(R.id.radio_group);
        imgFotoPerfil = findViewById(R.id.imgFotoperfil);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtFechaNac = findViewById(R.id.txtFechaNac);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtPrimaria = findViewById(R.id.txtPrimaria);
        txtSecundaria = findViewById(R.id.txtSecundaria);
        txtUniversidad = findViewById(R.id.txtUniversidad);

        refStorage = FirebaseStorage.getInstance().getReference(ReferenciasFirebase.REFERENCIA_FOTOS_PERFIL);
        refUsuarioBD = FirebaseFirestore.getInstance();

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(EditarPerfil.this, MainActivity.class);
                startActivity(intent);*/
                guardarInformacionBD();
            }
        });

        imgFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirBusquedaImg();
            }
        });


    }
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
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
            Picasso.with(this).load(uriImagen).into(imgFotoPerfil);

        }
    }
    private void guardarInformacionBD(){
        final String datosNombre = txtNombre.getText().toString();
        final String datosApellidos = txtApellidos.getText().toString();
        final String datosCiudad = txtCiudad.getText().toString();
        final String datosTelefono = txtTelefono.getText().toString();
        final String datosFechaNac = txtFechaNac.getText().toString();
        final String datosPrimaria = txtPrimaria.getText().toString();
        final String datosSecundaria = txtSecundaria.getText().toString();
        final String datosUniversidad = txtUniversidad.getText().toString();

        if(datosNombre.isEmpty() || datosApellidos.isEmpty() || datosFechaNac.isEmpty()) {
            Toast.makeText(EditarPerfil.this, "Nombre,Apellidos y Fecha de Nacimiento son campos obligatorios", Toast.LENGTH_LONG).show();
        }else{
            if (uriImagen != null){
                final StorageReference refArchivo = refStorage.child(System.currentTimeMillis()+
                        "."+obtenerExtensionArchivo(uriImagen));
                refArchivo.putFile(uriImagen)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(EditarPerfil.this,"Carga Exitosa",Toast.LENGTH_LONG).show();
                                refArchivo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri url= uri;
                                        String datosUrl = url.toString();
                                        //refUsuarioBD.
                                        //refUsuarioBD.push().setValue(nuevoUsuario);
                                        //Productos prd = new Productos(datosNombre,datosPrecio,datosDescripcion,datosUrl);
                                        //refDatabase.push().setValue(prd);

                                        //Intent intent = new Intent(AgregarProducto.this,MainActivity.class);
                                       // startActivity(intent);

                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditarPerfil.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

            }else{
                Toast.makeText(this,"Seleccione una imagen",Toast.LENGTH_LONG).show();
            }
        }
    }

    private String obtenerExtensionArchivo(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}
