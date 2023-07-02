package br.edu.ifpb.pweb2.academiConect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import br.edu.ifpb.pweb2.academiConect.model.User;
import br.edu.ifpb.pweb2.academiConect.repository.DeclaracaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.EstudanteRepository;
import br.edu.ifpb.pweb2.academiConect.repository.InstituicaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.UserRepository;
import br.edu.ifpb.pweb2.academiConect.service.DocumentoService;
import groovy.transform.stc.FirstParam.Component;

//import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

@Controller
@RequestMapping("/estudantes") /*Rota para o acesso da class */
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessar */
public class EstudanteController {
    
    @Autowired
    private EstudanteRepository estudanteRepository;
    
    @Autowired
    private InstituicaoRepository instituicaoRepository; 

    @Autowired
    private DeclaracaoRepository declaracaoRepository; 

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentoService documentoService;

    // Rota para acessar a lista pelo menu
    @PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessar */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listAll(ModelAndView mav) {
        mav.addObject("estudantes", estudanteRepository.findAll());
        //List<Estudante> estudanteSemDeclaracao = estudanteRepository.buscaEstudanteQueNaoTemDeclaracao();
        mav.setViewName("estudantes/listEstu");
        return mav;
    }

    // Rota para acessar a lista ao usar o REDIRECT
    // REQFUNC 4 - CRUD
    // REQNFUNC - Layout e Fragments
    @PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessar */
    @RequestMapping()
    public String listAll(Model model) {
        model.addAttribute("estudantes", estudanteRepository.findAll());
        return "estudantes/listEstu";
    }

    // Rota para acessar o formunário
    // REQFUNC 6 - Instituição Atual
    // REQFUNC 8 - Instituição Atual
    // REQFUNC 13 - Autenticação e Autorização
    // REQNFUNC 05 - Mostrar Erro nos Formulários
    // REQNFUNC - Layout e Fragments
    @PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessar */
    @RequestMapping("/formEstu")
    public ModelAndView getFormEstu(Estudante estudante, ModelAndView mav) {
        mav.addObject("estudante", estudante);
        mav.setViewName("estudantes/formEstu");
        return mav;
    }

    // Rota para acessar o formunlário de atualização ou a lista se não for atualizar 
    // REQFUNC 06 - Instituição Atual
    // REQFUNC 08 - Instituição Atual
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Layout e Fragments cvb
    @RequestMapping("/{id}")
    @PreAuthorize("hasRole('USER', 'ADMIN')") /*Só o perfil Admin tem autorização para acessar */
    public ModelAndView getEstudanteById(@PathVariable(value = "id") Integer id, ModelAndView mav) {
        Optional<Estudante> opEstudante = estudanteRepository.findById(id);
       
        if (opEstudante.isPresent()) {
            Estudante estudante = opEstudante.get();
            mav.addObject("estudante", estudante);
            mav.setViewName("estudantes/formUpEstu");
        } else {
            mav.addObject("errorMensagem", "Estudante  não encontrado.");
            mav.setViewName("estudantes/listInst");
        }
        return mav;
    }

    // Rota para cadastrar um Estudante no Sitema
    // REQFUNC 04 - CRUD
    // REQFUNC 13 - Autenticação e Autorização
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessar */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView save(@Valid Estudante estudante, BindingResult validation, ModelAndView mav, RedirectAttributes redAttrs) {
        if(validation.hasErrors()) {
            mav.addObject("estudante", estudante);
            mav.setViewName("estudantes/formEstu");
            return mav;
        }
        Optional<Estudante> opEmail = estudanteRepository.findByEmail(estudante.getEmail());
        Optional<Estudante> opMatricula = estudanteRepository.findByMatricula(estudante.getMatricula());
        //if (opMatricula.isPresent() || opEmail.isPresent()) {
        if (opMatricula.isPresent()) {
            redAttrs.addFlashAttribute("errorMensagem", "Matrícula já cadastrada no sistema!!");
             mav.setViewName("redirect:/estudantes");
        } else if (opEmail.isPresent()) {
            redAttrs.addFlashAttribute("errorMensagem", "E-mail já cadastrado no sistema!!");
            mav.setViewName("redirect:/estudantes");
        } else {
            //estudante.setSenha(PasswordUtil.hashPassword(estudante.getSenha()));
            estudanteRepository.save(estudante);
            mav.addObject("estudantes", estudanteRepository.findAll());
            mav.addObject("succesMensagem", "Estudante cadastrado com sucesso!");
            mav.setViewName("estudantes/listEstu");
        }
        return mav;
    }

