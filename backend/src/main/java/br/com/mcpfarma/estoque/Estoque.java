package br.com.mcpfarma.estoque;

import br.com.mcpfarma.produto.Produto;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @Column(name = "produto_id")
    private Long produtoId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm;

    protected Estoque() {
    }

    public Estoque(Produto produto, Integer quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto é obrigatório."
            );
        }

        this.produto = produto;
        atualizarQuantidade(quantidade);
    }

    @PrePersist
    @PreUpdate
    private void atualizarData() {
        this.atualizadoEm = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void atualizarQuantidade(Integer quantidade) {
        if (quantidade != null && quantidade < 0) {
            throw new IllegalArgumentException(
                "Estoque não pode ser negativo."
            );
        }

        this.quantidade = quantidade;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public OffsetDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}