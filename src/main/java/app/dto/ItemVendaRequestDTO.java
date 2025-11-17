package app.dto;

import jakarta.validation.constraints.*;

public record ItemVendaRequestDTO(

    @NotNull(message = "O produto é obrigatório")
    Long produtoId,

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    Integer quantidade,

    @NotNull(message = "O preço unitário é obrigatório")
    @PositiveOrZero(message = "O preço unitário não pode ser negativo")
    Double precoUnitario,

    @NotNull(message = "O subtotal é obrigatório")
    @PositiveOrZero(message = "O subtotal não pode ser negativo")
    Double subtotal
) {}
