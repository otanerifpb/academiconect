package br.edu.ifpb.pweb2.academiConect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.academiConect.model.Periodo;

public interface PeriodoRepository extends JpaRepository<Periodo, Integer> {

    Optional<Periodo> findByPeriodo(Integer integer);

    Optional<Periodo> findByAno(Integer ano);
    
}
