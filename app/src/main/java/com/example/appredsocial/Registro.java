package com.example.appredsocial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Registro extends AppCompatActivity {
    TextView txtAtras;
    Button btnRegistrarseEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        txtAtras = findViewById(R.id.txtAtras);
        btnRegistrarseEmail = findViewById(R.id.btnRegistrarseEmail);

        txtAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro.this,Logueo.class);
                startActivity(intent);
            }
        });


        btnRegistrarseEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro.this,RegistrarseEmail.class);
                startActivity(intent);
            }
        });
        /*btnRegistrarseEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro.this,RegistrarseEmail.class);
                startActivity(intent);
            }
        });*/
    }


}
