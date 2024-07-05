package com.project.liar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // login.html 템플릿을 반환
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup"; // signup.html 템플릿을 반환
    }
    
    @PostMapping("path")
    public String logout() {
 
        return "redirect:/";
    }
    
}