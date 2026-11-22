
package br.com.mcpfarma.produto;

import br.com.mcpfarma.estoque.EstoqueRepository;
import br.com.mcpfarma.preco.PrecoRepository;
import br.com.mcpfarma.produto.dto.ProdutoDisponibilidadeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    private final ProdutoRepository repository;
    private final PrecoRepository precoRepository;
    private final EstoqueRepository estoqueRepository;

    public ProdutoController(
            ProdutoRepository repository,
            PrecoRepository precoRepository,
            EstoqueRepository estoqueRepository) {

        this.repository = repository;
        this.precoRepository = precoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    // Listar produtos ou pesquisar por nome
    @GetMapping
    public ResponseEntity<?> buscar(
            @RequestParam(defaultValue = "") String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (!nome.isBlank()) {

            List<ProdutoResumo> produtos = repository
                    .findTop50ByNomeContainingIgnoreCaseAndAtivoTrueOrderByNomeAsc(
                            nome.trim()
                    )
                    .stream()
                    .map(this::converter)
                    .toList();

            return ResponseEntity.ok(produtos);
        }

        if (page < 0 || size < 1 || size > 100) {
            return ResponseEntity.badRequest().body(
                    "Parâmetros inválidos: page >= 0 e size entre 1 e 100."
            );
        }

        PageRequest paginacao = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "nome")
        );

        Page<ProdutoResumo> produtos = repository
                .findByAtivoTrue(paginacao)
                .map(this::converter);

        return ResponseEntity.ok(produtos);
    }

    // Consultar produto pelo EAN
    @GetMapping("/ean/{ean}")
    public ResponseEntity<ProdutoResumo> buscarPorEan(
            @PathVariable String ean) {

        return repository.findByEan(ean)
                .filter(Produto::isAtivo)
                .map(this::converter)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Consultar produto, preço atual e estoque
    @GetMapping("/ean/{ean}/disponibilidade")
    public ResponseEntity<ProdutoDisponibilidadeDTO> consultarDisponibilidade(
            @PathVariable String ean) {

        return repository.findByEan(ean)
                .filter(Produto::isAtivo)
                .map(produto -> {

                    BigDecimal precoAtual = precoRepository
                            .findFirstByProdutoIdOrderByRegistradoEmDescIdDesc(
                                    produto.getId()
                            )
                            .map(preco -> preco.getValor())
                            .orElse(null);

                    Integer estoqueDisponivel = estoqueRepository
                            .findById(produto.getId())
                            .map(estoque -> estoque.getQuantidade())
                            .orElse(null);

                    ProdutoDisponibilidadeDTO resposta =
                            new ProdutoDisponibilidadeDTO(
                                    produto.getId(),
                                    produto.getEan(),
                                    produto.getNome(),
                                    produto.getApresentacao(),
                                    precoAtual,
                                    estoqueDisponivel
                            );

                    return ResponseEntity.ok(resposta);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ProdutoResumo converter(Produto produto) {
        return new ProdutoResumo(
                produto.getId(),
                produto.getEan(),
                produto.getNome(),
                produto.getApresentacao()
        );
    }

    public record ProdutoResumo(
            Long id,
            String ean,
            String nome,
            String apresentacao
    ) {
    }
}
