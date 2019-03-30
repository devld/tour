package me.devld.tour.controller.web;

import me.devld.tour.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping(value = "/me", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> me() {
        return ResponseEntity.ok(SecurityUtil.userOrNull());
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

}
