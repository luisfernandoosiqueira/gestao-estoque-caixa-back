package app.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.dto.UsuarioRequestDTO;
import app.dto.UsuarioResponseDTO;
import app.entity.Usuario;
import app.enums.PerfilUsuario;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.UsuarioMapper;
import app.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    // ========= CONSULTAS =========

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAllByOrderByNomeCompletoAsc()
                .stream().map(usuarioMapper::toResponseDTO).toList();
    }

    public List<UsuarioResponseDTO> listarAtivos() {
        return usuarioRepository.findByAtivoOrderByNomeCompletoAsc(true)
                .stream().map(usuarioMapper::toResponseDTO).toList();
    }

    public List<UsuarioResponseDTO> listarPorPerfil(String perfil) {
        PerfilUsuario tipo = PerfilUsuario.valueOf(perfil.toUpperCase());
        return usuarioRepository.findByPerfil(tipo)
                .stream().map(usuarioMapper::toResponseDTO).toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
        return usuarioMapper.toResponseDTO(u);
    }

    // ========= CRIAR / ATUALIZAR / INATIVAR =========

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {
        validar(dto);

        if (usuarioRepository.existsByEmail(dto.email()))
            throw new NegocioException("E-mail já cadastrado.");

        Usuario novo = usuarioMapper.toEntity(dto);
        if (dto.ativo() == null) novo.setAtivo(true);
        Usuario salvo = usuarioRepository.save(novo);
        return usuarioMapper.toResponseDTO(salvo);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        validar(dto);

        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));

        if (!existente.getEmail().equalsIgnoreCase(dto.email()) &&
            usuarioRepository.existsByEmail(dto.email()))
            throw new NegocioException("E-mail já cadastrado.");

        usuarioMapper.updateEntity(existente, dto);
        Usuario atualizado = usuarioRepository.save(existente);
        return usuarioMapper.toResponseDTO(atualizado);
    }

    @Transactional
    public UsuarioResponseDTO inativar(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
        if (!u.getAtivo()) throw new NegocioException("Usuário já está inativo.");
        u.setAtivo(false);
        return usuarioMapper.toResponseDTO(usuarioRepository.save(u));
    }

    // ========= AUXILIAR =========
    private void validar(UsuarioRequestDTO dto) {
        if (dto == null) throw new NegocioException("Dados obrigatórios não informados.");
        if (dto.nomeCompleto() == null || dto.nomeCompleto().isBlank())
            throw new NegocioException("Nome completo é obrigatório.");
        if (dto.email() == null || dto.email().isBlank())
            throw new NegocioException("E-mail é obrigatório.");
        if (dto.senha() == null || dto.senha().length() < 8)
            throw new NegocioException("Senha deve conter pelo menos 8 caracteres.");
    }
}
