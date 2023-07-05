package br.edu.ifpb.pweb2.academiConect.controller;

import javax.servlet.http.HttpServletRequest;

//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/login") /*Rota para o acesso da class LoginController */
public class LoginController {

    // Rota para exibir o formulário do Login
    @RequestMapping("/showLoginForm")
    public String showLoginForm() {
        return "formLogin";
    }

    // Rota para processar o Login
    @RequestMapping("/processLogin")
    public String processLoginForm() {
        return "logged-in-1";
    }

    // Rota para validar o login Modelo 1
    @RequestMapping("/validateLogin")
    public String validate(HttpServletRequest request, ModelAndView mav) {
        String proxView = "formLogin";
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        if (login.equals("renato") && senha.equals("admin")) {
            mav.addObject("usuario", login);
            proxView = "logged-in-1";
        }
        return proxView;
    }

    // Rota para validar o login Modelo 2
    @RequestMapping("/validateLoginComParams")
    public String validade(@RequestParam("login") String login, @RequestParam("senha") String senha, ModelAndView mav) {
        String proxView = "formLogin";
        if (login.equals("renato") && senha.equals("admin")) {
            mav.addObject("usuario", login);
            proxView = "logged-in-2";
        }
        return proxView;
    }
}
