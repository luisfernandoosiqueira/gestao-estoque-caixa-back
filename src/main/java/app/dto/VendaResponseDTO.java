package app.dto;

import java.time.LocalDateTime;
import java.util.List;

public record VendaResponseDTO(
    Long id,
    LocalDateTime dataHora,
    Double valorTotal,
    Double valorRecebido,
    Double troco,
    UsuarioResponseDTO usuario,
    List<ItemVendaResponseDTO> itens
) {}
