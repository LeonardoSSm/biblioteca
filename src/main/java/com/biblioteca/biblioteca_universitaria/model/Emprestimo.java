package com.biblioteca.biblioteca_universitaria.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataEmprestimo;

    private LocalDate dataDevolucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    @JsonIgnoreProperties({"emprestimos", "autor"}) // Ignora propriedades que podem causar loops
    private Livro livro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    @JsonIgnoreProperties("emprestimos") // Ignora a propriedade 'emprestimos' na serialização do aluno
    private Aluno aluno;

    private String status; // "ATIVO", "FINALIZADO", "ATRASADO"

    // Construtores
    public Emprestimo() {}

    public Emprestimo(LocalDate dataEmprestimo, Livro livro, Aluno aluno) {
        this.dataEmprestimo = dataEmprestimo;
        this.livro = livro;
        this.aluno = aluno;
        this.status = "ATIVO";
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString seguro
    @Override
    public String toString() {
        String livroTitulo = (livro != null) ? livro.getTitulo() : "N/A";
        String alunoNome = (aluno != null) ? aluno.getNome() : "N/A";
        return "Emprestimo [id=" + id + ", dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucao=" + dataDevolucao + ", status=" + status +
                ", livro=" + livroTitulo + ", aluno=" + alunoNome + "]";
    }
}
