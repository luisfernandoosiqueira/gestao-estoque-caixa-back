package app.mapper;

import org.springframework.stereotype.Component;

import app.dto.ItemVendaRequestDTO;
import app.dto.ItemVendaResponseDTO;
import app.dto.ProdutoResponseDTO;
import app.entity.ItemVenda;
import app.entity.Produto;

@Component
public class ItemVendaMapper {

    private final ProdutoMapper produtoMapper;

    public ItemVendaMapper(ProdutoMapper produtoMapper) {
        this.produtoMapper = produtoMapper;
    }

    public ItemVenda toEntity(ItemVendaRequestDTO dto) {
        if (dto == null) return null;

        ItemVenda i = new ItemVenda();
        i.setQuantidade(dto.quantidade());
        i.setPrecoUnitario(dto.precoUnitario());
        i.setSubtotal(dto.subtotal());
        // Produto e Venda são definidos no service
        return i;
    }

    public ItemVendaResponseDTO toResponseDTO(ItemVenda i) {
        if (i == null) return null;

        ProdutoResponseDTO produtoDTO = null;
        Produto produto = i.getProduto();
        if (produto != null) {
            produtoDTO = produtoMapper.toResponseDTO(produto);
        }

        return new ItemVendaResponseDTO(
                i.getId(),
                i.getQuantidade(),
                i.getPrecoUnitario(),
                i.getSubtotal(),
                produtoDTO
        );
    }
}
