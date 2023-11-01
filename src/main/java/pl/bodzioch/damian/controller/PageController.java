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

    @GetMapping("/scheduler-upload")
    public String getUploadSchedulerPage() {
        return "schedulerupload/scheduler-upload";
    }

    @GetMapping("")
    public String getMainPage() {
        return "redirect:/services-list";
    }

    @GetMapping("/scheduler-create")
    public String getScheduleCreatePage() {
        return "schedulercreate/scheduler-create";
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/admin";
    }
}
