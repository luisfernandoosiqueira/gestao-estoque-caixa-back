package app.dto;

import jakarta.validation.constraints.*;

public record UsuarioRequestDTO(

    @NotBlank
    @Size(min = 3, max = 120)
    String nomeCompleto,

    @NotBlank
    @Email
    @Size(max = 120)
    String email,

    @NotBlank
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    String senha,

    @NotNull(message = "O perfil do usuário é obrigatório")
    String perfil, // ADMINISTRADOR ou OPERADOR

    Boolean ativo
) {}
