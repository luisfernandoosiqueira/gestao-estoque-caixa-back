package app.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "TB_VENDA")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private Double valorTotal;

    @Column(nullable = false)
    private Double valorRecebido;

    @Column(nullable = false)
    private Double troco;

    //  Cascade ALL e orphanRemoval TRUE garantem que os itens sejam persistidos, atualizados e removidos junto com a venda.
    
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemVenda> itens = new ArrayList<>();

    public Venda() {
        this.dataHora = LocalDateTime.now();
        this.valorTotal = 0.0;
        this.valorRecebido = 0.0;
        this.troco = 0.0;
    }

    public Venda(Usuario usuario, LocalDateTime dataHora, Double valorTotal, Double valorRecebido, Double troco) {
        this.usuario = usuario;
        this.dataHora = (dataHora != null ? dataHora : LocalDateTime.now());
        this.valorTotal = (valorTotal != null ? valorTotal : 0.0);
        this.valorRecebido = (valorRecebido != null ? valorRecebido : 0.0);
        this.troco = (troco != null ? troco : 0.0);
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public Double getTroco() {
        return troco;
    }

    public void setTroco(Double troco) {
        this.troco = troco;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
        if (itens != null) {
            itens.forEach(item -> item.setVenda(this)); // ✅ Garante vínculo bidirecional
        }
    }

    // ✅ Método auxiliar para adicionar item individual com vínculo automático
    public void adicionarItem(ItemVenda item) {
        item.setVenda(this);
        this.itens.add(item);
    }

    // ✅ Método útil para cálculo automático no Service
    public void calcularTotais() {
        this.valorTotal = this.itens.stream()
                .mapToDouble(ItemVenda::getSubtotal)
                .sum();
        this.troco = (this.valorRecebido != null ? this.valorRecebido : 0.0) - this.valorTotal;
        if (this.troco < 0) this.troco = 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return Objects.equals(id, venda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
