package br.com.mcpfarma.importacao;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImportacaoErroRepository
        extends JpaRepository<ImportacaoErro, Long> {

    List<ImportacaoErro> findByImportacaoIdOrderByNumeroLinhaAsc(
            Long importacaoId
    );
}