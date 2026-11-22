
package br.com.mcpfarma.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Pesquisa produtos ativos pelo nome
    List<Produto> findTop50ByNomeContainingIgnoreCaseAndAtivoTrueOrderByNomeAsc(
            String nome
    );

    // Lista produtos ativos com paginação
    Page<Produto> findByAtivoTrue(Pageable pageable);

    // Pesquisa pelo código de barras EAN
    Optional<Produto> findByEan(String ean);

    // Pesquisa pelo código interno da drogaria
    Optional<Produto> findByCodigoInterno(String codigoInterno);

    // Lista produtos ativos e autorizados para publicação
    Page<Produto> findByAtivoTrueAndPublicavelTrue(Pageable pageable);
}
