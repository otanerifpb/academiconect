package br.edu.ifpb.pweb2.academiConect.controller;

import java.time.chrono.Chronology;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.academiConect.model.Declaracao;
import br.edu.ifpb.pweb2.academiConect.model.Estudante;
import br.edu.ifpb.pweb2.academiConect.model.Periodo;
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
    public ModelAndView getlistDecVenci(ModelAndView mav) {
        Set<Declaracao> declaracaoVencida = declaracaoRepository.findByAllOverdueDeclaration();
        if (!declaracaoVencida.isEmpty()) {
            List<Declaracao> listDeclaracoes = new ArrayList<>();
            for (Declaracao declaracao : declaracaoVencida) {
                listDeclaracoes.add(declaracao);
            }
            mav.addObject("declaracoes", listDeclaracoes);
            mav.addObject("succesMensagem", "Declaração(s) encontrada(s) com sucesso!!");
            mav.setViewName("relatorios/listDecVenci");
        } else {
            mav.addObject("errorMensagem", "Não existe Declaração vencida cadastrada!!");
            mav.setViewName("redirect:/relatorios");
        }
        return mav;
    }

    // Rota para acessar o Formulário de Declaração por Vencer
    // REQFUNC 10 - Relatório Declarações por Vencer
    @RequestMapping("formDecVence")
    public String getformDecVence() {
        //String mostrarForm = "false";
        //mav.addObject("declaracao", declaracao);
        //mav.addObject("mostrarForm", mostrarForm);
        //mav.setViewName("relatorios/listRelator");
        return "relatorios/listDecVence";
    }

    // Rota para acessar a Lista de Estudante Declaração
    // REQFUNC 10 - Relatório Declarações por Vencer
    @RequestMapping("/listDecVence")
    public ModelAndView getlistDecVence( Integer dias, ModelAndView mav) {

        LocalDate novaData = LocalDate.now();
        LocalDate novaData2 = LocalDate.now().plusDays(dias);
        Date d1 = java.sql.Date.valueOf(novaData);
        Date d2 = java.sql.Date.valueOf(novaData2);
       
        Set<Declaracao> declaracoesAvencer = declaracaoRepository.declarationForExpire(d1, d2);
       
        //String mostrarForm = "false";
        mav.addObject("declaracoes", declaracoesAvencer);
        //mav.addObject("mostrarForm", mostrarForm);
        mav.setViewName("relatorios/listDecVence");
        return mav;
    }

    // Rota para acessar a Lista de Declaração por Vencer
    // REQFUNC 11 - Relatório Estudante sem Declarações
    @RequestMapping("/listEstSDec")
    public ModelAndView getlistEstSDec(ModelAndView mav) {
        Set<Estudante> estudanteSemDeclaracao = 
                estudanteRepository.findByStudantWintoutDeclaration();
        if (!estudanteSemDeclaracao.isEmpty()) {
            List<Estudante> listEstudantes = new ArrayList<>();
            for (Estudante estudante : estudanteSemDeclaracao) {
                listEstudantes.add(estudante);
            }
            mav.addObject("estudantes", listEstudantes);
            mav.addObject("succesMensagem", "Estudante(s) encontrado(s) com sucesso!!");
            mav.setViewName("relatorios/listEstSDec");
        } else {
            mav.addObject("errorMensagem", "Não existe Estudante sem declaração cadastrada!!");
            mav.setViewName("redirect:/relatorios");
        }
        return mav;
    }
    
    // Rota para acessar a lista com todas as declarações cadastradas
    @RequestMapping("/listDecEstu")
    public String listAll(Model model) {
        model.addAttribute("declaracoes", declaracaoRepository.findAll());
        return "relatorios/listDecEstu";
    }

    // Rota para acessar a Lista dos Estudantes menores de 24 Anos
    @RequestMapping("/listEstuMaior")
    public ModelAndView getListEstuMaior(ModelAndView mav) {
        List<Estudante> estudantes = new ArrayList<>();
        List<Estudante> listEstudantes = estudanteRepository.findAll();
        LocalDate  dataHoje = LocalDate.now();
   

        for(Estudante est : listEstudantes ){
            LocalDate localDateNas = (est.getDataNascimento()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            //LocalDate dataNascimento = LocalDate.parse();
            Period periodo = Period.between(localDateNas, dataHoje);
             int anos = periodo.getYears();
            if (anos>=24){
            estudantes.add(est);
                
            }
        }

        mav.addObject("estudantes", estudantes);
        mav.setViewName("relatorios/listEstuMaior");
        return mav;
    }
}
