
package br.com.mcpfarma.produto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogoPublicoControllerTest {

    @Mock
    private ProdutoRepository produtoRepository;

    private CatalogoPublicoController controller;

    @BeforeEach
    void configurar() {
        controller = new CatalogoPublicoController(produtoRepository);
    }

    @Test
    void deveRetornarCatalogoVazioQuandoNaoExistemProdutosPublicaveis() {

        Page<Produto> paginaVazia = new PageImpl<>(List.of());

        when(produtoRepository.findByAtivoTrueAndPublicavelTrue(
                any(Pageable.class)
        )).thenReturn(paginaVazia);

        ResponseEntity<?> resposta = controller.listar(0, 10);

        assertEquals(200, resposta.getStatusCode().value());
        assertInstanceOf(Page.class, resposta.getBody());

        Page<?> resultado = (Page<?>) resposta.getBody();

        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());

        verify(produtoRepository).findByAtivoTrueAndPublicavelTrue(
                any(Pageable.class)
        );

        verify(produtoRepository, never()).findByAtivoTrue(
                any(Pageable.class)
        );
    }

    @Test
    void deveRejeitarTamanhoDePaginaInvalido() {

        ResponseEntity<?> resposta = controller.listar(0, 200);

        assertEquals(400, resposta.getStatusCode().value());

        verifyNoInteractions(produtoRepository);
    }
}
