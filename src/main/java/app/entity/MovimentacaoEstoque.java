package app.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.*;
import app.enums.TipoMovimentacao;

@Entity
@Table(name = "TB_MOVIMENTACAO_ESTOQUE")
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentacao tipo; // ENTRADA, SAIDA ou AJUSTE

    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 255)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public MovimentacaoEstoque() {
        this.dataHora = LocalDateTime.now();
        this.tipo = TipoMovimentacao.AJUSTE;
        this.quantidade = 0;
    }

    public MovimentacaoEstoque(Produto produto, TipoMovimentacao tipo, Integer quantidade, String motivo, LocalDateTime dataHora) {
        this.produto = produto;
        this.tipo = (tipo != null ? tipo : TipoMovimentacao.AJUSTE);
        this.quantidade = (quantidade != null ? quantidade : 0);
        this.motivo = motivo;
        this.dataHora = (dataHora != null ? dataHora : LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovimentacaoEstoque that = (MovimentacaoEstoque) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
