package br.edu.ifpb.pweb2.academiConect.controller;

//import java.util.Optional;

import javax.servlet.http.HttpSession;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMethod;

//import br.edu.ifpb.pweb2.academiConect.model.Estudante;
//import br.edu.ifpb.pweb2.academiConect.repository.EstudanteRepository;
//import br.edu.ifpb.pweb2.academiConect.util.PasswordUtil;

// Rota para o acesso da class AuthController
// REQFUNC 13 - Autenticação e Autorização
@Controller
@RequestMapping("/auth")
public class AuthController {

    // @Autowired
    // private EstudanteRepository estudanteRepository;

    // Rota para o acessar com uso do GET
    // Cria um novo objeto conhecido como "cookie", para identificar o Usuário
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getForm(ModelAndView mav) {  
        mav.setViewName("/auth/formLogin");
       // mav.addObject("estudante", new Estudante());
        return mav;
    }
    
    // Rota para o acessar com uso do POST, para validar Usuário e Senha
    // @RequestMapping(value="/valide", method = RequestMethod.POST)
    // public ModelAndView valide(Estudante estudante, HttpSession session, ModelAndView mav,
    //         RedirectAttributes redAttrs) {
    //     if ((estudante = this.isValido(estudante)) != null) {
    //         session.setAttribute("usuario", estudante);
    //         mav.addObject("succesMensagem", estudante.getNome() + " Bem Vindo ao ACADEMICONECT");
    //         mav.setViewName("redirect:/home");
    //     } else {
    //         redAttrs.addFlashAttribute("errorMensagem", "Login e/ou Senha inválidos!");
    //         mav.setViewName("redirect:/auth");
    //     }
    //     return mav;
    // }

    // Rota para o sair da Sessão
    @RequestMapping("/logout")
    public ModelAndView logout(ModelAndView mav, HttpSession session) {
        session.invalidate();
        mav.setViewName("redirect:/auth");
        return mav;
    }

    // Método para validação do Usuário e Senha
    // private Estudante isValido(Estudante estudante) {
    //     Optional<Estudante> opestudanteSGBD = estudanteRepository.findByEmail(estudante.getEmail());
    //     //Estudante estudanteSGBD = estudanteRepository.findByEmail(estudante.getEmail());
    //     Estudante estudanteBD = opestudanteSGBD.get();
    //     boolean valido = false;
    //     if (opestudanteSGBD.isPresent()) {
    //         if (PasswordUtil.checkPass(estudante.getSenha(), estudanteBD.getSenha())) {
    //             valido = true;
    //         }
    //     }
    //     return valido ? estudanteBD : null;
    // }
}
