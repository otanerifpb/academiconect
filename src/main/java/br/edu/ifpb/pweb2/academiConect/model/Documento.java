package br.edu.ifpb.pweb2.academiConect.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.JoinColumn;
import javax.persistence.Lob;
//import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import lombok.AllArgsConstructor;
import lombok.Data;
//import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
//import lombok.ToString;

// RECFUNC 12 - Upload PDF
@Data
@NoArgsConstructor
//@EqualsAndHashCode(exclude = {"declaracao"})
@AllArgsConstructor
@Entity
public class Documento implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Este campo é obrigatório!")
    private String nome;

    @URL
    private String url;

    @Lob
    private byte[] dados;

    public Documento (String nome, byte[] dados) {
        this.nome = nome;
        this.dados = dados;
    }

    // Relação entre Documento e Declaração (1:1)
    // Quando tem @OneToMany é necessário add a class no @EqualsAndHashCode(exclude = {"classA", "classB"})
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    // @ManyToOne
    // @JoinColumn(name = "id_declaracao")
    // @ToString.Exclude
    // private Declaracao declaracao;
}
