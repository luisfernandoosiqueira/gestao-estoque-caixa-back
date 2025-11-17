package app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record VendaRequestDTO(

    @NotNull(message = "O usuário é obrigatório")
    Long usuarioId,

    @NotNull(message = "O valor recebido é obrigatório")
    @PositiveOrZero(message = "O valor recebido não pode ser negativo")
    Double valorRecebido,

    @NotEmpty(message = "A venda deve conter pelo menos um item")
    List<@Valid ItemVendaRequestDTO> itens
) {}
