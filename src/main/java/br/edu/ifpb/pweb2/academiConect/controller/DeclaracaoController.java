package br.edu.ifpb.pweb2.academiConect.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import br.edu.ifpb.pweb2.academiConect.model.Declaracao;
import br.edu.ifpb.pweb2.academiConect.model.Documento;
import br.edu.ifpb.pweb2.academiConect.model.Estudante;
import br.edu.ifpb.pweb2.academiConect.model.Instituicao;
import br.edu.ifpb.pweb2.academiConect.model.Periodo;
import br.edu.ifpb.pweb2.academiConect.repository.DeclaracaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.DocumentoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.EstudanteRepository;
import br.edu.ifpb.pweb2.academiConect.repository.PeriodoRepository;
import br.edu.ifpb.pweb2.academiConect.service.DocumentoService;

@Controller
@RequestMapping("/declaracoes") /*Rota para acessar a class */
//@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//@PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessar */
public class DeclaracaoController implements Serializable {
    @Autowired
    DeclaracaoRepository declaracaoRepository;

    @Autowired
    EstudanteRepository estudanteRepository;

    @Autowired
    PeriodoRepository periodoRepository;

    @Autowired
    private DocumentoService documentoService;

    @Autowired
    private DocumentoRepository documentoRepository;

    // Rota para acessar a lista pelo formDecEstu
    //@PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessar */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listAll(ModelAndView mav) {
        mav.addObject("declaracoes", declaracaoRepository.findAll());
       // List<Declaracao> declaracoesVencidas = declaracaoRepository.findByDeclaracoesVencidas("UFPB",2022,"2021.2" );
        mav.setViewName("declaracoes/listDecl");
        return mav;
    }

    // Rota para acessar a lista com o uso do REDIRECT
    // REQFUNC 4 - CRUD
    //@PreAuthorize("hasRole('ADMIN')")
    @RequestMapping()
    public String listAll(Model model) {
        model.addAttribute("declaracoes", declaracaoRepository.findAll());
        return "declaracoes/listDecl";
    }

    // Rota para acessar o Formulário de Cadastrar Declaração
    // REQFUNC 6 - Instituição Atual
    // REQFUNC 8 - Instituição Atual
    // REQNFUNC - Mostrar Erro nos Formulários
    @RequestMapping("/formDecl")
    public ModelAndView getFormDecl(Declaracao declaracao, ModelAndView mav) {
        String mostrarForm = "false";
        mav.addObject("declaracao", declaracao);
        mav.addObject("mostrarForm", mostrarForm);
        mav.setViewName("declaracoes/formDecl");
        return mav;
    }

    // Rota para acessar o Formulário de Declaração de Estudante pelo menu
    @RequestMapping("/formDecEstu")
    public ModelAndView getFormDecEstu(Declaracao declaracao, ModelAndView mav) {
        mav.addObject("declaracao", declaracao);
        mav.setViewName("declaracoes/formDecEstu");
        return mav;
    }

    // Mapeamanto para buscar as declarações de um Estudante, ao pasar uma matricula
    // cadastrada
    @RequestMapping("/getDecEstudante")
    public ModelAndView getDeclaracaoEstudante(Declaracao declaracao, ModelAndView mav) {
        Optional<Estudante> opEstudante = estudanteRepository.findByMatricula(declaracao.getEstudante().getMatricula());
        if (opEstudante.isPresent()) {
            Estudante estudante = opEstudante.get();
            Set<Declaracao> declaracoesEstudante = estudante.getDeclaracoes();
            if (!declaracoesEstudante.isEmpty()) {
                List<Declaracao> listDeclaracoes = new ArrayList<>();
                for (Declaracao declaracaoEstudante : declaracoesEstudante) {
                    listDeclaracoes.add(declaracaoEstudante);
                }
                mav.addObject("declaracoes", listDeclaracoes);
                mav.addObject("succesMensagem", "Estudante encontrado com sucesso!!");
                mav.setViewName("declaracoes/listDecl");
            }else{
                mav.addObject("errorMensagem", "Estudante não tem declaração cadastrada!!");
                mav.setViewName("/declaracoes/formDecEstu");
            }
        } else {
        mav.addObject("errorMensagem", "Estudante não cadastrado no sistema!!");
        mav.setViewName("/declaracoes/formDecEstu");
        }
        return mav;
    }

    // Rota para acessar o formunlário de atualização ou a lista se não for
    // atualizar
    // REQFUNC 6 - Instituição Atual
    // REQFUNC 8 - Instituição Atual
    // REQNFUNC - Mostrar Erro nos Formulários
    @RequestMapping("/{id}")
    public ModelAndView getInstituicaoById(@PathVariable(value = "id") Integer id, ModelAndView mav) {
        Optional<Declaracao> opDeclaracao = declaracaoRepository.findById(id);
        if (opDeclaracao.isPresent()) {
            Declaracao declaracao = opDeclaracao.get();
            mav.addObject("declaracao", declaracao);
            mav.setViewName("declaracoes/formUpDecl");
        } else {
            mav.addObject("errorMensagem", "Declaração não encontrado.");
            mav.setViewName("declaracoes/listDecl");
        }
        return mav;
    }

    // Rota para salvar novo objeto na lista com o uso do POST
    // REQFUNC 4 - CRUD
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView save(@Valid Declaracao declaracao, BindingResult validation, String matricula,
            ModelAndView mav, RedirectAttributes redAttrs) {
        if (validation.hasErrors()) {
            mav.addObject("declaracao", declaracao);
            Optional<Estudante> opEstudante = estudanteRepository.findByMatricula(matricula);
            mav.addObject("estudante", opEstudante.get());
            mav.addObject("mostrarForm", true);
            mav.setViewName("declaracoes/formDecl");
            return mav;
        }
        Optional<Estudante> opEstudante = estudanteRepository.findByMatricula(matricula);
        if (opEstudante.isPresent()) {
            Estudante estudante = opEstudante.get();
            Set<Declaracao> declaracoesEstudante = estudante.getDeclaracoes();
            for (Declaracao declaracaoAtual : declaracoesEstudante) {
                if (declaracaoAtual.isDeclaracaoAtual() == true) {
                    declaracaoAtual.setDeclaracaoAtual(false);
                }
            }
            declaracao.setDeclaracaoAtual(true);
            declaracao.setEstudante(estudante);
            declaracaoRepository.save(declaracao);
            mav.addObject("declaracoes", declaracaoRepository.findAll());
            mav.addObject("succesMensagem", "Declaração cadastrado com sucesso!");
            mav.setViewName("declaracoes/listDecl");
        } else {
            redAttrs.addFlashAttribute("errorMensagem", "Estudante não encontrado!!");
            mav.setViewName("redirect:/declaracoes");
        }
        return mav;
    }

    // Rota para atualizar um objeto na lista com o uso do POST
    // REQFUNC 4 - CRUD
    // REQFUNC 6 - Instituição Atual
    // REQFUNC 8 - Instituição Atual
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updade(Declaracao declaracao, ModelAndView mav, RedirectAttributes redAttrs) {
        Optional<Estudante> opEstudante = estudanteRepository.findByMatricula(declaracao.getEstudante().getMatricula());
        if (opEstudante.isPresent()) {
            Estudante estudante = opEstudante.get();
            declaracao.setEstudante(estudante);
            declaracaoRepository.save(declaracao);
            mav.addObject("declaracoes", declaracaoRepository.findAll());
            mav.addObject("succesMensagem", "Declaração atualizada com sucesso!!");
            mav.setViewName("declaracoes/listDecl");
        } else {
            mav.addObject("declaracoes", declaracaoRepository.findAll());
            redAttrs.addFlashAttribute("errorMensagem", "Estudante não encontrado!!");
            mav.setViewName("redirect:/declaracoes");
        }
        return mav;
    }

    // Rota para deletar um objeto da lista
    // REQFUNC 4 - CRUD
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    //@PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessar */
    @RequestMapping("{id}/delete")
    public ModelAndView deleteById(@PathVariable(value = "id") Integer id, ModelAndView mav,
            RedirectAttributes redAtt) {
        Optional<Declaracao> opDeclaracao = declaracaoRepository.findById(id);
        if (opDeclaracao.isPresent()) {
            Declaracao declaracao = opDeclaracao.get();
            declaracao.setEstudante(null);
            ;
            declaracao.setPeriodo(null);
            declaracaoRepository.deleteById(id);
            redAtt.addFlashAttribute("succesMensagem", "Declarção deletada com sucesso!!");
        } else {
            redAtt.addFlashAttribute("errorMensagem", "Declaração não encontrada.");
        }
        mav.setViewName("redirect:/declaracoes");
        return mav;
    }

    // Relacionamento class Declaração com class Estudante
    @ModelAttribute("estudanteItems")
    public List<Estudante> getEstudantes() {
        return estudanteRepository.findAll();
    }

    // Relacionamento class Declaração com class Período Letivo
    @ModelAttribute("periodosItems")
    public List<Periodo> getPeriodos() {
        return periodoRepository.findAll();
    }

    // Ativa o menu Declaração na barra de navegação
    @ModelAttribute("menu")
    public String activeMenu() {
        return "declaracoes";
    }

    // Rota para pesquisar o Período Letivo de um Estudante pela Matricula
    // REQFUNC 7 - Nova Declaração
    @RequestMapping("periodoInstituicao")
    public ModelAndView getByPeriodoInstituicao(String matricula, ModelAndView mav, RedirectAttributes redAtt) {
        String mostrarForm = "true";
        Optional<Estudante> opEstudante = estudanteRepository.findByMatricula(matricula);
        Estudante estudante = null;
        if (opEstudante.isPresent()) {
            estudante = opEstudante.get();
            Instituicao inst = estudante.getInstituicao();
            List<Periodo> periodos = inst.getPeriodos();
            mav.addObject("periodosInstituicao", periodos);
            mav.addObject("declaracao", new Declaracao());
            mav.addObject("estudante", estudante);
            mav.addObject("mostrarForm", mostrarForm);
            mav.setViewName("/declaracoes/formDecl");
        } else {
            redAtt.addFlashAttribute("errorMensagem", "Matrícula não encontrada!");
            mav.setViewName("redirect:/declaracoes");
        }
        return mav;
    }

    // Relacionamento class Declaração com class Documento
    // @ModelAttribute("documentoItems")
    // public List<Documento> getDocumentos() {
    //     return documentoRepository.findAll();
    // }

    // Método para acessar a lista de documentos
    @RequestMapping("listaDocumentos")
    public ModelAndView listaDocumentos(ModelAndView mav) {
        mav.addObject("declaracoes", declaracaoRepository.findAll());
       // List<Declaracao> declaracoesVencidas = declaracaoRepository.findByDeclaracoesVencidas("UFPB",2022,"2021.2" );
        mav.setViewName("documentos/listDoc");
        return mav;
    }

    // REQFUNC 12 - Upload de PDF
    // Método para acessar a lista dos Documentos de um Estudante
    // @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("/{id}/documentos") 
    public ModelAndView getDocumentos(@PathVariable ("id") Integer id, ModelAndView mav) {
        Optional<Documento> opdocumento = documentoService.getDocumentoOf(id);
        if(opdocumento.isPresent()) {
            mav.addObject("succesMensagem", "Documento Encontrado com Sucesso!!"); 
            mav.addObject("documento", opdocumento.get());
        } else {
            mav.addObject("errorMensagem", "Não foi Encontrado Nenhum Documento no Sistema!!"); 
        }
        mav.setViewName("documentos/listDoc");
        return mav;
    }

    // @PostMapping("/destino")
    // @RequestMapping(value = "/destino", method = RequestMethod.GET)
    // public ModelAndView destinoForm(@RequestParam("acao") String acao, ModelAndView mav) {
    //     String mostrarForm = "false";
    //     if(acao.equals("destino1")) {
    //         mav.addObject("mostrarForm", mostrarForm);
    //         mav.setViewName("declaracoes/formDecl");
            
    //         //mav.setViewName("/formDecl");
    //     } else if (acao.equals("destino2")) {
    //         mav.setViewName("/declaracoes/formDecEstu");
    //     } else if (acao.equals("destino3")) {
    //         mav.setViewName("/declaracoes/{id}/delete(id=${declaracao.id})");
    //     }
    //     return mav;
    // }
}
