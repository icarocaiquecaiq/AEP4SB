package br.unicesumar;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private byte[] senhaCriptografada;
    private IvParameterSpec iv;
    private SecretKey chaveSecreta;

    public Usuario(String nome, String email, String senha) throws Exception {
        this.nome = nome;
        this.email = email;
        this.chaveSecreta = Criptografia.gerarChaveSecreta();
        this.iv = Criptografia.gerarIV();
        this.senhaCriptografada = Criptografia.criptografar(senha, chaveSecreta, iv);
    }

    public Usuario(String nome, String email, byte[] senhaCriptografada, IvParameterSpec iv, SecretKey chaveSecreta) {
        this.nome = nome;
        this.email = email;
        this.senhaCriptografada = senhaCriptografada;
        this.iv = iv;
        this.chaveSecreta = chaveSecreta;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getSenhaCriptografada() {
        return senhaCriptografada;
    }

    public IvParameterSpec getIv() {
        return iv;
    }

    public SecretKey getChaveSecreta() {
        return chaveSecreta;
    }

    public String getIvBase64() {
        return Criptografia.toBase64(iv.getIV());
    }

    public String getChaveSecretaBase64() {
        return Criptografia.toBase64(chaveSecreta.getEncoded());
    }

    public boolean verificarSenha(String senha) throws Exception {
        String senhaDescriptografada = Criptografia.descriptografar(this.senhaCriptografada, this.chaveSecreta, this.iv);
        return senhaDescriptografada.equals(senha);
    }
}
