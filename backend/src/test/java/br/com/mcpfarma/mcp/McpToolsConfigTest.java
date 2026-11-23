
package br.com.mcpfarma.mcp;

import br.com.mcpfarma.produto.ProdutoConsultaService;
import org.junit.jupiter.api.Test;
import org.springframework.ai.tool.ToolCallbackProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class McpToolsConfigTest {

    @Test
    void deveRegistrarFerramentaDeConsultaPorEan() {
        ProdutoConsultaService service =
                mock(ProdutoConsultaService.class);

        ProdutoMcpTools tools = new ProdutoMcpTools(service);

        ToolCallbackProvider provider =
                new McpToolsConfig()
                        .produtoToolCallbackProvider(tools);

        var callbacks = provider.getToolCallbacks();

        assertEquals(1, callbacks.length);

        assertEquals(
                "consultar_produto_por_ean",
                callbacks[0].getToolDefinition().name()
        );
    }
}
