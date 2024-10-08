package com.example.tcctela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class Cadastro extends AppCompatActivity {

    private EditText edit_nome, edit_email, edit_senha;
    private Button bt_criarConta;
    private String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso", "Erro ao cadastrar usuário"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicializa os componentes da tela
        IniciarComponentes();

        bt_criarConta.setOnClickListener(v -> {
            // Pega os dados inseridos pelo usuário
            String nome = edit_nome.getText().toString();
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();
            String acesso = "user"; // Define o nível de acesso. Pode ajustar conforme necessário
            String statusUsuario = "ativo"; // Define o status do usuário

            // Verifica se todos os campos estão preenchidos
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                // Exibe mensagem se algum campo estiver vazio
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                // Chama o método para cadastrar o usuário
                CadastrarUsuario(nome, email, senha, acesso, statusUsuario, v);
            }
        });
    }

    // Método para registrar o usuário
    private void CadastrarUsuario(String nome, String email, String senha, String acesso, String statusUsuario, View v) {
        // Chama o método de registro no banco de dados
        boolean sucesso = ConexaoBD.registerUser(nome, email, senha, acesso, statusUsuario, nome);

        if (sucesso) {
            // Se o cadastro for bem-sucedido, exibe mensagem de sucesso
            Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();

            // Navega para a tela de login após o cadastro
            Intent intent = new Intent(Cadastro.this, Login.class);
            startActivity(intent);
            finish();
        } else {
            // Se ocorrer um erro no cadastro, exibe mensagem de erro
            Snackbar snackbar = Snackbar.make(v, mensagens[2], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }
    }

    // Método para inicializar os componentes da tela
    private void IniciarComponentes() {
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_criarConta = findViewById(R.id.bt_criarConta);
    }
}
