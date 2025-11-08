package app.dto;

public record ItemVendaResponseDTO(
    Long id,
    Integer quantidade,
    Double precoUnitario,
    Double subtotal,
    ProdutoResponseDTO produto
) {}
