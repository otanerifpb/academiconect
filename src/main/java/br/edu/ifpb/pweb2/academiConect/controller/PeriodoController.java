package br.edu.ifpb.pweb2.academiConect.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.academiConect.model.Instituicao;
import br.edu.ifpb.pweb2.academiConect.model.Periodo;
import br.edu.ifpb.pweb2.academiConect.repository.DeclaracaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.InstituicaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.PeriodoRepository;

// Rota para acessar a class
@Controller
@RequestMapping("/periodos")
public class PeriodoController {
    @Autowired
    PeriodoRepository periodoRepository;
    
    @Autowired
    InstituicaoRepository instituicaoRepository;

    @Autowired
    DeclaracaoRepository declaracaoRepository;

    // Rota para acessar a lista pelo menu com o GET
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listAll(ModelAndView model) {
        model.addObject("periodos", periodoRepository.findAll());
        model.setViewName("periodos/listPeri");
        return model;
    }

    // Rota para acessar a lista ao usar a Rota da class
    @RequestMapping()
    public String listAll(Model model) {
        model.addAttribute("periodos", periodoRepository.findAll());
        return "periodos/listPeri";
    }

    // Rota para acessar o formunário
    @RequestMapping("/formPeri")
    public ModelAndView getFormPeri(ModelAndView model) {
        model.addObject("periodo", new Periodo());
        model.setViewName("periodos/formPeri");
        return model;
    }

    // Rota para acessar o formunlário de atualização ou a lista se não for atualizar 
    @RequestMapping("/{id}")
    public ModelAndView getPeriodoById(@PathVariable(value = "id") Integer id, ModelAndView model) {
        Optional<Periodo> opPeriodo = periodoRepository.findById(id);
        //Periodo periodo = opPeriodo.get();
        if (opPeriodo.isPresent()) {
            model.addObject("periodo", opPeriodo.get());
            model.setViewName("periodos/formUpPeri");
        } else {
            model.addObject("errorMensagem", "Periodo informado não encontrado.");
            model.setViewName("periodos/listPeri");
        }
        return model;
    }

    // Rota para salvar novo objeto na lista
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView save(Periodo periodo, ModelAndView model, RedirectAttributes redAttrs) {
        Optional<Periodo> opPeriodo = periodoRepository.findByPeriodo(periodo.getPeriodo());
        //Optional<Periodo> opPeriodo = periodoRepository.findByAno(periodo.getAno());
        if (opPeriodo.isPresent()) {
            redAttrs.addFlashAttribute("errorMensagem", "Periodo "+periodo.getPeriodo()+" já cadastrado no sistema!!");
            model.setViewName("redirect:periodos");     
        } else {
            periodoRepository.save(periodo);
            model.addObject("periodos", periodoRepository.findAll());
            redAttrs.addFlashAttribute("succesMensagem", "Período "+periodo.getPeriodo()+" cadastrado com sucesso!!");
            model.setViewName("redirect:periodos");
        }  
        return model;
    }

    // Rota para atualizar um objeto na lista
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public ModelAndView updade(Periodo periodo, ModelAndView model) {
        periodoRepository.save(periodo);
        model.addObject("periodos", periodoRepository.findAll());
        model.addObject("succesMensagem", "Período "+periodo.getPeriodo()+", atualizado com sucesso!");
        model.setViewName("periodos/listPeri");
        return model;
    }

    // Rota para deletar um objeto da lista
    @RequestMapping("{id}/delete")
    public ModelAndView deleteById(@PathVariable(value = "id") Integer id, ModelAndView model, RedirectAttributes redAtt) {
        Optional<Periodo> opPeriodo = periodoRepository.findById(id);
        if (opPeriodo.isPresent()) {
            Periodo periodo = opPeriodo.get();
            List<Instituicao> listInstituicao = periodo.getInstituicao();
            if(listInstituicao.isEmpty()){
                periodoRepository.deleteById(id);
            }
            //declaracao.setDeclaracao(null);
            redAtt.addFlashAttribute("succesMensagem", "Período "+periodo.getPeriodo()+" deletado com sucesso!!");
        } else {
            redAtt.addFlashAttribute("errorMensagem", "Período não pode ser deletada, contem instituição cadastrada.");
        }
        model.setViewName("redirect:/periodos");
        return model;
    }

     // Ativa o menu estudantes na barra de navegação
     @ModelAttribute("menu")
     public String activeMenu(){
         return "periodos";
     }
    
}
