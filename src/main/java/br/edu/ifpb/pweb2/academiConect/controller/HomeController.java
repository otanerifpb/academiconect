package br.edu.ifpb.pweb2.academiConect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/home")
    public String showHomePage() {
        return "index";
    }  
    
    // Ativa o menu Home na barra de navegação
    @ModelAttribute("menu")
    public String activeMenu(){
        return "home";
    }
}


