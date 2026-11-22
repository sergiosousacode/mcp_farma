package br.com.mcpfarma.preco;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrecoRepository extends JpaRepository<Preco, Long> {

    Optional<Preco> findFirstByProdutoIdOrderByRegistradoEmDescIdDesc(
        Long produtoId
    );
}
