package app.dto;

import jakarta.validation.constraints.*;

public record LoginRequestDTO(

    @NotBlank
    @Email
    String email,

    @NotBlank
    String senha
) {}
