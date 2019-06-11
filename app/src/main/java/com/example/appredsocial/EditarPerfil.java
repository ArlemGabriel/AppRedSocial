package com.example.appredsocial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditarPerfil extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btnAceptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        btnAceptar = findViewById(R.id.btnAceptar);
        radioGroup = findViewById(R.id.radio_group);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarPerfil.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }
}
