package app.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record VendaRequestDTO(

    @NotNull(message = "O usuário é obrigatório")
    Long usuarioId,

    // calculado automaticamente pelo sistema
    @PositiveOrZero
    Double valorTotal,

    @NotNull(message = "O valor recebido é obrigatório")
    @PositiveOrZero
    Double valorRecebido,

    // calculado automaticamente pelo sistema
    @PositiveOrZero
    Double troco,

    @NotEmpty(message = "A venda deve conter pelo menos um item")
    List<ItemVendaRequestDTO> itens
) {}
