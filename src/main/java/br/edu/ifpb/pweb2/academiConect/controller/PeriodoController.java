package br.edu.ifpb.pweb2.academiConect.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    // REQFUNC 2 - CRUD
    // REQNFUNC - Layout e Fragments
    @RequestMapping()
    public String listAll(Model model) {
        model.addAttribute("periodos", periodoRepository.findAll());
        return "periodos/listPeri";
    }

    // Rota para acessar o formunário
    // REQFUNC 5 - Período Atual
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Layout e Fragments
    @RequestMapping("/formPeri")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')") /*Só o perfil Admin tem autorização para acessa a classe */
    public ModelAndView getFormPeri(ModelAndView model) {
        model.addObject("periodo", new Periodo());
        model.setViewName("periodos/formPeri");
        return model;
    }

    // Rota para acessar o formunlário de atualização ou a lista se não for atualizar 
    // REQFUNC 5 - Período Atual
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Layout e Fragments
    @RequestMapping("/{id}")
    public ModelAndView getPeriodoById(@PathVariable(value = "id") Integer id, ModelAndView model) {
        Optional<Periodo> opPeriodo = periodoRepository.findById(id);
        if (opPeriodo.isPresent()) {
            Periodo periodo = opPeriodo.get();
            model.addObject("periodo", periodo);
            model.setViewName("periodos/formUpPeri");
        } else {
            model.addObject("errorMensagem", "Periodo informado não encontrado.");
            model.setViewName("periodos/listPeri");
        }
        return model;
    }

    // Rota para salvar novo objeto na lista
    // REQFUNC 2 - CRUD
    // REQFUNC 3 - Último periodo cadastrado se torna atual
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @RequestMapping(method = RequestMethod.POST)
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')") /*Só o perfil Admin tem autorização para acessa a classe */
    public ModelAndView save(@Valid Periodo periodo, BindingResult validation, ModelAndView mav, RedirectAttributes redAttrs) { 
        if(validation.hasErrors()) {
            mav.addObject("periodo", periodo);
			mav.setViewName("/periodos/formPeri");
			return mav;
        }
       Optional<Periodo> opPeriodo = periodoRepository.findByAnoPeriodoInstituicao(periodo.getAno(), periodo.getPeriodoLetivo(), periodo.getInstituicoes().get(0).getSigla());
        if (opPeriodo.isPresent()) {
            redAttrs.addFlashAttribute("errorMensagem", "Periodo já cadastrado no sistema!!");
            mav.setViewName("redirect:/periodos");     
        } else {
            String periodoExistente = periodo.getInstituicoes().get(0).getSigla();
           
              List<Periodo> periodosCadastrados =  periodoRepository.findAll();
              for (Periodo periodoAtual: periodosCadastrados) {
                for (Instituicao institu: periodoAtual.getInstituicoes() ){
                    if (institu.getSigla().equals(periodoExistente)){
                        periodoAtual.setPeriodoAtual(false);
                    }
                }    
              }
           
            Instituicao inst = periodo.getInstituicoes().get(0);//pega inst q esta sendo passada
            periodo.setPeriodoAtual(true);
            periodoRepository.save(periodo);
                 
            //pega o periodo e seta na lista da instituicao
            inst.getPeriodos().add(periodo);
            instituicaoRepository.save(inst); //salva periodo na instituicao

            mav.addObject("periodos", periodoRepository.findAll());
            mav.addObject("succesMensagem", "Período "+periodo.getPeriodoLetivo()+" cadastrado com sucesso!!");
            mav.setViewName("/periodos/listPeri");
        }  
        return mav;
    }

    // Rota para atualizar um objeto na lista
    // REQFUNC 2 - CRUD
    // REQNFUNC - Mostrar Erro nos Formulários
    @RequestMapping(value="/update", method = RequestMethod.POST)
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')") /*Só o perfil Admin tem autorização para acessa a classe */
    public ModelAndView updade(Periodo periodo, ModelAndView model) {
        periodoRepository.save(periodo);
        model.addObject("periodos", periodoRepository.findAll());
        model.addObject("succesMensagem", "Período "+periodo.getPeriodoLetivo()+", atualizado com sucesso!");
        //redAtt.addFlashAttribute("succesMensagem", "Período "+periodo.getPeriodo()+", atualizado com sucesso!");
        //model.setViewName("redirect:/periodos");
        model.setViewName("/periodos/listPeri");
        return model;
    }

    // Rota para deletar um objeto da lista
    // REQFUNC 2 - CRUD
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    //@PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessa a classe */
    @RequestMapping("{id}/delete")
    public ModelAndView deleteById(@PathVariable(value = "id") Integer id, ModelAndView model, RedirectAttributes redAtt) {
        Optional<Periodo> opPeriodo = periodoRepository.findById(id);
        if (opPeriodo.isPresent()) {
            Periodo periodo = opPeriodo.get();
            List<Instituicao> listInstituicao = periodo.getInstituicoes();
            for (Instituicao instituicao: listInstituicao) {
                instituicao.getPeriodos().remove(periodo);
            }
            periodoRepository.deleteById(id);
            redAtt.addFlashAttribute("succesMensagem", "Período "+periodo.getPeriodoLetivo()+" deletado com sucesso!!");
        } else {
            redAtt.addFlashAttribute("errorMensagem", "Período não pode ser deletada, contem instituição cadastrada.");
        }
        model.setViewName("redirect:/periodos");
        return model;
    }

     // Ativa o menu Período na barra de navegação
     @ModelAttribute("menu")
     public String activeMenu(){
         return "periodos";
     }

     // Rota para acessar a lista de Instituições no <select> do Form 
    @ModelAttribute("instituicoesItems")
    public List<Instituicao> getInstituicoes() {
        return instituicaoRepository.findAll();
    }

}
