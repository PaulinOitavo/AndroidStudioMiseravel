package com.example.tcctela;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConexaoBD {

    private static final String DB_URL = "jdbc:jtds:sqlserver://172.19.0.103/bdtccOeo"; // Lembre se de trocar toda vez que trocar de máquina
    private static final String USER = "sa";
    private static final String PASS = "@ITB123456";

    private static Connection conn;

    // Conectar ao banco de dados SQL Server
    public static Connection conectar()  {
        Connection conn = null;
        /*if (ConexaoBD.conn != null )) {
            return ConexaoBD.conn;
        }*/
        try {
            // Permitir execução na thread principal (para desenvolvimento, não recomendado para produção)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Carregar o driver SQL Server
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Log.d("ConexaoBD", "Conexão bem-sucedida");
        } catch (Exception e) {
            Log.e("ConexaoBD", "Erro de conexão: " + e.getMessage());
            e.printStackTrace();
        }
        ConexaoBD.conn = conn;
        return conn;
    }

    // Método para registrar novo usuário
    public static boolean registerUser(String nome, String email, String senha, String acesso, String statusUsuario, String usuario) {
        Connection conn = conectar();
        if (conn == null) {
            return false;
        }

        try {
            // Gerar a data atual no formato desejado
            String dataCadastro = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            String query = "INSERT INTO Usuario (nome, email, senha, nivelAcesso, dataCadastro, statusUsuario) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.setString(4, acesso);
            stmt.setString(5, dataCadastro); // Passando a data de cadastro
            stmt.setString(6, statusUsuario);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            Log.e("ConexaoBD", "Erro ao registrar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para verificar login
    public static int loginUser(String email, String senha) {
        Connection conn = conectar();
        if (conn == null) {
            return -1; // Erro ao conectar ao banco
        }

        try {
            // Verificar se o e-mail está cadastrado
            if (!isEmailRegistered(email)) {
                return -2; // Retorna -2 se o e-mail não estiver cadastrado
            }

            // Verificar se a senha está correta
            String query = "SELECT * FROM Usuario WHERE email = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return 1; // Login bem-sucedido (substituindo a linha de 'access')
            } else {
                return -1; // Senha incorreta
            }
        } catch (Exception e) {
            Log.e("ConexaoBD", "Erro ao realizar login: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            closeConnection(conn);
        }
    }


    // Método para recuperar nome e e-mail do usuário com base no e-mail
    public static String[] getUserDataByEmail(String email) {
        Connection conn = conectar();
        if (conn == null) {
            return null;
        }

        try {
            String query = "SELECT nome, email, id FROM Usuario WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new String[]{rs.getString("nome"), rs.getString("email"), rs.getString("id")}; // Retorna o nome e o e-mail do usuário
            } else {
                return null; // Usuário não encontrado
            }
        } catch (Exception e) {
            Log.e("ConexaoBD", "Erro ao buscar dados do usuário: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para verificar se o e-mail já está cadastrado
    public static boolean isEmailRegistered(String email) {
        Connection conn = conectar();
        if (conn == null) {
            return false;
        }

        try {
            String query = "SELECT COUNT(*) FROM Usuario WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            System.out.println(rs);

            return rs.next() && rs.getInt(1) > 0; // Retorna true se o e-mail estiver cadastrado
        } catch (Exception e) {
            Log.e("conexaoBD", "Erro ao verificar e-mail: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para fechar a conexão
    private static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close(); // Fechar conexão
            }
        } catch (Exception e) {
            Log.e("ConexaoBD", "Erro ao fechar conexão: " + e.getMessage());
        }
    }


    public static boolean inserirHistoricoManifesto(String dataCadastro, int usuarioId, String providencia, String statusHist, int manifestoId) {
        Connection conn = conectar();
        if (conn == null) {
            return false;
        }

        try {
            String query = "INSERT INTO HistoricoManifesto (dataCadastro, usuario_id, providencia, status_hist, manifesto_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dataCadastro);  // Data de cadastro
            stmt.setInt(2, usuarioId);  // ID do usuário
            stmt.setString(3, providencia);  // Providência
            stmt.setString(4, statusHist);  // Status do histórico
            stmt.setInt(5, manifestoId);  // ID do manifesto

            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            Log.e("ConexaoBD", "Erro ao inserir histórico de manifesto: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    public static List<String[]> getHistoricoManifesto(int usuarioId) {
        Connection conn = conectar();
        if (conn == null) {
            return null;
        }

        List<String[]> historicoList = new ArrayList<>();

        try {
            String query = "SELECT dataCadastro, providencia, status_hist FROM HistoricoManifesto WHERE usuario_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, usuarioId);  // ID do usuário

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Adiciona cada registro como um array de Strings
                historicoList.add(new String[]{
                        rs.getString("dataCadastro"),
                        rs.getString("providencia"),
                        rs.getString("status_hist")
                });
            }

            return historicoList; // Retorna a lista de resultados
        } catch (Exception e) {
            Log.e("ConexaoBD", "Erro ao buscar histórico de manifesto: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            closeConnection(conn);
        }
    }

    public static int getUsuarioIdByManifesto(int manifestoId) {
        Connection conn = conectar();
        if (conn == null) {
            return -1; // Erro ao conectar
        }

        try {
            // Consulta para obter o ID do usuário associado ao manifesto
            String query = "SELECT usuario_id FROM HistoricoManifesto WHERE manifesto_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, manifestoId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("usuario_id");  // Retorna o ID do usuário associado ao manifesto
            } else {
                return -1;  // Nenhum usuário encontrado
            }
        } catch (Exception e) {
            Log.e("ConexaoBD", "Erro ao buscar usuário por manifesto: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            closeConnection(conn);
        }
    }


}