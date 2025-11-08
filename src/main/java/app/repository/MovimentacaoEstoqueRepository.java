package app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.MovimentacaoEstoque;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    List<MovimentacaoEstoque> findByProdutoId(Long produtoId);

    List<MovimentacaoEstoque> findByTipo(String tipo);

    List<MovimentacaoEstoque> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<MovimentacaoEstoque> findByProdutoNomeContainingIgnoreCase(String nomeProduto);

    List<MovimentacaoEstoque> findAllByOrderByDataHoraDesc();
}
