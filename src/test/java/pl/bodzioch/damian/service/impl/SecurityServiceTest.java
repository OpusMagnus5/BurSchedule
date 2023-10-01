package pl.bodzioch.damian.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.bodzioch.damian.service.SecurityService;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class SecurityServiceTest {

    @Autowired
    SecurityService securityService;

    @Test
    public void decryptionShouldReturnTheEncryptedMessage() {
        String messageToEncrypt = getRandomMessage();
        String encryptedMessage = securityService.encryptMessage(messageToEncrypt); //TODO fix Invalid AES key length: 50 bytes
        String decryptedMessage = securityService.decryptMessage(encryptedMessage);

        assertEquals(messageToEncrypt, decryptedMessage);
        assertNotEquals(messageToEncrypt, encryptedMessage);
    }

    private String getRandomMessage() {
        byte[] byteArray = new byte[18];
        new Random().nextBytes(byteArray);
        return new String(byteArray, StandardCharsets.UTF_8);
    }
}
