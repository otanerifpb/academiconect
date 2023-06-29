package br.edu.ifpb.pweb2.academiConect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.academiConect.model.Declaracao;
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
    // REQFUNC 09 - Relatório Declarações Vencidas
    // REQFUNC 10 - Relatório Declarações por Vencer
    // REQFUNC 11 - Relatório Aluno sem Declarações
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView pageReport(ModelAndView mav) {      
        mav.setViewName("relatorios/listRelator");
        return mav;
    } 

    // Rota para acessar a Página Relatórios ao usar o REDIRECT
    // REQFUNC 09 - Relatório Declarações Vencidas
    // REQFUNC 10 - Relatório Declarações por Vencer
    // REQFUNC 11 - Relatório Estudante sem Declarações
    @RequestMapping()
    public String pageReport() {
        return "relatorios/listRelator";
    }

    // Ativa o menu Relatórios na barra de navegação
    @ModelAttribute("menu")
    public String activeMenu(){
        return "relatorios";
    }

    // Rota para acessar a Lista de Declaração Vencida
    // REQFUNC 09 - Relatório Declarações Vencidas
    @RequestMapping("/listDecVenci")
    public String getlistDecVenci() {
        //String mostrarForm = "false";
        //mav.addObject("declaracao", declaracao);
        //mav.addObject("mostrarForm", mostrarForm);
        //mav.setViewName("relatorios/listRelator");
        return "relatorios/listDecVenci";
    }

    // Rota para acessar o Formulário de Declaração por Vencer
    // REQFUNC 10 - Relatório Declarações por Vencer
    @RequestMapping("formDecVence")
    public String getformDecVence() {
        //String mostrarForm = "false";
        //mav.addObject("declaracao", declaracao);
        //mav.addObject("mostrarForm", mostrarForm);
        //mav.setViewName("relatorios/listRelator");
        return "relatorios/formDecVence";
    }

    // Rota para acessar a Lista de Declaração por Vencer
    // REQFUNC 10 - Relatório Declarações por Vencer
    @RequestMapping("/listEstSDec")
    public String getlistEstSDec() {
        //String mostrarForm = "false";
        //mav.addObject("declaracao", declaracao);
        //mav.addObject("mostrarForm", mostrarForm);
        //mav.setViewName("relatorios/listRelator");
        return "relatorios/listEstSDec";
    }

    // Rota para acessar a Lista de Estudante Declaração
    // REQFUNC 11 - Relatório Estudante sem Declarações
    @RequestMapping("/listDecVence")
    public String getlistDecVence() {
        //String mostrarForm = "false";
        //mav.addObject("declaracao", declaracao);
        //mav.addObject("mostrarForm", mostrarForm);
        //mav.setViewName("relatorios/listRelator");
        return "relatorios/listDecVence";
    }

    //@RequestMapping("/listDecVence")
    // public ModelAndView getFormDecEstu(Declaracao declaracao, ModelAndView mav) {
    //     mav.addObject("declaracao", declaracao);
    //     mav.setViewName("relatorios/listDecVence");
    //     return mav;
    // }

    //List<Estudante> estudanteSemDeclaracao = estudanteRepository.buscaEstudanteQueNaoTemDeclaracao();
      
}
