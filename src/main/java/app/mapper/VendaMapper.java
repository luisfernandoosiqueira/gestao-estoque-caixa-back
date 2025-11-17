package app.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import app.dto.ItemVendaResponseDTO;
import app.dto.VendaRequestDTO;
import app.dto.VendaResponseDTO;
import app.dto.UsuarioResponseDTO;
import app.entity.ItemVenda;
import app.entity.Usuario;
import app.entity.Venda;

@Component
public class VendaMapper {

    private final ItemVendaMapper itemVendaMapper;
    private final UsuarioMapper usuarioMapper;

    public VendaMapper(ItemVendaMapper itemVendaMapper, UsuarioMapper usuarioMapper) {
        this.itemVendaMapper = itemVendaMapper;
        this.usuarioMapper = usuarioMapper;
    }

    // DTO → Entidade
    public Venda toEntity(VendaRequestDTO dto) {
        if (dto == null) return null;

        Venda v = new Venda();
        v.setValorRecebido(dto.valorRecebido());
        // valorTotal e troco são calculados e atribuídos no service
        return v;
    }

    // Entidade → DTO
    public VendaResponseDTO toResponseDTO(Venda v) {
        if (v == null) return null;

        UsuarioResponseDTO usuarioDTO = null;
        Usuario usuario = v.getUsuario();
        if (usuario != null) {
            usuarioDTO = usuarioMapper.toResponseDTO(usuario);
        }

        List<ItemVendaResponseDTO> itensDTO = null;
        List<ItemVenda> itens = v.getItens();
        if (itens != null && !itens.isEmpty()) {
            itensDTO = itens.stream()
                    .map(itemVendaMapper::toResponseDTO)
                    .collect(Collectors.toList());
        }

        return new VendaResponseDTO(
                v.getId(),
                v.getDataHora(),
                v.getValorTotal(),
                v.getValorRecebido(),
                v.getTroco(),
                usuarioDTO,
                itensDTO
        );
    }
}
