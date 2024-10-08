package com.example.tcctela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Historico extends AppCompatActivity {

    private TextView nomeUsuario, emailUsuario;
    private SharedPreferences sharedPreferences;
    private Button bt_sair;
    private TextView dataCadastroView;
    private TextView providenciaView;
    private TextView statusHistView;

    private TextView usuarioIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        // Inicializa os componentes da interface
        IniciarComponentes();

        // Recupera os dados armazenados no SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("nome", ""); // Valor padrão
        String userEmail = sharedPreferences.getString("email", ""); // Valor padrão
        Integer usuarioId = sharedPreferences.getInt("usuarioId", -1); // Recupera o ID do usuário logado

        // Verificação do usuário
        if (usuarioId == -1) {
            Toast.makeText(this, "Erro ao recuperar o usuário. Faça login novamente." + userName, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Historico.this, Login.class);
            startActivity(intent);
            finish();
            return;
        }

        // Define os valores dos campos de nome e email
        nomeUsuario.setText(userName);
        emailUsuario.setText(userEmail);
        usuarioIdTextView.setText(usuarioId.toString());

        // Ação do botão de logout
        bt_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpar os dados do SharedPreferences para efetuar logout
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Limpa todos os dados
                editor.apply();

                // Voltar para a tela de login
                Intent intent = new Intent(Historico.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Inicializar as Views
        dataCadastroView = findViewById(R.id.criacao2); // TextView onde exibe a data de criação
        providenciaView = findViewById(R.id.assunto2); // TextView onde exibe a providência
        statusHistView = findViewById(R.id.status2); // TextView onde exibe o status

        // Carregar histórico em uma thread separada
        carregarHistorico(usuarioId);
    }

    private void carregarHistorico(final int usuarioId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String[]> historicoData = ConexaoBD.getHistoricoManifesto(usuarioId);

                // Atualizar a interface na thread principal
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (historicoData != null && !historicoData.isEmpty()) {
                            String[] primeiroHistorico = historicoData.get(0);

                            dataCadastroView.setText("Data de criação: " + primeiroHistorico[0]); // dataCadastro
                            providenciaView.setText(primeiroHistorico[1]); // providencia
                            statusHistView.setText(primeiroHistorico[2]); // status_hist
                        } else {
                            // Tratar caso não haja histórico
                            dataCadastroView.setText("Data não encontrada");
                            providenciaView.setText("Providência não disponível");
                            statusHistView.setText("Status não disponível");
                        }
                    }
                });
            }
        }).start();
    }

    // Método para inicializar os componentes da interface
    private void IniciarComponentes() {
        nomeUsuario = findViewById(R.id.nome_usuario);
        emailUsuario = findViewById(R.id.email_usuario);
        bt_sair = findViewById(R.id.bt_sair);
        usuarioIdTextView = (TextView) findViewById(R.id.idusu2);
    }
}
