package pl.bodzioch.damian.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/services-list")
    public String getServicesPage() {
        return "services/services";
    }

    @GetMapping("/scheduler-edit")
    public String getSchedulerEditPage() {
        return "scheduler/scheduler";
    }
}
