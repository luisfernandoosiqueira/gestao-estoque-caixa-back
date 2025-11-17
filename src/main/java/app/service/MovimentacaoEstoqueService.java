package app.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.dto.MovimentacaoEstoqueRequestDTO;
import app.dto.MovimentacaoEstoqueResponseDTO;
import app.entity.MovimentacaoEstoque;
import app.entity.Produto;
import app.enums.TipoMovimentacao;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.MovimentacaoEstoqueMapper;
import app.repository.MovimentacaoEstoqueRepository;
import app.repository.ProdutoRepository;

@Service
public class MovimentacaoEstoqueService {

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MovimentacaoEstoqueMapper movimentacaoMapper;

    // ========= CONSULTAS =========

    public List<MovimentacaoEstoqueResponseDTO> listarTodas() {
        return movimentacaoRepository.findAllByOrderByDataHoraDesc()
                .stream().map(movimentacaoMapper::toResponseDTO).toList();
    }

    public List<MovimentacaoEstoqueResponseDTO> listarPorProduto(Long produtoId) {
        return movimentacaoRepository.findByProdutoId(produtoId)
                .stream().map(movimentacaoMapper::toResponseDTO).toList();
    }

    public List<MovimentacaoEstoqueResponseDTO> listarPorTipo(String tipo) {
        return movimentacaoRepository.findByTipo(tipo)
                .stream().map(movimentacaoMapper::toResponseDTO).toList();
    }

    public List<MovimentacaoEstoqueResponseDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return movimentacaoRepository.findByDataHoraBetween(inicio, fim)
                .stream().map(movimentacaoMapper::toResponseDTO).toList();
    }

    // ========= REGISTRO =========

    @Transactional
    public MovimentacaoEstoqueResponseDTO registrar(MovimentacaoEstoqueRequestDTO dto) {
        validar(dto);

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new NegocioException("Produto não encontrado."));

        MovimentacaoEstoque mov = movimentacaoMapper.toEntity(dto);
        mov.setProduto(produto);
        mov.setDataHora(LocalDateTime.now());

        if (mov.getTipo() == TipoMovimentacao.ENTRADA) {
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + mov.getQuantidade());
        } else if (mov.getTipo() == TipoMovimentacao.SAIDA) {
            if (produto.getQuantidadeEstoque() < mov.getQuantidade())
                throw new NegocioException("Estoque insuficiente.");
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - mov.getQuantidade());
        } else {
            produto.setQuantidadeEstoque(mov.getQuantidade());
        }

        produtoRepository.save(produto);
        MovimentacaoEstoque salva = movimentacaoRepository.save(mov);
        return movimentacaoMapper.toResponseDTO(salva);
    }

    // ========= AUXILIAR =========
    private void validar(MovimentacaoEstoqueRequestDTO dto) {
        if (dto == null) throw new NegocioException("Dados obrigatórios não informados.");
        if (dto.produtoId() == null) throw new NegocioException("Produto é obrigatório.");
        if (dto.tipo() == null) throw new NegocioException("Tipo é obrigatório.");
        if (dto.quantidade() == null || dto.quantidade() <= 0)
            throw new NegocioException("Quantidade deve ser maior que zero.");
    }
}
