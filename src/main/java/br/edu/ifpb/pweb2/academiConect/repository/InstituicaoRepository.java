package br.edu.ifpb.pweb2.academiConect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;

//import br.edu.ifpb.pweb2.academiConect.model.Estudante;
import br.edu.ifpb.pweb2.academiConect.model.Instituicao;

//@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, Integer>{
    Optional<Instituicao> findBySigla(String sigla);
    //Instituicao findByEstudante(Estudante estudante);

   // @Query(value = "from Instituicao i left join fetch i.declaracao d where i.matricula = :matricula")
   // Instituicao findByMatriculaWithDeclaracoes(String matricula);
    
    // @Query(value = "select distinct i from Instituicao i left join fetch i.declaracao d where i.id = :id")
    // Instituicao findByIdWithDeclaracoes(Integer id);
}
