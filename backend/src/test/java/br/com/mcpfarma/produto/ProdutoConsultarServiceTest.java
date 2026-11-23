
package br.com.mcpfarma.produto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoConsultaServiceTest {

    @Mock
    private ProdutoRepository repository;

    private ProdutoConsultaService service;

    @BeforeEach
    void configurar() {
        service = new ProdutoConsultaService(repository);
    }

    @Test
    void deveRetornarVazioQuandoProdutoNaoExiste() {
        String ean = "7891234567890";

        when(repository.findByEan(ean))
                .thenReturn(Optional.empty());

        var resultado = service.buscarPorEan(ean);

        assertTrue(resultado.isEmpty());
        verify(repository).findByEan(ean);
    }


@Test
void deveRetornarProdutoAtivoPorEan() {

    String ean = "7891234567895";

    Produto produto = new Produto(
            null,
            ean,
            "Dipirona 500mg",
            "10 comprimidos"
    );

    when(repository.findByEan(ean))
            .thenReturn(Optional.of(produto));

    var resultado = service.buscarPorEan(ean);

    assertTrue(resultado.isPresent());
    assertEquals(ean, resultado.get().ean());
    assertEquals("Dipirona 500mg", resultado.get().nome());
    assertEquals("10 comprimidos", resultado.get().apresentacao());

    verify(repository).findByEan(ean);
}


@Test
void naoDeveRetornarProdutoInativo() {

    String ean = "7891234567895";

    Produto produto = new Produto(
            null,
            ean,
            "Produto Inativo",
            "10 comprimidos"
    );

    org.springframework.test.util.ReflectionTestUtils
            .setField(produto, "ativo", false);

    when(repository.findByEan(ean))
            .thenReturn(Optional.of(produto));

    var resultado = service.buscarPorEan(ean);

    assertTrue(resultado.isEmpty());

    verify(repository).findByEan(ean);
}


}
