package com.example.tcctela;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Cadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().hide();
    }
}