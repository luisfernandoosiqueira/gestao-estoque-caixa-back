package app.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.dto.ProdutoRequestDTO;
import app.dto.ProdutoResponseDTO;
import app.entity.Produto;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.ProdutoMapper;
import app.repository.ProdutoRepository;
import app.repository.ItemVendaRepository; // usado para validar vínculo com vendas

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    @Autowired
    private ItemVendaRepository itemVendaRepository; // verificação de uso em vendas

    // ========= CONSULTAS =========

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAllByOrderByNomeAsc()
                .stream().map(produtoMapper::toResponseDTO).toList();
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
        return produtoMapper.toResponseDTO(p);
    }

    public ProdutoResponseDTO buscarPorCodigo(String codigo) {
        Produto p = produtoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + codigo));
        return produtoMapper.toResponseDTO(p);
    }

    public List<ProdutoResponseDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome)
                .stream().map(produtoMapper::toResponseDTO).toList();
    }

    public List<ProdutoResponseDTO> buscarPorCategoria(String categoria) {
        return produtoRepository.findByCategoriaContainingIgnoreCaseOrderByNomeAsc(categoria)
                .stream().map(produtoMapper::toResponseDTO).toList();
    }

    public List<ProdutoResponseDTO> listarEstoqueBaixo(Integer limite) {
        return produtoRepository.findByQuantidadeEstoqueLessThanEqual(limite)
                .stream().map(produtoMapper::toResponseDTO).toList();
    }

    // ========= CRIAR / ATUALIZAR / DELETAR =========

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto) {
        validar(dto);
        if (produtoRepository.existsByCodigo(dto.codigo()))
            throw new NegocioException("Código de produto já cadastrado.");
        Produto salvo = produtoRepository.save(produtoMapper.toEntity(dto));
        return produtoMapper.toResponseDTO(salvo);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        validar(dto);
        Produto existente = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));
        if (!existente.getCodigo().equalsIgnoreCase(dto.codigo()) &&
            produtoRepository.existsByCodigo(dto.codigo()))
            throw new NegocioException("Código já cadastrado.");

        produtoMapper.updateEntity(existente, dto);
        return produtoMapper.toResponseDTO(produtoRepository.save(existente));
    }

    @Transactional
    public void deletar(Long id) {
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + id));

        // impede exclusão de produto vinculado a vendas
        if (itemVendaRepository.existsByProdutoId(id)) {
            throw new NegocioException("Produto já utilizado em vendas e não pode ser excluído.");
        }

        produtoRepository.delete(p);
    }

    // ========= AUXILIAR =========
    private void validar(ProdutoRequestDTO dto) {
        if (dto == null) throw new NegocioException("Dados obrigatórios não informados.");
        if (dto.codigo() == null || dto.codigo().isBlank())
            throw new NegocioException("Código é obrigatório.");
        if (dto.nome() == null || dto.nome().isBlank())
            throw new NegocioException("Nome é obrigatório.");
        if (dto.precoUnitario() == null || dto.precoUnitario() < 0)
            throw new NegocioException("Preço deve ser ≥ 0.");
        if (dto.quantidadeEstoque() == null || dto.quantidadeEstoque() < 0)
            throw new NegocioException("Quantidade deve ser ≥ 0.");
    }
}
