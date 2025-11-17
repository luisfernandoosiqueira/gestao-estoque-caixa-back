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
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.ItemVendaMapper;
import app.mapper.VendaMapper;
import app.repository.ProdutoRepository;
import app.repository.UsuarioRepository;
import app.repository.VendaRepository;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemVendaMapper itemVendaMapper;

    @Autowired
    private VendaMapper vendaMapper;

    // ========= CONSULTAS =========

    public List<VendaResponseDTO> listarTodas() {
        return vendaRepository.findAllByOrderByDataHoraDesc()
                .stream()
                .map(vendaMapper::toResponseDTO)
                .toList();
    }

    public List<VendaResponseDTO> listarPorUsuario(Long usuarioId) {
        return vendaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(vendaMapper::toResponseDTO)
                .toList();
    }

    public List<VendaResponseDTO> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findByDataHoraBetween(inicio, fim)
                .stream()
                .map(vendaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public VendaResponseDTO buscarPorId(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Venda não encontrada com ID: " + id));
        return vendaMapper.toResponseDTO(venda);
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

        List<ItemVenda> itens = dto.itens().stream()
                .map(i -> criarItem(venda, i))
                .collect(Collectors.toList());
        venda.setItens(itens);

        double total = itens.stream()
                .mapToDouble(ItemVenda::getSubtotal)
                .sum();
        venda.setValorTotal(total);

        if (dto.valorRecebido() < total) {
            throw new NegocioException("Valor recebido insuficiente.");
        }

        venda.setValorRecebido(dto.valorRecebido());
        venda.setTroco(dto.valorRecebido() - total);

        Venda salva = vendaRepository.save(venda);
        return vendaMapper.toResponseDTO(salva);
    }

    private ItemVenda criarItem(Venda venda, ItemVendaRequestDTO dto) {
        Produto p = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new NegocioException("Produto não encontrado."));

        if (dto.quantidade() == null || dto.quantidade() <= 0) {
            throw new NegocioException("Quantidade do item deve ser maior que zero.");
        }

        if (p.getQuantidadeEstoque() < dto.quantidade()) {
            throw new NegocioException("Estoque insuficiente para " + p.getNome());
        }

        p.setQuantidadeEstoque(p.getQuantidadeEstoque() - dto.quantidade());
        produtoRepository.save(p);

        ItemVenda item = itemVendaMapper.toEntity(dto);
        item.setVenda(venda);
        item.setProduto(p);

        // subtotal calculado no back-end
        double precoUnitario = item.getPrecoUnitario() != null ? item.getPrecoUnitario() : 0.0;
        item.setSubtotal(dto.quantidade() * precoUnitario);

        return item;
    }

    private void validar(VendaRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.usuarioId() == null) {
            throw new NegocioException("Usuário é obrigatório.");
        }
        if (dto.itens() == null || dto.itens().isEmpty()) {
            throw new NegocioException("A venda deve conter pelo menos um item.");
        }
        if (dto.valorRecebido() == null || dto.valorRecebido() < 0) {
            throw new NegocioException("Valor recebido deve ser informado e não pode ser negativo.");
        }
    }
}