    // Rota para atualizar um objeto na lista pelo formUpEstu
    // REQFUNC 4 - CRUD
    // REQFUNC 6 - Instituição Atual
    // REQFUNC 8 - Instituição Atual
    // REQNFUNC - Mostrar Erro nos Formulários
    @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public ModelAndView updade(Estudante estudante, ModelAndView mav, RedirectAttributes redAttrs) {
        //Optional<Instituicao> listaInstituicao = instituicaoRepository.findBySigla(instituicao.getSigla());
        if (estudante.getInstituicao().getId() == null){
            estudante.setInstituicao(null);
        }
        estudanteRepository.save(estudante);
        mav.addObject("estudantes", estudanteRepository.findAll());
        //redAttrs.addFlashAttribute("succesMensagem", "Instituição atualizada com sucesso!");
        //mav.setViewName("redirect:/estudantes");
        mav.addObject("succesMensagem", "Estudante "+estudante.getNome()+", atualizado com sucesso!");
        mav.setViewName("/estudantes/listEstu"); 
        return mav;
    }

    // Rota para deletar um objeto da lista
    // REQFUNC 4 - CRUD
    // REQNFUNC - Mostrar Erro nos Formulários
    // REQNFUNC - Padrão Post_Redirect_Get
    @PreAuthorize("hasRole('ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("{id}/delete")
    public ModelAndView deleteById(@PathVariable(value = "id") Integer id, ModelAndView mav, RedirectAttributes redAttrs) {
        Optional<Estudante> opEstudante = estudanteRepository.findById(id);
        if (opEstudante.isPresent()) {
            Estudante estudante = opEstudante.get();
            Set<Declaracao> listaDeclaracao = estudante.getDeclaracoes();
            declaracaoRepository.deleteAll(listaDeclaracao);
            estudante.setInstituicao(null);
            estudanteRepository.deleteById(id);
            redAttrs.addFlashAttribute("succesMensagem", "Estudante "+estudante.getNome()+" deletado com sucesso!!");
        } else {
            redAttrs.addFlashAttribute("errorMensagem", "Estudante Não encontrado!!");
        }
        mav.setViewName("redirect:/estudantes");
        return mav;
    }

    // Ativa o menu Estudante na barra de navegação
    @ModelAttribute("menu")
    public String activeMenu(){
        return "estudantes";
    }

    // Calcular Idade do estudante
    @RequestMapping("/idade")
    public Integer calculate(@PathVariable Integer ano,@PathVariable Integer mes, @PathVariable Integer dia ) {
        LocalDate dataNascimento = LocalDate.of(ano, mes, dia);
        LocalDate dataHoje = LocalDate.now();
        Integer idade = Period.between(dataNascimento, dataHoje).getYears();
        return idade;
    }

    // Método para selecionar a Instituição no formEstu
    // REQFUNC 6 - Instituição Atual
    // REQFUNC 8 - Declaração Atual
    @ModelAttribute("instituicaoItems")
    public List<Instituicao> getInstituicao() {
        return instituicaoRepository.findAll();
    }

    // Acessar lista das declarações de um Estudante
    @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("{id}/listaDeclaracoes")
    public ModelAndView getEstudanteByMatricula(@PathVariable(value = "id") Integer id, ModelAndView mav) {
        Optional<Estudante> opEstudante = estudanteRepository.findById(id);
        //Optional<Estudante> opEstudante = estudanteRepository.findByMatricula(estudante.matricula);
        if (opEstudante.isPresent()) {
            Estudante estudante = opEstudante.get();
            Set<Declaracao> declaracoesEstudante = estudante.getDeclaracoes();
            if (!declaracoesEstudante.isEmpty()) {
                List<Declaracao> listDeclaracoes = new ArrayList<>();
                for (Declaracao declaracaoEstudante : declaracoesEstudante) {
                    listDeclaracoes.add(declaracaoEstudante);
                }
                mav.addObject("declaracoes", listDeclaracoes);
                mav.addObject("succesMensagem", "Declaração estudante encontrado com sucesso!!");
                mav.setViewName("declaracoes/listDecl");
            }else{
                mav.addObject("errorMensagem", "Estudante não tem declaração cadastrada!!");
                //mav.setViewName("/estudantes/listEstu");
                mav.setViewName("/declaracoes/listDecl");
            }
        } else {
            mav.addObject("errorMensagem", "Estudante não está cadastrado no sistema!!");
            mav.setViewName("estudantes/listEstu");
        }

        return mav;
    } 
    
    //Lista de usuários para o select do formEstu
    @ModelAttribute("users")
    public List<User> getUserOptions() {
        return userRepository.findByEnabledTrue();
    }

    // REQFUNC 12 - Upload de PDF
    // Método para acessar a lista dos Documentos de um Estudante
    @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("/{id}/documentos") 
    public ModelAndView getDocumentos(@PathVariable ("id") Integer id, ModelAndView mav) {
        Optional<Documento> opdocumento = documentoService.getDocumentoOf(id);
        if(opdocumento.isPresent()) {
            mav.addObject("documento", opdocumento.get());
        }
        mav.setViewName("estudantes/documentos/listDoc");
        return mav;
    }

    // REQFUNC 12 - Upload de PDF
    // Método para carregar o Documento de um Estudante
    @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping(value = "/{id}/documentos/upload", method = RequestMethod.POST)
    public ModelAndView fileUploadUri(@RequestParam("file") MultipartFile arquivo, 
            @PathVariable("id") Integer id, ModelAndView mav) {
        //String mensagem = "";
        String proxPagina = "";
        try{
            Optional<Estudante> opEstudante = estudanteRepository.findById(id);
            Estudante estudante = null;
            if(opEstudante.isPresent()) {
                estudante = opEstudante.get();
                String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());
                Documento documento = documentoService.saveDoc(estudante, nomeArquivo, 
                        arquivo.getBytes());
                documento.setUrl(this.buildUrl(estudante.getId(), documento.getId()));
                estudanteRepository.save(estudante);
                //mensagem = "Documento carregado com sucesso: " + arquivo.getOriginalFilename();
                mav.addObject("succesMensagem", "Documento carregado com sucesso: " 
                        + arquivo.getOriginalFilename() + "!!");
                proxPagina = String.format("redirect:/estudantes/%s/documentos", 
                        estudante.getId().toString());
            }
        } catch(Exception e) {
            mav.addObject("errorMensagem", "Não foi possível carregar o documento " 
                        + arquivo.getOriginalFilename() + "!!");
            // mensagem = "Não foi possível carregar o documento: " + arquivo.getOriginalFilename() 
            //         + "!! " + e.getMessage();
            proxPagina = "/estudantes/documentos/formDoc";
        }
        //mav.addObject("mensagem", mensagem);
        mav.setViewName(proxPagina);
        return mav;
    }

