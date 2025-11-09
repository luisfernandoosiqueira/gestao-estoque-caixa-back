package app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.dto.ItemVendaRequestDTO;
import app.dto.VendaRequestDTO;
import app.dto.VendaResponseDTO;
import app.entity.ItemVenda;
import app.entity.Produto;
import app.entity.Usuario;
import app.entity.Venda;
import app.exceptions.NegocioException;
import app.mapper.ItemVendaMapper;
import app.mapper.VendaMapper;
import app.repository.ProdutoRepository;
import app.repository.UsuarioRepository;
import app.repository.VendaRepository;

@Service
public class VendaService {

    @Autowired private VendaRepository vendaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private ItemVendaMapper itemVendaMapper;
    @Autowired private VendaMapper vendaMapper;

    // ========= CONSULTAS =========

    public List<VendaResponseDTO> listarTodas() {
        return vendaRepository.findAllByOrderByDataHoraDesc()
                .stream().map(vendaMapper::toResponseDTO).toList();
    }

    public List<VendaResponseDTO> listarPorUsuario(Long usuarioId) {
        return vendaRepository.findByUsuarioId(usuarioId)
                .stream().map(vendaMapper::toResponseDTO).toList();
    }

    public List<VendaResponseDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findByDataHoraBetween(inicio, fim)
                .stream().map(vendaMapper::toResponseDTO).toList();
    }

    // ========= REGISTRO =========

    @Transactional
    public VendaResponseDTO registrar(VendaRequestDTO dto) {
        validar(dto);

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new NegocioException("Usuário não encontrado."));

        Venda venda = vendaMapper.toEntity(dto);
        venda.setUsuario(usuario);
        venda.setDataHora(LocalDateTime.now());

        // Monta os itens sem salvar individualmente
        List<ItemVenda> itens = dto.itens().stream()
                .map(i -> criarItem(venda, i))
                .collect(Collectors.toList());
        venda.setItens(itens);

        // Calcula o total da venda e troco
        double total = itens.stream().mapToDouble(ItemVenda::getSubtotal).sum();
        venda.setValorTotal(total);

        if (dto.valorRecebido() < total)
            throw new NegocioException("Valor recebido insuficiente.");
        venda.setValorRecebido(dto.valorRecebido());
        venda.setTroco(dto.valorRecebido() - total);

        // ✅ Persiste a venda e os itens em cascata
        Venda salva = vendaRepository.save(venda);

        return vendaMapper.toResponseDTO(salva);
    }

    private ItemVenda criarItem(Venda venda, ItemVendaRequestDTO dto) {
        Produto p = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new NegocioException("Produto não encontrado."));

        if (p.getQuantidadeEstoque() < dto.quantidade())
            throw new NegocioException("Estoque insuficiente para " + p.getNome());

        // Atualiza estoque e persiste o produto
        p.setQuantidadeEstoque(p.getQuantidadeEstoque() - dto.quantidade());
        produtoRepository.save(p);

        // Cria o item sem salvar — ele será salvo pelo cascade
        ItemVenda item = itemVendaMapper.toEntity(dto);
        item.setVenda(venda);
        item.setProduto(p);

        // Se o subtotal não vier calculado, calcula aqui
        if (item.getSubtotal() == null || item.getSubtotal() == 0.0)
            item.setSubtotal(item.getQuantidade() * item.getPrecoUnitario());

        return item;
    }

    private void validar(VendaRequestDTO dto) {
        if (dto == null) throw new NegocioException("Dados obrigatórios não informados.");
        if (dto.usuarioId() == null) throw new NegocioException("Usuário é obrigatório.");
        if (dto.itens() == null || dto.itens().isEmpty())
            throw new NegocioException("A venda deve conter pelo menos um item.");
        if (dto.valorRecebido() == null || dto.valorRecebido() < 0)
            throw new NegocioException("Valor recebido deve ser informado e ≥ 0.");
    }
}
