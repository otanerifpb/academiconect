package br.edu.ifpb.pweb2.academiConect.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// RECFUNC 12 - Upload PDF
@Data
@NoArgsConstructor
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
}
