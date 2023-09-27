package pl.bodzioch.damian.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.exception.CipherException;
import pl.bodzioch.damian.service.SecurityService;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class SecurityServiceImpl implements SecurityService {

    private final String key;

    @Autowired
    public SecurityServiceImpl(@Value("${cipher.key}") String key) {
        this.key = key;
    }

    @Override
    public String encryptMessage(String message) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return new String(cipher.doFinal(message.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException ex) {
            log.error("Cipher exception during encrypt message: {}", message, ex);
            throw new CipherException("Cipher exception during encrypt message", ex);
        }
    }

    @Override
    public String decryptMessage(String message) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(message.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException ex) {
            log.error("Cipher exception during decrypt message: {}", message, ex);
            throw new CipherException("Cipher exception during decrypt message", ex);
        }
    }
}
