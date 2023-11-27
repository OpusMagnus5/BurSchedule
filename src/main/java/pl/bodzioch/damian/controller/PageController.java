package pl.bodzioch.damian.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/services-list")
    public String getServicesPage() {
        return "services/services";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/scheduler-edit")
    public String getSchedulerEditPage() {
        return "scheduler/scheduler";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/scheduler-upload")
    public String getUploadSchedulerPage() {
        return "schedulerupload/scheduler-upload";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("")
    public String getMainPage() {
        return "redirect:/services-list";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/scheduler-create")
    public String getScheduleCreatePage() {
        return "schedulercreate/scheduler-create";
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "admin/admin";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/scheduler-list")
    public String schedulerList() {
        return "scheduler-list/scheduler-list";
    }
}
