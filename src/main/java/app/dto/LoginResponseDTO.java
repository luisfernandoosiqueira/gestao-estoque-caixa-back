package app.dto;

public record LoginResponseDTO(
    String nomeCompleto,
    String email,
    String perfil
) {}
