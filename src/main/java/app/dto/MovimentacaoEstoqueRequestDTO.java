package app.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import app.enums.TipoMovimentacao;

public record MovimentacaoEstoqueRequestDTO(

    @NotNull(message = "O produto é obrigatório")
    Long produtoId,

    @NotNull(message = "O tipo da movimentação é obrigatório")
    TipoMovimentacao tipo,

    @NotNull
    @Positive
    Integer quantidade,

    @Size(max = 255)
    String motivo,

    @PastOrPresent
    LocalDateTime dataHora
) {}
