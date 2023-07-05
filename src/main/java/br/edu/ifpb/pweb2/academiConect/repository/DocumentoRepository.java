package br.edu.ifpb.pweb2.academiConect.repository;

//import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifpb.pweb2.academiConect.model.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Integer>{

    //@Query(value = "select d.documentos from Declaracao d where d.id = :idDeclaracao")
    //Set<Documento> findDocumentoById(@Param ("idDeclaracao") Integer idDeclaracao);

    @Query(value = "select d.documento from Declaracao d where d.id = :idDeclaracao")
    Documento findDocumentoById(@Param ("idDeclaracao") Integer idDeclaracao);
    
    }
