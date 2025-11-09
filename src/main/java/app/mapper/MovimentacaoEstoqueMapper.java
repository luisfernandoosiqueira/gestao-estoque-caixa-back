package app.mapper;

import org.springframework.stereotype.Component;

import app.dto.MovimentacaoEstoqueRequestDTO;
import app.dto.MovimentacaoEstoqueResponseDTO;
import app.dto.ProdutoResponseDTO;
import app.entity.MovimentacaoEstoque;
import app.entity.Produto;
import app.enums.TipoMovimentacao;

@Component
public class MovimentacaoEstoqueMapper {

    private final ProdutoMapper produtoMapper;

    public MovimentacaoEstoqueMapper(ProdutoMapper produtoMapper) {
        this.produtoMapper = produtoMapper;
    }

    // DTO → Entidade
    public MovimentacaoEstoque toEntity(MovimentacaoEstoqueRequestDTO dto) {
        if (dto == null) return null;

        MovimentacaoEstoque m = new MovimentacaoEstoque();
        m.setTipo(dto.tipo() != null ? dto.tipo() : TipoMovimentacao.AJUSTE);
        m.setQuantidade(dto.quantidade());
        m.setMotivo(dto.motivo());
        m.setDataHora(dto.dataHora());
        // Produto será setado no Service após buscar por ID
        return m;
    }

    // Entidade → DTO
    public MovimentacaoEstoqueResponseDTO toResponseDTO(MovimentacaoEstoque m) {
        if (m == null) return null;

        ProdutoResponseDTO produtoDTO = null;
        Produto produto = m.getProduto();
        if (produto != null) {
            produtoDTO = produtoMapper.toResponseDTO(produto);
        }

        return new MovimentacaoEstoqueResponseDTO(
                m.getId(),
                m.getTipo(),
                m.getQuantidade(),
                m.getMotivo(),
                m.getDataHora(),
                produtoDTO
        );
    }
}
