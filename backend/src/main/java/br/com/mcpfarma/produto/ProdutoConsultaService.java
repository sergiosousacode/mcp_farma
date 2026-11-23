
package br.com.mcpfarma.produto;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoConsultaService {

    private final ProdutoRepository repository;

    public ProdutoConsultaService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Optional<ProdutoController.ProdutoResumo> buscarPorEan(
            String ean) {

        return repository.findByEan(ean)
                .filter(Produto::isAtivo)
                .map(produto -> new ProdutoController.ProdutoResumo(
                        produto.getId(),
                        produto.getEan(),
                        produto.getNome(),
                        produto.getApresentacao()
                ));
    }


    public Optional<ProdutoController.ProdutoResumo> buscarPublicavelPorEan(
                String ean) {

            return repository.findByEan(ean)
                    .filter(Produto::isAtivo)
                    .filter(Produto::isPublicavel)
                    .map(produto -> new ProdutoController.ProdutoResumo(
                            produto.getId(),
                            produto.getEan(),
                            produto.getNome(),
                            produto.getApresentacao()
                    ));
        }

}
