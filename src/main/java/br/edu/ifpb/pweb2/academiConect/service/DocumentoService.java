package br.edu.ifpb.pweb2.academiConect.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.academiConect.model.Declaracao;
import br.edu.ifpb.pweb2.academiConect.model.Documento;
import br.edu.ifpb.pweb2.academiConect.model.Estudante;
import br.edu.ifpb.pweb2.academiConect.repository.DeclaracaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.DocumentoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.EstudanteRepository;

//REQFUNC 12 - Upload de PDF
@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private DeclaracaoRepository declaracaoRepository;
    

    public Documento saveDoc(Declaracao declaracao, String nomeArquivo, byte[] bytes) throws IOException {
        Documento documento = new Documento(nomeArquivo, bytes);
        //declaracao.setDocumento(documento);
        documento.setDeclaracao(declaracao);
        documentoRepository.save(documento);
        return documento;
    }

    public Documento getDocumento(Integer id) {
        return documentoRepository.findById(id).get();
    }

    public Optional<Documento> getDocumentoOf(Integer idDeclaracao) {
        return Optional.ofNullable(declaracaoRepository.findDocumentoById(idDeclaracao));
    }
    
}
