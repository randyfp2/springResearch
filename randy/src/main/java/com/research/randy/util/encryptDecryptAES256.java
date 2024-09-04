package com.research.randy.util;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class encryptDecryptAES256 {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12; // Initialization Vector size (in bytes)
    private static final int TAG_LENGTH = 128; // Authentication tag length (in bits)

    // Generate a new AES256 key
    private SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(KEY_SIZE);
        return keyGen.generateKey();
    }

    // Encrypt the input string using the AES256 key
    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey key = generateKey();
        byte[] iv = new byte[IV_SIZE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        byte[] encryptedData = cipher.doFinal(data.getBytes());
        byte[] encryptedDataWithIv = new byte[IV_SIZE + encryptedData.length];
        System.arraycopy(iv, 0, encryptedDataWithIv, 0, IV_SIZE);
        System.arraycopy(encryptedData, 0, encryptedDataWithIv, IV_SIZE, encryptedData.length);

        // Combine IV and key for storage or transmission
        byte[] keyAndEncryptedData = new byte[key.getEncoded().length + encryptedDataWithIv.length];
        System.arraycopy(key.getEncoded(), 0, keyAndEncryptedData, 0, key.getEncoded().length);
        System.arraycopy(encryptedDataWithIv, 0, keyAndEncryptedData, key.getEncoded().length, encryptedDataWithIv.length);

        return Base64.getEncoder().encodeToString(keyAndEncryptedData);
    }

    // Decrypt the encrypted string
    public String decrypt(String encryptedData) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);

        byte[] keyBytes = new byte[KEY_SIZE / 8];
        byte[] iv = new byte[IV_SIZE];
        byte[] encryptedBytes = new byte[decodedData.length - keyBytes.length - IV_SIZE];

        System.arraycopy(decodedData, 0, keyBytes, 0, keyBytes.length);
        System.arraycopy(decodedData, keyBytes.length, iv, 0, IV_SIZE);
        System.arraycopy(decodedData, keyBytes.length + IV_SIZE, encryptedBytes, 0, encryptedBytes.length);

        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

        byte[] decryptedData = cipher.doFinal(encryptedBytes);
        return new String(decryptedData);
    }
}