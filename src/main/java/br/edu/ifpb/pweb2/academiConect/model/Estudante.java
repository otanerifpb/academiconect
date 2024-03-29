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
//import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
//import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    // @NotBlank(message = "Este campo é obrigatório!")
    // private String login;

    // @NotBlank(message = "Este campo é obrigatório!")
    // @Size(min = 6, max = 60, message = "Senha deve ter entre  6 a 60 caracteres")
    // private String senha;

    // Relação entre Estudante e User (1:1)
    // Uma vez que não temos mais solicitação de senha no Estudante, e sim uma vinculação
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    @OneToOne
    @JoinColumn(name = "username")
    @ToString.Exclude
    private User user;

    // Relação entre Estudante e Instituição (1:1)
    // Quando tem @ManyToOne é necessário add a class no @EqualsAndHashCode(exclude = {"instituicao", "declaracoes"})
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    @ManyToOne
    @JoinColumn(name = "id_instituicao")
    @ToString.Exclude
    private Instituicao instituicao;

    // Relação entre Estudante e Declaração (1:N)
    // Quando tem @OneToMany é necessário add a class no @EqualsAndHashCode(exclude = {"instituicao", "declaracoes"})
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    @OneToMany(mappedBy = "estudante", cascade = CascadeType.ALL)
    //@JoinColumn(name = "id_declaracao") // identificar coluna apenas no ManyToOne
    @ToString.Exclude
    private Set<Declaracao> declaracoes = new HashSet<Declaracao>();  

    // Para associar um Estudante a uma Instituição
    public Estudante(Instituicao instituicao) {
        this.instituicao = instituicao;
    }
    
    // Relação entre Estudante e Documento (1:1)
    // O @ToString.Exclude evita que o Lombok gere um loop infinito ao gerar o toString devido o relacionamento
    // @OneToOne
    // @JoinColumn(name = "id_documento")
    // @ToString.Exclude
    // private Documento documento;
    
}
