package com.example.cloth_area;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        // /webtest.html로 리다이렉트
        return "redirect:/webtest.html";
    }
}
