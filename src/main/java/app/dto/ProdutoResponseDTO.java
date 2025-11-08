package app.dto;

public record ProdutoResponseDTO(
    Long id,
    String codigo,
    String nome,
    String categoria,
    Integer quantidadeEstoque,
    Double precoUnitario
) {}
