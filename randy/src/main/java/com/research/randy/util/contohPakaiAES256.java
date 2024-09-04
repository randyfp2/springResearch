package com.research.randy.util;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class contohPakaiAES256 {
    @Autowired
    private encryptDecryptAES256 aesEncryptionService;

    public void performEncryptionDecryption() throws Exception {
        // Data yang akan dienkripsi
        String originalData = "manage";

        // Enkripsi data
        String encryptedData = aesEncryptionService.encrypt(originalData);
        System.out.println("Encrypted Data: " + encryptedData);

        // Dekripsi data
        String decryptedData = aesEncryptionService.decrypt(encryptedData);
        System.out.println("Decrypted Data: " + decryptedData);
    }
}


