package br.edu.ifpb.pweb2.academiConect.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

//import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// Para que não gere um loop na class do relacionamanto usar: exclude = nomeClassRelacionamento  para @OnoToMany ou @ManyToOne
@EqualsAndHashCode(exclude = {"declaracoes"})
@AllArgsConstructor
@Entity
public class Periodo implements Serializable {
    // Para garantir que a assinatura de um número seja única , para o uso do @Id
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer ano;

    private String periodo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataFim;

    private boolean PeriodoAtual;

    // Relação entre PerioLetivo e Instituição (1:N)
    @ManyToMany
    @JoinColumn(name = "id_instituicao")
    private List<Instituicao> instituicao;

    // Relação entre PerioLetivo e Declaração (1:N)
    // Quando tem @OneToMany é necessário add a class no @EqualsAndHashCode(exclude = {"declaracoes"})
    @OneToMany(mappedBy = "periodo", cascade = CascadeType.ALL)
    private Set<Declaracao> declaracoes = new HashSet<Declaracao>();

    // public void addDeclaracao(Declaracao declaracao){
    //     this.declaracoes.add(declaracao);
    //     declaracao.setPeriodoLetivo(this);
    // }
    
}
