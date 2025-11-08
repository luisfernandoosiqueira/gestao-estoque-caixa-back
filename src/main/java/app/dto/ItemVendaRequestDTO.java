package app.dto;

import jakarta.validation.constraints.*;

public record ItemVendaRequestDTO(

    @NotNull(message = "O produto é obrigatório")
    Long produtoId,

    @NotNull
    @Positive
    Integer quantidade,

    @NotNull
    @PositiveOrZero
    Double precoUnitario,

    @NotNull
    @PositiveOrZero
    Double subtotal
) {}
