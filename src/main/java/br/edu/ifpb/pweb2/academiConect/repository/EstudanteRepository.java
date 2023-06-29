package br.edu.ifpb.pweb2.academiConect.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifpb.pweb2.academiConect.model.Declaracao;
import br.edu.ifpb.pweb2.academiConect.model.Documento;
import br.edu.ifpb.pweb2.academiConect.model.Estudante;

//@Repository
public interface EstudanteRepository extends JpaRepository<Estudante, Integer>{
   
   Optional<Estudante> findByMatricula(String string);

   Optional<Estudante> findByEmail(String email);

   //Estudante findByEmail(String email);

   Optional<Estudante> findByNome(String nome);

   @Query(value = "select e.documento from Estudante e where e.id = :idEstudante")
    Documento findDocumentById(@Param ("idEstudante") Integer idEstudante);
    
    
   @Query("select est from Estudante as est "
   + " where not exists ( Select est from Declaracao as dec where dec.estudante = est ) " )
   Set<Estudante> findByStudantWintoutDeclaration();


//    SELECT * FROM estudante e
// where not exists (select * from declaracao d where e.id = d.id_estudante )
}
