package br.edu.ifpb.pweb2.academiConect.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"estudantes"})
@AllArgsConstructor
@Entity
@Table(name = "instituicao")
public class Instituicao implements Serializable {
    // Para garantir que a assinatura de um número seja única , para o uso do "@Id"
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Este campo é obrigatório!")
    private String nome;

    @NotBlank(message = "Este campo é obrigatório!")
    private String sigla;

    @NotBlank(message = "Este campo é obrigatório!")
    private String fone;

    // Relação entre Instituição e Estudante (1:N)
    // Se tem uma coleção, necessário criar uma fun para add o objeto encontrato "addEstudante"
    // Quando tem @OneToMany é necessário add a class no @EqualsAndHashCode(exclude = {"estudantes"})
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    @OneToMany(mappedBy = "instituicao", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Estudante> estudantes;

    // Relação entre Instituição e PeriodoLetivo (1:N)
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "periodo_intituicao",
        joinColumns = @JoinColumn(name = "instituicao_id"),
        inverseJoinColumns = @JoinColumn(name = "periodo_id") )
    @ToString.Exclude
    private List<Periodo> periodos;

    // O uso de addEstudante, se o objeto vai ser adicionado à coleção existente
    // O uso de setEstudante, se o objeto ja existe e vai ser atualizado em uma propriedade privada estudanteAtual
    public void addEstudante(Estudante estudante){
        this.estudantes.add(estudante);
        estudante.setInstituicao(this);
    }
    //add periodo nalista de intituicao
    public void addPeriodo(Periodo peri) {
        this.periodos.add(peri);
        peri.getInstituicoes().add(this);
      }

}
