package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.ItemVenda;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

    // usado para validar se o produto já foi utilizado em alguma venda
    boolean existsByProdutoId(Long produtoId);

    List<ItemVenda> findByVendaId(Long vendaId);

    List<ItemVenda> findByProdutoId(Long produtoId);

    List<ItemVenda> findByProdutoNomeContainingIgnoreCase(String nomeProduto);
}
