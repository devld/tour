package me.devld.tour.controller.web;

import me.devld.tour.util.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping("/greeting")
    @ResponseBody
    public String greeting() {
        return "Greeting, " + SecurityUtil.user().getUsername();
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String adminOnly() {
        return "Hello, admin";
    }

}
