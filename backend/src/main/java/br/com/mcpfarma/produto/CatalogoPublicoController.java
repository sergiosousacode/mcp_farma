
package br.com.mcpfarma.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalogo-publico/produtos")
public class CatalogoPublicoController {

    private final ProdutoRepository produtoRepository;

    public CatalogoPublicoController(
            ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

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

        Page<ProdutoPublicoDTO> produtos = produtoRepository
                .findByAtivoTrueAndPublicavelTrue(paginacao)
                .map(produto -> new ProdutoPublicoDTO(
                        produto.getId(),
                        produto.getEan(),
                        produto.getNome(),
                        produto.getApresentacao()
                ));

        return ResponseEntity.ok(produtos);
    }

    public record ProdutoPublicoDTO(
            Long id,
            String ean,
            String nome,
            String apresentacao
    ) {
    }
}
