package com.academia.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "mensalidades")
@Getter
@Setter
@NoArgsConstructor
public class Mensalidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    @NotNull(message = "O aluno é obrigatório")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "plano_id", nullable = false)
    @NotNull(message = "O plano é obrigatório")
    private Plano plano;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "A data de vencimento é obrigatória")
    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @NotBlank(message = "A situação é obrigatória")
    @Pattern(
            regexp = "PENDENTE|PAGO|ATRASADO",
            message = "A situação deve ser PENDENTE, PAGO ou ATRASADO"
    )
    private String status = "PENDENTE";
}
