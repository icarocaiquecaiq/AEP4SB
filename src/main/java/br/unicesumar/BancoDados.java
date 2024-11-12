package br.unicesumar;

import java.sql.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class BancoDados {

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/aepUni";
        String user = "root";
        String password = "123Mudar";
        return DriverManager.getConnection(url, user, password);
    }

    public void salvarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha_criptografada, iv, chave_secreta) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setBytes(3, usuario.getSenhaCriptografada());
            stmt.setString(4, usuario.getIvBase64());
            stmt.setString(5, usuario.getChaveSecretaBase64());
            stmt.executeUpdate();
        }
    }

    public Usuario recuperarUsuario(String email) throws Exception {
        String sql = "SELECT nome, email, senha_criptografada, iv, chave_secreta FROM usuario WHERE email = ?";
        Usuario usuario = null;

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                String emailRecuperado = rs.getString("email");
                byte[] senhaCriptografada = rs.getBytes("senha_criptografada");
                byte[] ivBytes = Criptografia.fromBase64(rs.getString("iv"));
                byte[] chaveBytes = Criptografia.fromBase64(rs.getString("chave_secreta"));

                IvParameterSpec iv = new IvParameterSpec(ivBytes);
                SecretKey chaveSecreta = new javax.crypto.spec.SecretKeySpec(chaveBytes, "AES");

                usuario = new Usuario(nome, emailRecuperado, senhaCriptografada, iv, chaveSecreta);
            }
        }

        return usuario;
    }

    public void adicionarArquivo(String nome, String conteudo, Usuario usuarioLogado) {
        String sqlArquivo = "INSERT INTO Arquivo (nome, conteudo, usuario_id) VALUES (?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlArquivo)) {
            stmt.setString(1, nome);
            stmt.setString(2, conteudo);
            stmt.setInt(3, usuarioLogado.getId()+1);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exibirArquivos(Usuario usuarioLogado) throws SQLException {
        String sql = "SELECT nome, conteudo FROM Arquivo WHERE usuario_id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioLogado.getId()+1);

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Não há arquivos armazenados.");
            } else {
                do {
                    String nomeArquivo = rs.getString("nome");
                    String conteudoArquivo = rs.getString("conteudo");
                    System.out.println("Arquivo: " + nomeArquivo);
                    System.out.println("Conteúdo: " + conteudoArquivo);
                    System.out.println("-----");
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
