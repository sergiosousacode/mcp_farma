
package br.com.mcpfarma.produto;

import org.springframework.test.util.ReflectionTestUtils;

import br.com.mcpfarma.estoque.Estoque;
import br.com.mcpfarma.estoque.EstoqueRepository;
import br.com.mcpfarma.preco.Preco;
import br.com.mcpfarma.preco.PrecoRepository;
import br.com.mcpfarma.produto.dto.ProdutoDisponibilidadeDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PrecoRepository precoRepository;

    @Mock
    private EstoqueRepository estoqueRepository;

    private ProdutoController controller;

    @BeforeEach
    void configurar() {
        controller = new ProdutoController(
                produtoRepository,
                precoRepository,
                estoqueRepository
        );
    }

    @Test
    void deveRetornar404QuandoProdutoNaoExiste() {

        String ean = "7891234567890";

        when(produtoRepository.findByEan(ean))
                .thenReturn(Optional.empty());

        ResponseEntity<ProdutoDisponibilidadeDTO> resposta =
                controller.consultarDisponibilidade(ean);

        assertEquals(404, resposta.getStatusCode().value());
        assertNull(resposta.getBody());

        verifyNoInteractions(precoRepository, estoqueRepository);
    }

    @Test
    void deveRetornarPrecoEEstoqueNulosQuandoNaoCadastrados() {

        String ean = "7891234567895";

        Produto produto = new Produto(
                null,
                ean,
                "Produto Teste GTIN Valido",
                "10 comprimidos"
        );
        ReflectionTestUtils.setField(produto, "id", 4L);

        when(produtoRepository.findByEan(ean))
                .thenReturn(Optional.of(produto));

        when(precoRepository
                .findFirstByProdutoIdOrderByRegistradoEmDescIdDesc(4L))
                .thenReturn(Optional.empty());

        when(estoqueRepository.findById(4L))
                .thenReturn(Optional.empty());

        ResponseEntity<ProdutoDisponibilidadeDTO> resposta =
                controller.consultarDisponibilidade(ean);

        assertEquals(200, resposta.getStatusCode().value());
        assertNotNull(resposta.getBody());
        assertNull(resposta.getBody().precoAtual());
        assertNull(resposta.getBody().estoqueDisponivel());
        assertEquals(ean, resposta.getBody().ean());
    }
    @Test
void deveRetornarPrecoEEstoqueDoProduto() {

    String ean = "7891234567895";

    Produto produto = new Produto(
            null,
            ean,
            "Produto Teste GTIN Valido",
            "10 comprimidos"
    );

    ReflectionTestUtils.setField(produto, "id", 4L);

    Preco preco = new Preco(
            produto,
            new BigDecimal("15.90")
    );

    Estoque estoque = new Estoque(produto, 20);

    when(produtoRepository.findByEan(ean))
            .thenReturn(Optional.of(produto));

    when(precoRepository
            .findFirstByProdutoIdOrderByRegistradoEmDescIdDesc(4L))
            .thenReturn(Optional.of(preco));

    when(estoqueRepository.findById(4L))
            .thenReturn(Optional.of(estoque));

    ResponseEntity<ProdutoDisponibilidadeDTO> resposta =
            controller.consultarDisponibilidade(ean);

    assertEquals(200, resposta.getStatusCode().value());
    assertNotNull(resposta.getBody());

    ProdutoDisponibilidadeDTO dados = resposta.getBody();

    assertEquals(ean, dados.ean());
    assertEquals("Produto Teste GTIN Valido", dados.nome());
    assertEquals(0, new BigDecimal("15.90").compareTo(dados.precoAtual()));
    assertEquals(20, dados.estoqueDisponivel());

    assertEquals(4L, dados.id());

verify(precoRepository)
        .findFirstByProdutoIdOrderByRegistradoEmDescIdDesc(4L);

verify(estoqueRepository).findById(4L);
}
}
