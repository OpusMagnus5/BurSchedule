package pl.bodzioch.damian.client.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.bodzioch.damian.client.BurClient;
import pl.bodzioch.damian.model.ServiceModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BurClientTest {

    @Autowired
    BurClient burClient;

    @Test
    public void isAuthorizeWorksCorrect() {
        List<ServiceModel> result = burClient.getServiceById(-1);
        assertTrue(result.isEmpty());
    }
}
