package br.edu.ifpb.pweb2.academiConect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.academiConect.model.Estudante;

//@Repository
public interface EstudanteRepository extends JpaRepository<Estudante, Integer>{
   
   Optional<Estudante> findByMatricula(String string);

   Optional<Estudante> findByEmail(String email);

   //Estudante findByEmail(String email);

   Optional<Estudante> findByNome(String nome);
    
}
