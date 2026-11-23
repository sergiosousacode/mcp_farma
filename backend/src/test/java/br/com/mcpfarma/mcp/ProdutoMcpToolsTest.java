
package br.com.mcpfarma.mcp;

import br.com.mcpfarma.produto.ProdutoConsultaService;
import br.com.mcpfarma.produto.ProdutoController;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoMcpToolsTest {

    private final ProdutoConsultaService service =
            mock(ProdutoConsultaService.class);

    private final ProdutoMcpTools tools =
            new ProdutoMcpTools(service);

    @Test
    void deveConsultarSomenteProdutoPublicavel() {
        String ean = "7891234567895";

        var produto = new ProdutoController.ProdutoResumo(
                1L,
                ean,
                "Produto Teste",
                "Caixa"
        );

        when(service.buscarPublicavelPorEan(ean))
                .thenReturn(Optional.of(produto));

        Object resultado = tools.consultarProdutoPorEan(ean);

        assertEquals(produto, resultado);

        verify(service).buscarPublicavelPorEan(ean);
        verify(service, never()).buscarPorEan(anyString());
    }

    @Test
    void deveRetornarNuloQuandoProdutoNaoForPublicavel() {
        String ean = "7891234567895";

        when(service.buscarPublicavelPorEan(ean))
                .thenReturn(Optional.empty());

        Object resultado = tools.consultarProdutoPorEan(ean);

        assertNull(resultado);

        verify(service).buscarPublicavelPorEan(ean);
    }
}
