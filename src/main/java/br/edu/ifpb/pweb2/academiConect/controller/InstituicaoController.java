package br.edu.ifpb.pweb2.academiConect.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.academiConect.model.Estudante;
import br.edu.ifpb.pweb2.academiConect.model.Instituicao;
import br.edu.ifpb.pweb2.academiConect.repository.EstudanteRepository;
import br.edu.ifpb.pweb2.academiConect.repository.InstituicaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.PeriodoRepository;
import br.edu.ifpb.pweb2.academiConect.ui.NavPage;
import br.edu.ifpb.pweb2.academiConect.ui.NavPageBuilder;

@Controller
@RequestMapping("/instituicoes") /*Rota para acessar a class */
public class InstituicaoController {
    @Autowired
    EstudanteRepository estudanteRepository;
    
    @Autowired
    InstituicaoRepository instituicaoRepository;

    @Autowired
    PeriodoRepository periodoRepository;

    // REQNFUNC 09 - Paginação
    // Rota para acessar a lista pelo menu com o GET
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')") /*Só o perfil Admin tem autorização para acessa a classe */
    @RequestMapping(method = RequestMethod.GET)
    // public ModelAndView listAll(ModelAndView mav) {
    //     mav.addObject("instituicoes", instituicaoRepository.findAll());
    //     mav.setViewName("instituicoes/listInst");
    //     return mav;
    // }
    public ModelAndView listAll(ModelAndView mav, @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size) {
        Pageable paging = PageRequest.of(page - 1, size);
        Page<Instituicao> pageInstituicoes = instituicaoRepository.findAll(paging);
        NavPage navPage = NavPageBuilder.newNavPage(pageInstituicoes.getNumber() + 1, 
            pageInstituicoes.getTotalElements(), pageInstituicoes.getTotalPages(), size);
        mav.addObject("instituicoes", pageInstituicoes);
        mav.addObject("navPage", navPage);
        mav.setViewName("instituicoes/listInst");
        return mav;
    }

    // Rota para acessar a lista ao usar a Rota da class
    // REQFUNC 1 - CRUD
    // REQFUNC 13 - Autenticação e Autorização
    @RequestMapping()
    @PreAuthorize("hasAnyRole('VIS','USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    public String listAll(Model model) {
        model.addAttribute("instituicoes", instituicaoRepository.findAll());
        return "instituicoes/listInst";
    }
    
    // Rota para acessar o formunário
    // REQNFUNC 5 - Mostrar Erro nos Formulários
    // REQFUNC 13 - Autenticação e Autorização 
    @RequestMapping("/formInst")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    public ModelAndView getForm(ModelAndView mav) {
        mav.addObject("instituicao", new Instituicao());
        mav.setViewName("instituicoes/formInst");
        return mav;
    }

    // Rota para acessar o formunlário de atualização ou a lista se não for atualizar 
    // REQNFUNC - Mostrar Erro nos Formulários
    @RequestMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    public ModelAndView getInstituicaoById(@PathVariable(value = "id") Integer id, ModelAndView mav) {
        Optional<Instituicao> opInstituicao = instituicaoRepository.findById(id);
        Instituicao instituicao = opInstituicao.get();
        if (opInstituicao.isPresent()) {
            mav.addObject("instituicao", opInstituicao.get());
            mav.setViewName("instituicoes/formUpInst");
        } else {
            mav.addObject("errorMensagem", "Instituição "+instituicao.getNome()+" não encontrado.");
            mav.setViewName("instituicoes/listInst");
        }
        return mav;
    }

    // Rota para salvar novo objeto na lista
    // REQFUNC 1 - CRUD
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView save(Instituicao instituicao, ModelAndView mav, RedirectAttributes redAttrs) {
        Optional<Instituicao> opInstituicao = instituicaoRepository.findBySigla(instituicao.getSigla());
        if (opInstituicao.isPresent()) {
            redAttrs.addFlashAttribute("errorMensagem", "Instituição "+instituicao.getSigla()+" já cadastrado no sistema!!");
            //model.addObject("errorMensagem", "Instituição "+instituicao.getSigla()+" já existe!!");
            mav.setViewName("redirect:/instituicoes");
        } else {
            instituicaoRepository.save(instituicao);
            mav.addObject("instituicoes", instituicaoRepository.findAll());
            //redAttrs.addFlashAttribute("succesMensagem", "Instituição cadastrada com sucesso!");
            mav.addObject("succesMensagem", "Instituição cadastrado com sucesso!");
            mav.setViewName("/instituicoes/listInst");
        }  
        return mav;
    }

    // Rota para atualizar um objeto na lista
    // REQFUNC 1 - CRUD
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public ModelAndView updade(Instituicao instituicao, ModelAndView mav, RedirectAttributes redAttrs) {
        instituicaoRepository.save(instituicao);
        mav.addObject("instituicoes", instituicaoRepository.findAll());
        //redAttrs.addFlashAttribute("succesMensagem", "Instituição atualizada com sucesso!");
        //model.setViewName("redirect:/instituicoes");
        mav.addObject("succesMensagem", "Instituição "+instituicao.getNome()+", atualizada com sucesso!");
        mav.setViewName("/instituicoes/listInst");    
        return mav;
    }

    // Rota para deletar um objeto da lista
    // REQFUNC 1 - CRUD
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @RequestMapping("{id}/delete")
    @PreAuthorize("hasRole('ADMIN')") /*Perfil que tem autorização para acessar */
    public ModelAndView deleteById(@PathVariable(value = "id") Integer id, ModelAndView mav, RedirectAttributes redAtt) {
        Optional<Instituicao> opInstituicao = instituicaoRepository.findById(id);
        Instituicao instituicao = null;
        if (opInstituicao.isPresent()) {
            instituicao = opInstituicao.get();
            List<Estudante> listEstudantes = instituicao.getEstudantes();
            if(listEstudantes.isEmpty()){
                instituicaoRepository.deleteById(id);
            }
            //instituicao.setPeriodoLetivo(null);
            redAtt.addFlashAttribute("succesMensagem", "Instituicao "+instituicao.getNome()+" deletado com sucesso!!");
        } else {
            redAtt.addFlashAttribute("errorMensagem", "Instituição não pode ser deletada, contem estudante cadastrado.");
        }
        mav.setViewName("redirect:/instituicoes");
        return mav;
    }

    // Ativa o menu Instituição na barra de navegação
     @ModelAttribute("menu")
     public String activeMenu(){
         return "instituicoes";
     }

    // Rota para acessar a Instituição em um Periodo
    @ModelAttribute("instituicaoItems")
    public List<Instituicao> getInstituicao() {
        return instituicaoRepository.findAll();
    }
    
}
