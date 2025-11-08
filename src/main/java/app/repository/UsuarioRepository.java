package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Usuario;
import app.enums.PerfilUsuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByAtivo(boolean ativo);

    List<Usuario> findByPerfil(PerfilUsuario perfil);

    List<Usuario> findByNomeCompletoContainingIgnoreCase(String nomeCompleto);

    List<Usuario> findByAtivoOrderByNomeCompletoAsc(boolean ativo);

    List<Usuario> findAllByOrderByNomeCompletoAsc();
}
