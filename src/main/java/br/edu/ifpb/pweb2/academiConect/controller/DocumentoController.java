package br.edu.ifpb.pweb2.academiConect.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.pweb2.academiConect.model.Instituicao;
import br.edu.ifpb.pweb2.academiConect.repository.DeclaracaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.EstudanteRepository;
import br.edu.ifpb.pweb2.academiConect.repository.InstituicaoRepository;
import br.edu.ifpb.pweb2.academiConect.repository.PeriodoRepository;

@Controller
@RequestMapping("/documentos") /*Rota para acessar a class */
public class DocumentoController implements Serializable {

    @Autowired
    DeclaracaoRepository declaracaoRepository;

    @Autowired
    EstudanteRepository estudanteRepository;

    @Autowired
    PeriodoRepository periodoRepository;

    @Autowired
    InstituicaoRepository instituicaoRepository;
    
}
