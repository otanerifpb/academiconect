package br.edu.ifpb.pweb2.academiConect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifpb.pweb2.academiConect.model.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Integer>{

    @Query(value = "select e.documentos from Declaracao e where e.id = :idDeclaracao")
    Documento findDocumentoById(@Param ("idDeclaracao") Integer idDeclaracao);

    //@Query(value = "select e.documento from Estudante e where e.id = :idEstudante")
    //Documento findDocumentoById(@Param ("idEstudante") Integer idEstudante);
    
}
