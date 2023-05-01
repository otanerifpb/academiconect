package br.edu.ifpb.pweb2.academiConect.model;

import java.io.Serializable;
import java.sql.Date;
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

    private String nome;

    private String matricula;

    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataNascimento;

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
