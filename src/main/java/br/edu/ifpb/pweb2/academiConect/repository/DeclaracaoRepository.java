package br.edu.ifpb.pweb2.academiConect.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.edu.ifpb.pweb2.academiConect.model.Declaracao;
import lombok.Data;

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

  @Query("select dec from Declaracao dec "
  + "inner join fetch dec.periodo periodo "  
  + " where periodo.dataFim between current_date and :dataInformada " )
  Set<Declaracao> declarationForExpire(Data dataInformada);

   //@Query("SELECT e FROM Enrollment e WHERE e.semester.end < current_date")
   // List<Enrollment> findExpiredEnrollments(); 
}
