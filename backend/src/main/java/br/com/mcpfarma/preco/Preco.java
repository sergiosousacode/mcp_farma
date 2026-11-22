
package br.com.mcpfarma.preco;

import br.com.mcpfarma.produto.Produto;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "preco")
public class Preco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "registrado_em",
            insertable = false,
            updatable = false)
    private OffsetDateTime registradoEm;

    protected Preco() {
    }

    public Preco(Produto produto, BigDecimal valor) {
        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto é obrigatório."
            );
        }

        if (valor == null || valor.signum() < 0
                || valor.scale() > 2) {
            throw new IllegalArgumentException(
                "Preço inválido."
            );
        }

        this.produto = produto;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public OffsetDateTime getRegistradoEm() {
        return registradoEm;
    }
}
