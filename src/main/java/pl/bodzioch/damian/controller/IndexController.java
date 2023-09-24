package pl.bodzioch.damian.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class IndexController {


    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
