package br.edu.ifpb.pweb2.academiConect.repository;

//import java.time.LocalDate;
//import java.time.ZoneId;
import java.util.Date;
//import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifpb.pweb2.academiConect.model.Declaracao;
import br.edu.ifpb.pweb2.academiConect.model.Documento;
//import br.edu.ifpb.pweb2.academiConect.model.Estudante;
//import br.edu.ifpb.pweb2.academiConect.model.Periodo;


public interface DeclaracaoRepository extends JpaRepository<Declaracao, Integer> {

  @Query("select dec from Declaracao dec "
  + "inner join fetch dec.periodo periodo " 
  + "inner join fetch periodo.instituicoes i " 
  + "where periodo.periodoLetivo = :periodoInformado" 
  + " and periodo.ano = :anoInf"
  + " and periodo.dataFim < current_date "
  + " and i.sigla= :siglaInst ")
  Set<Declaracao> findByOverdueDeclarationByParams(String siglaInst, int anoInf, String periodoInformado);

@Query("select dec from Declaracao dec "
  + "inner join fetch dec.periodo periodo "  
  + " where periodo.dataFim < current_date " )
  Set<Declaracao> findByAllOverdueDeclaration();

  @Query("select dec from Declaracao as dec "
  + " where dec.periodo in "
  + " (select pe from Periodo as pe" 
  + " where pe.dataFim > :dataInformada and pe.dataFim < :dataInformada2 )" )
  Set<Declaracao> declarationForExpire(Date dataInformada, Date dataInformada2);

   //@Query("SELECT e FROM Enrollment e WHERE e.semester.end < current_date")
   // List<Enrollment> findExpiredEnrollments(); 

  @Query("select d.documento from Declaracao d where d.id = :idDeclaracao")
  Documento findDocumentById(Integer idDeclaracao);

  //@Query("select d.documentos from Declaracao d where d.id = :idDeclaracao")
  //Set<Documento> findDocumentById(Integer idDeclaracao);

  @Query(value = "select e.documento from Declaracao e where e.id = :idDeclaracao")
  Documento findDocumentoById(@Param ("idDeclaracao") Integer idDeclaracao);

  @Query(value = "select d from Declaracao as d where d.documento in (select doc from Documento as doc where doc.id = :idDocumento)")
    Declaracao findDeclaracaoByIdDocument(@Param ("idDocumento") Integer idDocumento);
    // @Query(value = "select distinct i from Instituicao i left join fetch i.declaracao d where i.id = :id")

}
