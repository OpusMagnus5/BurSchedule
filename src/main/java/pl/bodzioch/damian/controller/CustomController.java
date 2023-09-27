package pl.bodzioch.damian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bodzioch.damian.service.ServiceForBurClient;

@RestController
@RequestMapping("/app")
public class CustomController {

    private final ServiceForBurClient serviceForBurClient;

    @Autowired
    public CustomController(ServiceForBurClient serviceForBurClient) {
        this.serviceForBurClient = serviceForBurClient;
    }

    @GetMapping("/service")
    public String getAllServices() {
        return "index";
    }
}
