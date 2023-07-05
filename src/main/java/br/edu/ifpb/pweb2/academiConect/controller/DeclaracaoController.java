package br.edu.ifpb.pweb2.academiConect.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    DocumentoRepository documentoRepository;

    @Autowired
    private DocumentoService documentoService;

    //@Autowired
    //private DocumentoRepository documentoRepository;

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
            declaracao.setPeriodo(null);
            Documento doc = declaracao.getDocumento();
            declaracao.setDocumento(null);
            if (doc!= null){
                documentoRepository.delete(doc);
            }
            declaracaoRepository.delete(declaracao);
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
    // public List<Periodo> getPeriodos() {

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
    @RequestMapping("listaDocumentos")
    public ModelAndView listaDocumentos(ModelAndView mav) {
        mav.addObject("declaracoes", declaracaoRepository.findAll());
       // List<Declaracao> declaracoesVencidas = declaracaoRepository.findByDeclaracoesVencidas("UFPB",2022,"2021.2" );
        mav.setViewName("documentos/listDoc");
        return mav;
    }

    // Relacionamento class Declaração com class Documento
    // @ModelAttribute("documentoItems")
    // public List<Documento> getDocumentos() {
    //     return documentoRepository.findAll();
    // }

    // Rota para acessar a lista dos Documentos da Declaração de um Estudante
    // REQFUNC 12 - Upload/Download de PDF
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

    // Rota para acessar o formDoc para salvar um novo Documento
    // REQFUNC 12 - Upload/Download de PDF
    // @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("/{id}/documentos/formDoc")
    public ModelAndView getForm(@PathVariable(name = "id") Integer id, ModelAndView mav) {
        mav.addObject("id", id);
        mav.setViewName("documentos/formDoc");
        return mav;
    }

    // Rota para carregar o Documento da Declaração de um Estudante
    // REQFUNC 12 - Upload/Download de PDF
    // @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping(value = "/{id}/documentos/upload", method = RequestMethod.POST)
    public ModelAndView fileUploadUri(@RequestParam("file") MultipartFile arquivo, 
            @PathVariable("id") Integer id, ModelAndView mav) {
        //String mensagem = "";
        String proxPagina = "";
        try{
            Optional<Declaracao> opDeclaracao = declaracaoRepository.findById(id);
            Declaracao declaracao = null;
            if(opDeclaracao.isPresent()) {
                declaracao = opDeclaracao.get();
                String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());
                Documento documento = documentoService.saveDoc(declaracao, nomeArquivo, 
                        arquivo.getBytes());
                documento.setUrl(this.buildUrl(declaracao.getId(), documento.getId()));
                declaracao.setDocumento(documento);
                declaracaoRepository.save(declaracao);
                //mensagem = "Documento carregado com sucesso: " + arquivo.getOriginalFilename();
                mav.addObject("succesMensagem", "Documento carregado com sucesso: " 
                        + arquivo.getOriginalFilename() + "!!");
                proxPagina = String.format("redirect:/%s/documentos", 
                        declaracao.getId().toString());
            }
        } catch(Exception e) {
            mav.addObject("errorMensagem", "Não foi possível carregar o documento " 
                        + arquivo.getOriginalFilename() + "!!");
            // mensagem = "Não foi possível carregar o documento: " + arquivo.getOriginalFilename() 
            //         + "!! " + e.getMessage();
            proxPagina = "/documentos/formDoc";
        }
        //mav.addObject("mensagem", mensagem);
        mav.setViewName(proxPagina);
        return mav;
    }

    // Método para criar a URL para o método uploadDocumentos()
    private String buildUrl(Integer idDeclaracao, Integer idDocumento) {
        String fileUploadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/declaracoes")
                .path(String.valueOf(idDeclaracao))
                .path("/documentos")
                .path(String.valueOf(idDocumento))
                .toUriString();
        return fileUploadUri;
    }

    // Rota para fazer download do Documento PDF
    // REQFUNC 12 - Upload/Download de PDF
    // @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("/{id}/documentos/download")
    public ResponseEntity<byte[]> getDocumento(@PathVariable("id") Integer id) {
        Documento documento = documentoService.getDocumento(id);
        //Optional<Documento> opdocumento = documentoService.getDocumentoOf(id);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" 
                    + documento.getNome() + "\"")
                .body(documento.getDados());
    }

    // Rota para deletar um Documento PDF
    // REQFUNC 12 - Upload/Download de PDF
    @RequestMapping("{id}/documentos/delete")
    public ModelAndView deleteDocumentoById(@PathVariable(value = "id") Integer id, ModelAndView mav,
        RedirectAttributes redAttr) {
        Optional<Documento> opDocumento = documentoRepository.findById(id);
        //Optional<Declaracao> opDeclaracao = declaracaoRepository.findById(id);
        if (opDocumento.isPresent()) {
            Documento documento = opDocumento.get();
            Declaracao declaracao = declaracaoRepository.findDeclaracaoByIdDocument(documento.getId());
            //Declaracao declaracao = opDeclaracao.get();
            //Documento documento = declaracao.getDocumento();
            declaracao.setDocumento(null);
            //Declaracao declaracao = documento.get();

           
            //declaracaoRepository
            documentoRepository.delete(documento);
            //declaracao.setEstudante(null);
            //declaracaoRepository.deleteById(id);
            redAttr.addFlashAttribute("succesMensagem", "Documento Deletado com Sucesso!!");
        } else {
            redAttr.addFlashAttribute("errorMensagem", "Documento Não Encontrado!!");
        }
        mav.setViewName("redirect:/declaracoes");
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
