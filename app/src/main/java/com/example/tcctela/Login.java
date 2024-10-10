package com.example.tcctela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.snackbar.Snackbar;

public class Login extends AppCompatActivity {

    private EditText edit_email, edit_senha;
    private Button bt_entrar;
    private Button bt_novaConta;
    private SharedPreferences sharedPreferences;
    String[] mensagens = {"Preencha todos os campos", "E-mail ou senha inválidos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Carrega o layout correto inicialmente

        // Verifica se o usuário já está logado
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            loginUser();
            return; // Impede que a tela de login seja carregada novamente
        }

        IniciarComponentes(); // Inicializa as views corretamente no layout activity_login

        bt_novaConta.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Cadastro.class);
            startActivity(intent);
        });

        bt_entrar.setOnClickListener(v -> {
            String email = edit_email.getText().toString();
            String senha = edit_senha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()) {
                Snackbar.make(v, "Preencha todos os campos", Snackbar.LENGTH_SHORT).show();
                return;
            }

            int loginResult = ConexaoBD.loginUser(email, senha);

            if (loginResult == -2) {
                // E-mail não cadastrado
                Snackbar.make(v, "E-mail não está cadastrado", Snackbar.LENGTH_SHORT).show();
            } else if (loginResult == -1) {
                // Senha incorreta ou erro ao logar
                Snackbar.make(v, "E-mail ou senha incorretos", Snackbar.LENGTH_SHORT).show();
            } else {
                // Login bem-sucedido
                Snackbar.make(v, "Login bem-sucedido", Snackbar.LENGTH_SHORT).show();

                // Recupera o nome do usuário a partir do e-mail
                String[] userData = ConexaoBD.getUserDataByEmail(email);
                if (userData != null) {
                    String nome = userData[0]; // Nome do usuário
                    int id = Integer.parseInt(userData[2]);

                    // Salvar o estado de login e detalhes do usuário
                    SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("email", email);
                    int usuarioId = sharedPreferences.getInt("usuarioId", -1);
                    editor.putInt("usuarioId", id);
                    editor.putString("nome", nome); // Salva o nome real do usuário
                    editor.apply();

                    // Navegar para a próxima tela
                    loginUser();
                } else {
                    Snackbar.make(v, "Erro ao buscar dados do usuário", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser() {
        Intent intent = new Intent(Login.this, Historico.class);

        startActivity(intent);
        finish();
    }
    private void IniciarComponentes() {
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        bt_novaConta = findViewById(R.id.bt_novaConta);
    }
}