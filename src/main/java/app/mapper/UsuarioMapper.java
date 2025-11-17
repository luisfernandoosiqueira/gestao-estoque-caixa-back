package app.mapper;

import org.springframework.stereotype.Component;

import app.dto.UsuarioRequestDTO;
import app.dto.UsuarioResponseDTO;
import app.entity.Usuario;
import app.enums.PerfilUsuario;

@Component
public class UsuarioMapper {

    // DTO → Entidade
    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null;

        Usuario u = new Usuario();
        u.setNomeCompleto(dto.nomeCompleto());
        u.setEmail(dto.email());
        u.setSenha(dto.senha());
        if (dto.perfil() != null) {
            try {
                u.setPerfil(PerfilUsuario.valueOf(dto.perfil().toUpperCase()));
            } catch (IllegalArgumentException e) {
                u.setPerfil(PerfilUsuario.OPERADOR);
            }
        }
        if (dto.ativo() != null) {
            u.setAtivo(dto.ativo());
        }
        return u;
    }

    // Entidade → DTO
    public UsuarioResponseDTO toResponseDTO(Usuario u) {
        if (u == null) return null;

        return new UsuarioResponseDTO(
                u.getId(),
                u.getNomeCompleto(),
                u.getEmail(),
                (u.getPerfil() != null ? u.getPerfil().name() : null),
                u.getAtivo()
        );
    }

    // Atualização de entidade existente
    public void updateEntity(Usuario destino, UsuarioRequestDTO dto) {
        if (destino == null || dto == null) return;

        if (dto.nomeCompleto() != null) destino.setNomeCompleto(dto.nomeCompleto());
        if (dto.email() != null)        destino.setEmail(dto.email());
        if (dto.senha() != null)        destino.setSenha(dto.senha());
        if (dto.perfil() != null) {
            try {
                destino.setPerfil(PerfilUsuario.valueOf(dto.perfil().toUpperCase()));
            } catch (IllegalArgumentException e) {
                destino.setPerfil(PerfilUsuario.OPERADOR);
            }
        }
        if (dto.ativo() != null)        destino.setAtivo(dto.ativo());
    }
}
