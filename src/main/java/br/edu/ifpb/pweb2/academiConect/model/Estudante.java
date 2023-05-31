package br.edu.ifpb.pweb2.academiConect.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;


import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// Para que não gere um loop na class do relacionamanto usar: exclude = nomeClassRelacionamento  para @OnoToMany ou @ManyToOne
@EqualsAndHashCode(exclude = {"instituicao", "declaracoes"})
@AllArgsConstructor
@Entity
public class Estudante implements Serializable{
    // Para garantir que a assinatura de um número seja única , para o uso do @Id
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Este campo é obrigatório!")
    //@Size(max = 50, message = "Valor máximo para este campo é 50 caracteres!")
    private String nome;

    @NotBlank(message = "Este campo é obrigatório!")
    private String matricula;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Data deve ser no passado")
    @NotNull(message = "Este campo é obrigatório!")
    private Date dataNascimento;

    @NotBlank(message = "Este campo é obrigatório!")
    @Email(message = "Informe um e-mail válido!")
    private String email;

    private String senha;

    // Relação entre Estudante e Instituição (1:1)
    // Quando tem @ManyToOne é necessário add a class no @EqualsAndHashCode(exclude = {"instituicao", "declaracoes"})
    @ManyToOne
    @JoinColumn(name = "id_instituicao")
    private Instituicao instituicao;

    // Relação entre Estudante e Declaração (1:N)
    // Quando tem @OneToMany é necessário add a class no @EqualsAndHashCode(exclude = {"instituicao", "declaracoes"})
    @OneToMany(mappedBy = "estudante", cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_declaracao")
    private Set<Declaracao> declaracoes = new HashSet<Declaracao>();  

    // Para associar um Estudante a uma Instituição
    public Estudante(Instituicao instituicao) {
        this.instituicao = instituicao;
    }  
    
}
