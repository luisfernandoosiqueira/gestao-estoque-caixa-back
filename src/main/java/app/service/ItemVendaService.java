package app.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.dto.ItemVendaRequestDTO;
import app.dto.ItemVendaResponseDTO;
import app.entity.ItemVenda;
import app.entity.Produto;
import app.entity.Venda;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.ItemVendaMapper;
import app.repository.ItemVendaRepository;
import app.repository.ProdutoRepository;
import app.repository.VendaRepository;

@Service
public class ItemVendaService {

    @Autowired private ItemVendaRepository itemVendaRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private VendaRepository vendaRepository;
    @Autowired private ItemVendaMapper itemVendaMapper;

    // ========= CONSULTAS =========

    public List<ItemVendaResponseDTO> listarPorVenda(Long vendaId) {
        return itemVendaRepository.findByVendaId(vendaId)
                .stream().map(itemVendaMapper::toResponseDTO).toList();
    }

    public ItemVendaResponseDTO buscarPorId(Long id) {
        ItemVenda item = itemVendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado: " + id));
        return itemVendaMapper.toResponseDTO(item);
    }

    // ========= CRIAR / EXCLUIR =========

    @Transactional
    public ItemVendaResponseDTO salvar(Long vendaId, ItemVendaRequestDTO dto) {
        validar(dto);

        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new NegocioException("Venda não encontrada."));

        Produto p = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new NegocioException("Produto não encontrado."));
        if (p.getQuantidadeEstoque() < dto.quantidade())
            throw new NegocioException("Estoque insuficiente para este produto.");

        p.setQuantidadeEstoque(p.getQuantidadeEstoque() - dto.quantidade());
        produtoRepository.save(p);

        ItemVenda item = itemVendaMapper.toEntity(dto);
        item.setVenda(venda);
        item.setProduto(p);
        return itemVendaMapper.toResponseDTO(itemVendaRepository.save(item));
    }

    @Transactional
    public void deletar(Long id) {
        ItemVenda item = itemVendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado: " + id));
        Produto p = item.getProduto();
        p.setQuantidadeEstoque(p.getQuantidadeEstoque() + item.getQuantidade());
        produtoRepository.save(p);
        itemVendaRepository.delete(item);
    }

    // ========= AUXILIAR =========
    private void validar(ItemVendaRequestDTO dto) {
        if (dto == null) throw new NegocioException("Dados obrigatórios não informados.");
        if (dto.produtoId() == null) throw new NegocioException("Produto é obrigatório.");
        if (dto.quantidade() == null || dto.quantidade() <= 0)
            throw new NegocioException("Quantidade deve ser maior que zero.");
        if (dto.precoUnitario() == null || dto.precoUnitario() < 0)
            throw new NegocioException("Preço unitário deve ser ≥ 0.");
    }
}
