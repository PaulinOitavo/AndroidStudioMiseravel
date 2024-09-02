package com.example.tcctela;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button cadastro = findViewById(R.id.bt_criarConta);

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Cadastro.this, Login.class);

                startActivity(intent);
            }
        });
    }












}