    // Método para usar no método fileUploadUri()
    private String buildUrl(Integer idEstudante, Integer idDocumento) {
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/estudantes")
                .path(String.valueOf(idEstudante))
                .path("/documentos")
                .path(String.valueOf(idDocumento))
                .toUriString();
        return fileDownloadUri;
    }

    // REQFUNC 12 - Upload de PDF
    // Método para devolver um documento binário PDF
    @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("/{id}/documentos/{idDoc}")
    public ResponseEntity<byte[]> getDocumento(@PathVariable("idDoc") Integer idDoc) {
        Documento documento = documentoService.getDocumento(idDoc);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documento.getNome() + "\"")
                .body(documento.getDados());
    }

    // REQFUNC 12 - Upload de PDF
    // Método para acessar o formDoc para salvar um novo Documento
    @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("/{id}/documentos/formDoc")
    public ModelAndView getFormDoc(@PathVariable(name = "id") Integer id, ModelAndView mav) {
        mav.addObject("id", id);
        mav.setViewName("estudantes/documentos/formDoc");
        return mav;
    }

    // REQNFUNC 09 - Paginação
    // Método para paginação 
    // @RequestMapping(method = RequestMethod.GET)
    // public ModelAndView listAll(ModelAndView mav, @RequestParam(defaultValue = "1") int page,
    //         @RequestParam(defaultValue = "3") int size) {
    //     Pageable paging = PageRequest.of(page - 1, size);
    //     Page<Estudante> pageEstudantes = estudanteRepository.findAll(paging);
    //     NavPage navPage = NavePageBuilder.newNavPage(pageEstudantes.getNumber() + 1,
    //             pageEstudantes.getTotalElements(), pageEstudantes.getTotalPages(), size);
    //     mav.addObject("estudantes", pageEstudantes);
    //     mav.addObject("navPage", navPage);
    //     mav.setViewName("estudantes/listEstu");
    //     return mav;
    // }

}
