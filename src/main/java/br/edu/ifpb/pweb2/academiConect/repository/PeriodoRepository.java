package br.edu.ifpb.pweb2.academiConect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.ifpb.pweb2.academiConect.model.Periodo;

public interface PeriodoRepository extends JpaRepository<Periodo, Integer> {

    Optional<Periodo> findByPeriodo(String string);

    Optional<Periodo> findByAno(Integer ano);
    
    @Query(value = "select distinct p from Periodo p left join fetch p.instituicoes i where i.sigla = :instituicao and p.ano = :ano and p.periodo = :periodo ")
    Optional<Periodo> findByAnoPeriodoInstituicao(Integer ano, String periodo, String instituicao);
}
