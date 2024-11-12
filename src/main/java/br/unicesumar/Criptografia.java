package br.unicesumar;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class Criptografia {

    public static SecretKey gerarChaveSecreta() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    public static IvParameterSpec gerarIV() {
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static byte[] criptografar(String texto, SecretKey chaveSecreta, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, chaveSecreta, iv);
        return cipher.doFinal(texto.getBytes());
    }

    public static String descriptografar(byte[] textoCriptografado, SecretKey chaveSecreta, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, chaveSecreta, iv);
        byte[] resultado = cipher.doFinal(textoCriptografado);
        return new String(resultado);
    }

    public static String toBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] fromBase64(String data) {
        return Base64.getDecoder().decode(data);
    }
}
