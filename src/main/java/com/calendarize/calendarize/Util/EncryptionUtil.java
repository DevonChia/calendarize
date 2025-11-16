package com.calendarize.calendarize.Util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.calendarize.calendarize.Exceptions.EncryptionException;

public class EncryptionUtil {
    public static byte[] encrypt(String text, String secret){
        try {
            SecretKey key = getSecretKey(secret);
            IvParameterSpec iv = generateIv();
            byte[] ivBytes = iv.getIV();

            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

            ByteBuffer buffer = ByteBuffer.allocate(ivBytes.length + cipherText.length);
            buffer.put(ivBytes);
            buffer.put(cipherText);
            return buffer.array();
        } catch (Exception e) {
            throw new EncryptionException("Failed to encrypt.", e);
        }
    }

    public static String decrypt(byte[] encrypted, String secret) {
        try {
            SecretKey key = getSecretKey(secret);

            byte[] ivBytes = Arrays.copyOfRange(encrypted, 0, 16);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            byte[] cipherText = Arrays.copyOfRange(encrypted, 16, encrypted.length);
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] plainBytes = cipher.doFinal(cipherText);
            return new String(plainBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptionException("Failed to decrypt.", e);
        }
    }

    private static SecretKey getSecretKey(String secret) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(bytes, 0, bytes.length, "AES");
    }

    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        SecretKey secret = keygen.generateKey();
        // System.out.println("secret  :: " + Base64.getEncoder().encodeToString(secret.getEncoded()));
        return secret;
    }

    private static IvParameterSpec generateIv() {
        byte[] bytes = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);
        IvParameterSpec iv = new IvParameterSpec(bytes);
        return iv;
    }
}