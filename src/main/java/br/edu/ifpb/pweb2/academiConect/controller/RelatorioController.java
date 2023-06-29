package br.edu.ifpb.pweb2.academiConect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.academiConect.repository.DeclaracaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.EstudanteRepository;

@Controller
@RequestMapping("/relatorios") /*Rota para o acesso da class */
@PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessa a classe */
public class RelatorioController {

    @Autowired
    private EstudanteRepository estudanteRepository;

    @Autowired
    private DeclaracaoRepository declaracaoRepository; 

    // Rota para acessar a Página de Relatóros pelo menu
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView pageReport(ModelAndView mav) {      
        mav.setViewName("relatorios/listRepo");
        return mav;
    } 

    // Rota para acessar a Página Relatórios ao usar o REDIRECT
    @RequestMapping()
    public String pageReport() {
        return "estudantes/listEstu";
    }

    // Ativa o menu Relatórios na barra de navegação
    @ModelAttribute("menu")
    public String activeMenu(){
        return "relatorios";
    }

    //List<Estudante> estudanteSemDeclaracao = estudanteRepository.buscaEstudanteQueNaoTemDeclaracao();
      
}
