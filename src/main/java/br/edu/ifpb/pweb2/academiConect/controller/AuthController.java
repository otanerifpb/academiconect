package br.edu.ifpb.pweb2.academiConect.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMethod;

import br.edu.ifpb.pweb2.academiConect.model.Estudante;
import br.edu.ifpb.pweb2.academiConect.repository.EstudanteRepository;
import br.edu.ifpb.pweb2.academiConect.util.PasswordUtil;
import br.edu.ifpb.pweb2.academiConect.util.PasswordUtil;

@Controller
@RequestMapping("/auth")
// Rota para o acesso da class AuthController
// REQFUNC 13 - Autenticação e Autorização
public class AuthController {

    @Autowired
    private EstudanteRepository estudanteRepository;

    // Rota para o acessar com uso do GET
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getForm(ModelAndView mav) {
        mav.setViewName("auth/formLogin");
        mav.addObject("usuario", new Estudante());
        return mav;
    }
    
    // Rota para o acessar com uso do POST
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView valide(Estudante estudante, HttpSession session, ModelAndView mav,
            RedirectAttributes redAttrs) {
        if ((estudante = this.isValido(estudante)) != null) {
            session.setAttribute("usuario", estudante);
            mav.addObject("succesMensagem", estudante.getNome() + " Bem Vindo ao ACADEMICONECT");
            mav.setViewName("redirect:/home");
        } else {
            redAttrs.addFlashAttribute("errorMensagem", "Login e/ou Senha inválidos!");
            mav.setViewName("redirect:/auth");
        }
        return mav;
    }

    // Rota para o sair da Sessão
    @RequestMapping("/logout")
    public ModelAndView logout(ModelAndView mav, HttpSession session) {
        session.invalidate();
        mav.setViewName("redirect:/auth");
        return mav;
    }

    private Estudante isValido(Estudante estudante) {
        Optional<Estudante> opestudanteSGBD = estudanteRepository.findByEmail(estudante.getEmail());
        //Estudante estudanteSGBD = estudanteRepository.findByEmail(estudante.getEmail());
        Estudante estudanteBD = opestudanteSGBD.get();
        boolean valido = false;
        if (opestudanteSGBD.isPresent()) {
            //Estudante estudanteBD = opestudanteSGBD.get();
            
            if (PasswordUtil.checkPass(estudante.getSenha(), estudanteBD.getSenha())) {
                valido = true;
            }
        }
        // if (estudanteSGBD != null) {
        //     //Estudante estudante = estudanteSGBD.get();
        //     if (PasswordUtil.checkPass(estudante.getSenha(), estudanteSGBD.getSenha())) {
        //         valido = true;
        //     }
        // }
        return valido ? estudanteBD : null;
    }
}
