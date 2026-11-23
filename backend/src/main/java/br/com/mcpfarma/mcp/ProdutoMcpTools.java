
package br.com.mcpfarma.mcp;

import br.com.mcpfarma.produto.ProdutoConsultaService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMcpTools {

    private final ProdutoConsultaService produtoConsultaService;

    public ProdutoMcpTools(ProdutoConsultaService produtoConsultaService) {
        this.produtoConsultaService = produtoConsultaService;
    }

    @Tool(
        name = "consultar_produto_por_ean",
        description = "Consulta um produto ativo e autorizado para divulgação pelo código EAN."
    )
    public Object consultarProdutoPorEan(String ean) {

        return produtoConsultaService
                .buscarPublicavelPorEan(ean)
                .orElse(null);
    }
}
