
package br.com.mcpfarma.produto.dto;

import java.math.BigDecimal;

public record ProdutoDisponibilidadeDTO(
        Long id,
        String ean,
        String nome,
        String apresentacao,
        BigDecimal precoAtual,
        Integer estoqueDisponivel
) {
}
