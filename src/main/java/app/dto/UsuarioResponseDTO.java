package app.dto;

public record UsuarioResponseDTO(
    Long id,
    String nomeCompleto,
    String email,
    String perfil,
    Boolean ativo
) {}
