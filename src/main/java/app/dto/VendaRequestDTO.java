package app.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record VendaRequestDTO(

    @NotNull(message = "O usuário é obrigatório")
    Long usuarioId,

    @NotNull
    @PositiveOrZero
    Double valorTotal,

    @NotNull
    @PositiveOrZero
    Double valorRecebido,

    @NotNull
    @PositiveOrZero
    Double troco,

    @NotEmpty(message = "A venda deve conter pelo menos um item")
    List<ItemVendaRequestDTO> itens
) {}
