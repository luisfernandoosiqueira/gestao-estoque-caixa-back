package app.dto;

import java.time.LocalDateTime;
import app.enums.TipoMovimentacao;

public record MovimentacaoEstoqueResponseDTO(
    Long id,
    TipoMovimentacao tipo,
    Integer quantidade,
    String motivo,
    LocalDateTime dataHora,
    ProdutoResponseDTO produto
) {}
