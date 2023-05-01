package br.edu.ifpb.pweb2.academiConect.controller;

import java.io.Serializable;
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

import br.edu.ifpb.pweb2.academiConect.model.Declaracao;
import br.edu.ifpb.pweb2.academiConect.model.Estudante;
import br.edu.ifpb.pweb2.academiConect.repository.DeclaracaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.EstudanteRepository;
import br.edu.ifpb.pweb2.academiConect.repository.PeriodoRepository;

// Rota para acessar a class
@Controller
@RequestMapping("/declaracoes")
public class DeclaracaoController implements Serializable {
    @Autowired
    DeclaracaoRepository declaracaoRepository; 

    @Autowired
    EstudanteRepository estudanteRepository;
    
    @Autowired
    PeriodoRepository periodoRepository; 

    // Rota para acessar a lista pelo menu com o GET
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listAll(ModelAndView model) {
        model.addObject("declaracoes", declaracaoRepository.findAll());
        model.setViewName("declaracoes/listDecl");
        return model;
    }

    // Rota para acessar a lista com o uso do REDIRECT
    // REQFUNC 4 - CRUD
    @RequestMapping()
    public String listAll(Model model) {
        model.addAttribute("declaracoes", declaracaoRepository.findAll());
        return "declaracoes/listDecl";
    }

    // Rota para acessar o formulário
    // REQFUNC 6 - Instituição Atual
    // REQFUNC 8 - Instituição Atual
    // REQNFUNC - Mostrar Erro nos Formulários
    @RequestMapping("/formDecl")
    public ModelAndView getFormDecl(Declaracao declaracao, ModelAndView model) {
        model.addObject("declaracao", declaracao);
        model.setViewName("declaracoes/formDecl");
        return model;
    }

    // Rota para acessar o formunlário de atualização ou a lista se não for atualizar 
    // REQFUNC 6 - Instituição Atual
    // REQFUNC 8 - Instituição Atual
    // REQNFUNC - Mostrar Erro nos Formulários
    @RequestMapping("/{id}")
    public ModelAndView getInstituicaoById(@PathVariable(value = "id") Integer id, ModelAndView model) {
        Optional<Declaracao> opDeclaracao = declaracaoRepository.findById(id);
        if (opDeclaracao.isPresent()) {
            Declaracao declaracao = opDeclaracao.get();
            model.addObject("declaracao", declaracao);
            model.setViewName("declaracoes/formUpDecl");
        } else {
            model.addObject("errorMensagem", "Declaração não encontrado.");
            model.setViewName("declaracoes/listDecl");
        }
        return model;
    }

    // Rota para salvar novo objeto na lista com o uso do POST
    // REQFUNC 4 - CRUD
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView save(Declaracao declaracao, ModelAndView model, RedirectAttributes redAttrs) {
        Optional<Estudante> opEstudante = estudanteRepository.findByMatricula(declaracao.getEstudante().getMatricula());
        if (opEstudante.isPresent()) {
            Estudante estudante = opEstudante.get();
            declaracao.setEstudante(estudante);
            declaracaoRepository.save(declaracao);
            model.addObject("declaracoes", declaracaoRepository.findAll());
            //redAttrs.addFlashAttribute("succesMensagem", "Declaração cadastrado com sucesso!");
            //model.setViewName("redirect:declaracoes");
            model.addObject("succesMensagem", "Declaração cadastrado com sucesso!");
            model.setViewName("declaracoes/listDecl");
        } else {
            model.addObject("declaracoes", declaracaoRepository.findAll());
            redAttrs.addFlashAttribute("errorMensagem", "Estudante não encontrado!!");
            model.setViewName("redirect:/declaracoes");
        }  
        return model;
    }

    // Rota para atualizar um objeto na lista com o uso do POST
    // REQFUNC 4 - CRUD
    // REQFUNC 6 - Instituição Atual
    // REQFUNC 8 - Instituição Atual
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public ModelAndView updade(Declaracao declaracao, ModelAndView model, RedirectAttributes redAttrs) {
        Optional<Estudante> opEstudante = estudanteRepository.findByMatricula(declaracao.getEstudante().getMatricula());
        if (opEstudante.isPresent()) {
            Estudante estudante = opEstudante.get();
            declaracao.setEstudante(estudante);
            declaracaoRepository.save(declaracao);
            model.addObject("declaracoes", declaracaoRepository.findAll());
            //redAttrs.addFlashAttribute("succesMensagem", "Instituição "+declaracao.getId()+", atualizada com sucesso!_redAttrs");
            //model.setViewName("redirect:/declaracoes");
            model.addObject("succesMensagem", "Declaração cadastrado com sucesso!_model");
            model.setViewName("declaracoes/listDecl");
    } else {
        model.addObject("declaracoes", declaracaoRepository.findAll());
        redAttrs.addFlashAttribute("errorMensagem", "Estudante não encontrado!!");
        model.setViewName("redirect:/declaracoes");
    }  
        return model;
    }

    // Rota para deletar um objeto da lista
    // REQFUNC 4 - CRUD
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @RequestMapping("{id}/delete")
    public ModelAndView deleteById(@PathVariable(value = "id") Integer id, ModelAndView model, RedirectAttributes redAtt) {
        Optional<Declaracao> opDeclaracao = declaracaoRepository.findById(id);
        if (opDeclaracao.isPresent()) {
            Declaracao declaracao = opDeclaracao.get();
            declaracao.setEstudante(null);;
            declaracao.setPeriodo(null);
            declaracaoRepository.deleteById(id);
            redAtt.addFlashAttribute("succesMensagem", "Declarção deletada com sucesso!!");
        } else {
            redAtt.addFlashAttribute("errorMensagem", "Declaração não encontrada.");
        }
        model.setViewName("redirect:/declaracoes");
        return model;
    }

    // Rota para relacionamento class Declaração com class Estudante para Form
    @ModelAttribute("estudanteItems")
    public List<Estudante> getEstudantes() {
        return estudanteRepository.findAll();
    }

    // Ativa o menu Declaração na barra de navegação
    @ModelAttribute("menu")
    public String activeMenu(){
        return "declaracoes";
    }
    
     
}
