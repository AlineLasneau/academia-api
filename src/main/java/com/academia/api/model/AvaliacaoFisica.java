package com.academia.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "avaliacoes_fisicas")
public class AvaliacaoFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @NotNull
    private LocalDate dataAvaliacao;

    @NotNull
    @DecimalMin("1.00")
    private BigDecimal peso;

    @NotNull
    @DecimalMin("0.50")
    private BigDecimal altura;

    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal percentualGordura;

    private String observacoes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }

    public LocalDate getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDate dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public BigDecimal getAltura() { return altura; }
    public void setAltura(BigDecimal altura) { this.altura = altura; }

    public BigDecimal getPercentualGordura() {
        return percentualGordura;
    }
    public void setPercentualGordura(BigDecimal percentualGordura) {
        this.percentualGordura = percentualGordura;
    }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}