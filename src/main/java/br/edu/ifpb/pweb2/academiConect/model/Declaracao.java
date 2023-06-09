package br.edu.ifpb.pweb2.academiConect.model;

import java.io.Serializable;
import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;

//import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
// Para que não gere um loop na class do relacionamanto usar: exclude = nomeClassRelacionamento  para @OnoToMany ou @ManyToOne
@EqualsAndHashCode(exclude = {"estudante", "periodo", "documento"})
@AllArgsConstructor
@Entity
public class Declaracao implements Serializable {
    // Para garantir que a assinatura de um número seja única , para o uso do @Id
    private static final long serialVersionUID = 1L;

    @Id
    // Cria o Id de forma automática e identifica a chave primária como IDENTITY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Para gerar a String com o formato da data da entrada de dados na View (form.html)
    // Para o mês usar MM, mm é para minuto, MM = mês numerico; MMM = nome mês abreviado; >MMM = nome do mês
    // Para add "de" na data ${#dates.format(conta.data,"dd''de''MMM''de''yyyy")} para a saída de dados na View (list.html)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Este campo é obrigatório!")
    private Date dataRecebimento;

    @NotBlank(message = "Este campo é obrigatório!")
    private String observacao;

    private boolean declaracaoAtual;

    private Integer dias;
    
    // Relação entre Declaração e Estudante (1:1)
    // Quando tem @OneToMany é necessário add a class no @EqualsAndHashCode(exclude = {"estudante", "periodo"})
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    @ManyToOne
    @JoinColumn(name = "id_estudante")
    @ToString.Exclude
    private Estudante estudante;

    // Relação entre Declaração e PeriodoLetivo (1:1)
    // Quando tem @ManyToOne é necessário add a class no @EqualsAndHashCode(exclude = {"estudante", "periodo"})
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    @ManyToOne
    @JoinColumn(name = "id_periodo")
    @ToString.Exclude
    private Periodo periodo;
    
    // Fazer a ligação de uma declaração com um estudante
    public Declaracao(Estudante estudante) {
        this.estudante = estudante;
    }

    // Relação entre Declaração e Documento (1:1)
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    //@OneToMany(mappedBy = "declaracao", cascade = CascadeType.ALL)
    @OneToOne
    @JoinColumn(name = "id_documento")
    @ToString.Exclude
    private Documento documento;
    //private Set<Documento> documentos = new HashSet<Documento>();
        
}
