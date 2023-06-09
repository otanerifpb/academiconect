package br.edu.ifpb.pweb2.academiConect.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
//import javax.validation.constraints.Digits;
//import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

//import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

//import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
// Para que não gere um loop na class do relacionamanto usar: exclude = nomeClassRelacionamento  para @OnoToMany ou @ManyToOne
@EqualsAndHashCode(exclude = {"declaracoes", "instituicoes"})
@AllArgsConstructor
@Entity
@Table(name = "periodo")
public class Periodo implements Serializable {
    // Para garantir que a assinatura de um número seja única , para o uso do @Id
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Min(value = 2020, message = "Ano deve ser superior a 2020!")
    @Max(value = 9999, message = "Valor para este campo é com 4 caracteres!")
    //@Digits(integer = 4, fraction = 0, message = "Informe um númenro com 4 dígitos!")
    //@Range(min = 4, max = 4, message = "Valor para este campo é de 4 caracteres!")
    @NotNull(message = "Informe um Ano com 4 dígitosdo tipo 2020!")
    private Integer ano;

    @NotBlank(message = "Inform o Período Letivo do tipo 2020.1!")
    @Column(name="periodo", columnDefinition = "text")
    private String periodoLetivo;

    @NotNull(message = "Informe a data de início do Período!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataInicio;

    //@Future(message = "Data deve ser no futuro")
    @NotNull(message = "Inform a data de fim do Período!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataFim;

    private boolean PeriodoAtual;

    // Relação entre PerioLetivo e Instituição (1:N)
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    @ManyToMany(mappedBy = "periodos")
    @NotEmpty(message = "Selecione uma Instituição ou cadastre uma Instituição primeiro!")
    //@JoinColumn(name = "id_instituicao")
    @ToString.Exclude
    private List<Instituicao> instituicoes;

    // Relação entre PerioLetivo e Declaração (1:N)
    // Quando tem @OneToMany é necessário add a class no @EqualsAndHashCode(exclude = {"declaracoes"})
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    @OneToMany(mappedBy = "periodo", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Declaracao> declaracoes = new HashSet<Declaracao>();

    public void addDeclaracao(Declaracao declaracao){
        this.declaracoes.add(declaracao);
        declaracao.setPeriodo(this);
    }

    // Para associar um Periodo a uma Instituição
    public Periodo(Instituicao instituicao) {
       this.instituicoes.add(instituicao); 
    }

   //add instgituicao na lista de periodos
    public void addInstituicao(Instituicao inst) {
        this.instituicoes.add(inst);
        inst.getPeriodos().add(this);
      }
      
    
}
