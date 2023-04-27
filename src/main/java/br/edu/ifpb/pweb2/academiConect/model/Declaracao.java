package br.edu.ifpb.pweb2.academiConect.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// Para que não gere um loop na class do relacionamanto usar: exclude = nomeClassRelacionamento  para @OnoToMany ou @ManyToOne
@EqualsAndHashCode(exclude = {"estudante", "periodo"})
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
    //@DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date dataRecebimento;

    private String observacao;
    
    // Relação entre Declaração e Estudante (1:1)
    // Quando tem @OneToMany é necessário add a class no @EqualsAndHashCode(exclude = {"estudante", "periodo"})
    @ManyToOne
    @JoinColumn(name = "id_estudante")
    private Estudante estudante;

    // Relação entre Declaração e PeriodoLetivo (1:1)
    // Quando tem @ManyToOne é necessário add a class no @EqualsAndHashCode(exclude = {"estudante", "periodo"})
    @ManyToOne
    @JoinColumn(name = "id_periodo")
    private Periodo periodo;
    
    // Fazer a ligação de uma declaração com um estudante
    public Declaracao(Estudante estudante) {
        this.estudante = estudante;
    }
    
}
