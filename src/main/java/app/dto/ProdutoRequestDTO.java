package app.dto;

import jakarta.validation.constraints.*;

public record ProdutoRequestDTO(

    @NotBlank
    @Size(max = 30)
    String codigo,

    @NotBlank
    @Size(min = 2, max = 120)
    String nome,

    @Size(max = 60)
    String categoria,

    @NotNull
    @PositiveOrZero
    Integer quantidadeEstoque,

    @NotNull
    @PositiveOrZero
    Double precoUnitario,

    // pode vir nulo em updates parciais; front normalmente envia true/false
    Boolean ativo
) {}
