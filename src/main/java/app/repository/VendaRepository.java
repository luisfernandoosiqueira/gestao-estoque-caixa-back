package app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByUsuarioId(Long usuarioId);

    // usado para bloquear exclusão de usuário com vendas
    boolean existsByUsuarioId(Long usuarioId);

    List<Venda> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Venda> findByValorTotalGreaterThanEqual(Double valor);

    List<Venda> findByValorTotalBetween(Double min, Double max);

    List<Venda> findAllByOrderByDataHoraDesc();
}
