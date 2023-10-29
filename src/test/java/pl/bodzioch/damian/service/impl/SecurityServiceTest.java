package pl.bodzioch.damian.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.bodzioch.damian.service.SecurityService;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles("local")
public class SecurityServiceTest {

    @Autowired
    SecurityService securityService;

    @Test
    public void decryptionShouldReturnTheEncryptedMessage() {
        String messageToEncrypt = getRandomMessage();
        String encryptedMessage = securityService.encryptMessage(messageToEncrypt);
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
