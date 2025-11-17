package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.dto.LoginRequestDTO;
import app.dto.LoginResponseDTO;
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
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    public List<UsuarioResponseDTO> listarAtivos() {
        return usuarioRepository.findByAtivoOrderByNomeCompletoAsc(true)
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    public List<UsuarioResponseDTO> listarPorPerfil(String perfil) {
        PerfilUsuario tipo = PerfilUsuario.valueOf(perfil.toUpperCase());
        return usuarioRepository.findByPerfil(tipo)
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
        return usuarioMapper.toResponseDTO(u);
    }

    // ========= LOGIN =========

    public LoginResponseDTO autenticar(LoginRequestDTO dto) {

        if (dto == null || dto.email() == null || dto.email().isBlank()
                || dto.senha() == null || dto.senha().isBlank()) {
            throw new NegocioException("E-mail e senha são obrigatórios.");
        }

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new NegocioException("Credenciais inválidas."));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new NegocioException("Usuário inativo.");
        }

        if (!usuario.getSenha().equals(dto.senha())) {
            throw new NegocioException("Credenciais inválidas.");
        }

        return new LoginResponseDTO(
                usuario.getNomeCompleto(),
                usuario.getEmail(),
                usuario.getPerfil().name()
        );
    }

    // ========= CRIAR / ATUALIZAR / ATIVAR / INATIVAR =========

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {
        validarNovo(dto);

        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new NegocioException("E-mail já cadastrado.");
        }

        Usuario novo = usuarioMapper.toEntity(dto);
        if (dto.ativo() == null) {
            novo.setAtivo(true);
        }

        Usuario salvo = usuarioRepository.save(novo);
        return usuarioMapper.toResponseDTO(salvo);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        validarAtualizacao(dto);

        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));

        if (!existente.getEmail().equalsIgnoreCase(dto.email())
                && usuarioRepository.existsByEmail(dto.email())) {
            throw new NegocioException("E-mail já cadastrado.");
        }

        // mantém a senha atual se não vier nova senha
        UsuarioRequestDTO dtoAjustado;
        if (dto.senha() == null || dto.senha().isBlank()) {
            dtoAjustado = new UsuarioRequestDTO(
                    dto.nomeCompleto(),
                    dto.email(),
                    existente.getSenha(),
                    dto.perfil(),
                    dto.ativo()
            );
        } else {
            dtoAjustado = dto;
        }

        usuarioMapper.updateEntity(existente, dtoAjustado);
        Usuario atualizado = usuarioRepository.save(existente);
        return usuarioMapper.toResponseDTO(atualizado);
    }

    @Transactional
    public UsuarioResponseDTO inativar(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));

        if (Boolean.FALSE.equals(u.getAtivo())) {
            throw new NegocioException("Usuário já está inativo.");
        }

        u.setAtivo(false);
        return usuarioMapper.toResponseDTO(usuarioRepository.save(u));
    }

    @Transactional
    public UsuarioResponseDTO ativar(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));

        if (Boolean.TRUE.equals(u.getAtivo())) {
            throw new NegocioException("Usuário já está ativo.");
        }

        u.setAtivo(true);
        return usuarioMapper.toResponseDTO(usuarioRepository.save(u));
    }

    // ========= AUXILIAR =========

    private void validarNovo(UsuarioRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.nomeCompleto() == null || dto.nomeCompleto().isBlank()) {
            throw new NegocioException("Nome completo é obrigatório.");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new NegocioException("E-mail é obrigatório.");
        }
        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new NegocioException("Senha é obrigatória.");
        }
        if (dto.senha().length() < 8) {
            throw new NegocioException("Senha deve conter pelo menos 8 caracteres.");
        }
    }

    private void validarAtualizacao(UsuarioRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.nomeCompleto() == null || dto.nomeCompleto().isBlank()) {
            throw new NegocioException("Nome completo é obrigatório.");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new NegocioException("E-mail é obrigatório.");
        }
        // senha só é validada se vier preenchida
        if (dto.senha() != null && !dto.senha().isBlank() && dto.senha().length() < 8) {
            throw new NegocioException("Senha deve conter pelo menos 8 caracteres.");
        }
    }
}
