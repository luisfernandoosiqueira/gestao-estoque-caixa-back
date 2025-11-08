package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByCategoriaContainingIgnoreCase(String categoria);

    List<Produto> findByQuantidadeEstoqueLessThanEqual(Integer quantidade);

    List<Produto> findAllByOrderByNomeAsc();

    List<Produto> findByCategoriaContainingIgnoreCaseOrderByNomeAsc(String categoria);
}
