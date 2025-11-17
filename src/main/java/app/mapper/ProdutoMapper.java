package app.mapper;

import org.springframework.stereotype.Component;

import app.dto.ProdutoRequestDTO;
import app.dto.ProdutoResponseDTO;
import app.entity.Produto;

@Component
public class ProdutoMapper {

    // DTO → Entidade
    public Produto toEntity(ProdutoRequestDTO dto) {
        if (dto == null) return null;

        Produto p = new Produto();
        p.setCodigo(dto.codigo());
        p.setNome(dto.nome());
        p.setCategoria(dto.categoria());
        p.setQuantidadeEstoque(dto.quantidadeEstoque());
        p.setPrecoUnitario(dto.precoUnitario());
        // se vier nulo, mantém padrão true da entidade
        if (dto.ativo() != null) {
            p.setAtivo(dto.ativo());
        }
        return p;
    }

    // Entidade → DTO
    public ProdutoResponseDTO toResponseDTO(Produto p) {
        if (p == null) return null;

        return new ProdutoResponseDTO(
                p.getId(),
                p.getCodigo(),
                p.getNome(),
                p.getCategoria(),
                p.getQuantidadeEstoque(),
                p.getPrecoUnitario(),
                p.getAtivo()
        );
    }

    // Atualização de entidade existente
    public void updateEntity(Produto destino, ProdutoRequestDTO dto) {
        if (destino == null || dto == null) return;

        if (dto.codigo() != null)            destino.setCodigo(dto.codigo());
        if (dto.nome() != null)              destino.setNome(dto.nome());
        if (dto.categoria() != null)         destino.setCategoria(dto.categoria());
        if (dto.quantidadeEstoque() != null) destino.setQuantidadeEstoque(dto.quantidadeEstoque());
        if (dto.precoUnitario() != null)     destino.setPrecoUnitario(dto.precoUnitario());
        if (dto.ativo() != null)             destino.setAtivo(dto.ativo());
    }
}
