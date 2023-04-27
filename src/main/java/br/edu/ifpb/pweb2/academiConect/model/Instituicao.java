package br.edu.ifpb.pweb2.academiConect.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"estudantes"})
@AllArgsConstructor
@Entity
public class Instituicao implements Serializable {
    // Para garantir que a assinatura de um número seja única , para o uso do "@Id"
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String sigla;

    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private String fone;

    // Relação entre Instituição e Estudante (1:N)
    // Se tem uma coleção, necessário criar uma fun para add o objeto encontrato "addEstudante"
    // Quando tem @OneToMany é necessário add a class no @EqualsAndHashCode(exclude = {"estudantes"})
    @OneToMany(mappedBy = "instituicao", cascade = CascadeType.ALL)
    private List<Estudante> estudantes;

    // Relação entre Instituição e PeriodoLetivo (1:N)
    @ManyToMany
    @JoinColumn(name = "id_periodo")
    private List<Periodo> periodo;

    // O uso de addEstudante, se o objeto vai ser adicionado à coleção existente
    // O uso de setEstudante, se o objeto ja existe e vai ser atualizado em uma propriedade privada estudanteAtual
    // public void addEstudante(Estudante estudante){
    //     this.estudantes.add(estudante);
    //     estudante.setInstituicao(this);
    // }
    
}
