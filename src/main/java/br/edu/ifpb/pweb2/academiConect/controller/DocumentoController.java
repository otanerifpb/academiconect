package br.edu.ifpb.pweb2.academiConect.controller;

import java.io.IOException;
import java.io.Serializable;
//import java.util.List;
import java.util.Optional;
//import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
//import br.edu.ifpb.pweb2.academiConect.model.Estudante;
//import br.edu.ifpb.pweb2.academiConect.model.Instituicao;
import br.edu.ifpb.pweb2.academiConect.repository.DeclaracaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.DocumentoRepository;
//import br.edu.ifpb.pweb2.academiConect.repository.EstudanteRepository;
import br.edu.ifpb.pweb2.academiConect.repository.InstituicaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.PeriodoRepository;
//import br.edu.ifpb.pweb2.academiConect.service.DocumentoService;

@Controller
@RequestMapping("/documentos") /*Rota para acessar a class */
@PreAuthorize("hasAnyRole('USER', 'ADMIN')") /*Só o perfil Admin tem autorização para acessar */
public class DocumentoController implements Serializable {

    @Autowired
    DeclaracaoRepository declaracaoRepository;

    //@Autowired
    //EstudanteRepository estudanteRepository;

    @Autowired
    PeriodoRepository periodoRepository;

    @Autowired
    InstituicaoRepository instituicaoRepository;

    @Autowired
    DocumentoRepository documentoRepository;

    // REQFUNC 12 - Upload de PDF
    // Rota para acessar a lista pelo menu
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView listAll(ModelAndView mav) {
        mav.addObject("documentos", documentoRepository.findAll());
        //List<Estudante> estudanteSemDeclaracao = estudanteRepository.buscaEstudanteQueNaoTemDeclaracao();
        mav.setViewName("documentos/listDoc");
        return mav;
    }

    // Rota para acessar a lista ao usar o REDIRECT
    @RequestMapping()
    //@PreAuthorize("hasRole('ADMIN')") /*Só o perfil Admin tem autorização para acessar */
    public String listAll(Model model) {
        model.addAttribute("documentos", documentoRepository.findAll());
        return "documentos/listDoc";
    }

    // Rota para acessar o formDoc
    @PreAuthorize("hasRole('USER','ADMIN')") /*Só o perfil Admin tem autorização para acessar */
    @RequestMapping("/formDoc")
    public ModelAndView getFormDoc(Documento documento, ModelAndView mav) {
        mav.addObject("documento", documento);
        mav.setViewName("documentos/formDoc");
        return mav;
    }

    // Método para acessar o formDoc para salvar um novo Documento
    @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("/{id}/formDoc")
    public ModelAndView getFormUpDoc(@PathVariable(name = "id") Integer id, ModelAndView mav) {
        mav.addObject("id", id);
        mav.setViewName("documentos/formDoc");
        return mav;
    }

    // Método para acessar a lista dos Documentos de um Estudante
    //@PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("/{id}") 
    public ModelAndView getDocumento(@PathVariable ("id") Integer id, ModelAndView mav) {
        Optional<Documento> opdocumento = documentoRepository.findById(id);
        if(opdocumento.isPresent()) {
            Documento documento = opdocumento.get();
            mav.addObject("documento", documento);
        }
        mav.setViewName("documentos/listDoc");
        return mav;
    }
    
    // Método para realizar o upload de um Documento
    //@PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping(value = "/{id}/upload", method = RequestMethod.POST)
    public ModelAndView uploadDocumentos(@RequestParam("file") MultipartFile arquivo, 
            @PathVariable("id") Integer id, ModelAndView mav) {
        //String mensagem = "";
        String proxPagina = "";
        try{
            Optional<Declaracao> opDeclaracao = declaracaoRepository.findById(id);
            Declaracao declaracao = null;
            if(opDeclaracao.isPresent()) {
                declaracao = opDeclaracao.get();
                String nomeArquivo = StringUtils.cleanPath(arquivo.getOriginalFilename());
                Documento documento = saveDoc(declaracao, nomeArquivo, 
                        arquivo.getBytes());
                documento.setUrl(this.buildUrl(declaracao.getId(), documento.getId()));
                declaracaoRepository.save(declaracao);
                //mensagem = "Documento carregado com sucesso: " + arquivo.getOriginalFilename();
                mav.addObject("succesMensagem", "Documento carregado com sucesso: " 
                        + arquivo.getOriginalFilename() + "!!");
                //proxPagina = String.format("redirect:/declaracoes/%s/documentos", 
                proxPagina = String.format("redirect:/declaracoes/%s/documentos", 
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

    // Método para usar no método uploadDocumentos()
    public Documento saveDoc(Declaracao declaracao, String nomeArquivo, byte[] bytes) 
            throws IOException {
        Documento documento = new Documento(nomeArquivo, bytes);
        declaracao.setDocumento(documento);
        //documento.setDeclaracao(declaracao);
        documentoRepository.save(documento);
        return documento;
    }

    // Rota para deletar um Documento PDF
    @PreAuthorize("hasRole('ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("{id}/delete")
    public ModelAndView deleteById(@PathVariable(value = "id") Integer id, ModelAndView mav, RedirectAttributes redAttrs) {
       
        Optional<Declaracao> opDeclaracao = declaracaoRepository.findById(id);
        if (opDeclaracao.isPresent()) {
            Declaracao declaracao = opDeclaracao.get();
            Documento documento = declaracao.getDocumento();
            //Set<Documento> listaDocumento = declaracao.getDocumentos();
            documentoRepository.delete(documento);
            //documentoRepository.deleteAll(listaDocumento);
            //declaracao.setEstudante(null);
           /// declaracaoRepository.deleteById(id);
            //redAttrs.addFlashAttribute("succesMensagem", "Estudante "+estudante.getNome()+" deletado com sucesso!!");
            redAttrs.addFlashAttribute("succesMensagem", "Documento Deletado com Sucesso!!");
        } else {
            redAttrs.addFlashAttribute("errorMensagem", "Documento Não Encontrado!!");
        }
        mav.setViewName("redirect:/declaracoes");
        return mav;
    }

    // Método para fazer download do Documento PDF
    @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("/{id}/documentos/{idDoc}")
    public ResponseEntity<byte[]> getDocumento(@PathVariable("idDoc") Integer idDoc) {
        Documento documento = getDocumentoPorId(idDoc);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documento.getNome() + "\"")
                .body(documento.getDados());
    }

    // Método usado no getDocunento
    public Documento getDocumentoPorId(Integer id) {
        return documentoRepository.findById(id).get();
    }

    //
    public Optional<Documento> getDocumentoOf(Integer idDeclaracao) {
        return Optional.ofNullable(declaracaoRepository.findDocumentById(idDeclaracao));
    }

    // public Optional<Set<Documento>> getDocumentoOf(Integer idDeclaracao) {
    //     return Optional.ofNullable(declaracaoRepository.findDocumentById(idDeclaracao));
    // }

     // REQFUNC 12 - Upload de PDF
    // Método para acessar o formDoc para salvar um novo Documento
    @PreAuthorize("hasRole('USER', 'ADMIN')") /*Perfil que tem autorização para acessar */
    @RequestMapping("/{id}/documentos/formDoc")
    public ModelAndView getFormDoc(@PathVariable(name = "id") Integer id, ModelAndView mav) {
        mav.addObject("id", id);
        mav.setViewName("documentos/formDoc");
        return mav;
    }

}
