package me.devld.tour.controller.api;

import me.devld.tour.util.SecurityUtil;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ExampleController {

    @GetMapping("/me")
    public Object me() {
        return SecurityUtil.user();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String adminOnly() {
        return "Hello, admin";
    }

}
