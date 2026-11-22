
package br.com.mcpfarma.importacao;

import br.com.mcpfarma.importacao.dto.ImportacaoErroDTO;
import br.com.mcpfarma.importacao.dto.ImportacaoHistoricoDTO;
import br.com.mcpfarma.importacao.dto.ImportacaoResultadoDTO;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/importacoes")
public class ImportacaoController {

    private final ImportacaoService importacaoService;
    private final ImportacaoRepository importacaoRepository;
    private final ImportacaoErroRepository importacaoErroRepository;

    public ImportacaoController(
            ImportacaoService importacaoService,
            ImportacaoRepository importacaoRepository,
            ImportacaoErroRepository importacaoErroRepository) {

        this.importacaoService = importacaoService;
        this.importacaoRepository = importacaoRepository;
        this.importacaoErroRepository = importacaoErroRepository;
    }

    // Importar arquivo CSV
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportacaoResultadoDTO> importar(
            @RequestParam("arquivo") MultipartFile arquivo) {

        ImportacaoResultadoDTO resultado =
                importacaoService.importarCsv(arquivo);

        return ResponseEntity.ok(resultado);
    }

    // Listar histórico de importações
    @GetMapping
    public ResponseEntity<List<ImportacaoHistoricoDTO>> listarImportacoes() {

        List<Importacao> importacoes = importacaoRepository.findAll(
                Sort.by(Sort.Direction.DESC, "id")
        );

        List<ImportacaoHistoricoDTO> historico = importacoes
                .stream()
                .map(importacao -> new ImportacaoHistoricoDTO(
                        importacao.getId(),
                        importacao.getNomeArquivo(),
                        importacao.getStatus(),
                        importacao.getTotalLinhas(),
                        importacao.getImportadas(),
                        importacao.getRejeitadas(),
                        importacao.getIniciadaEm(),
                        importacao.getFinalizadaEm()
                ))
                .toList();

        return ResponseEntity.ok(historico);
    }

    @GetMapping("/{id}")
public ResponseEntity<ImportacaoHistoricoDTO> consultarImportacao(
        @PathVariable("id") Long id) {

    return importacaoRepository.findById(id)
            .map(importacao -> new ImportacaoHistoricoDTO(
                    importacao.getId(),
                    importacao.getNomeArquivo(),
                    importacao.getStatus(),
                    importacao.getTotalLinhas(),
                    importacao.getImportadas(),
                    importacao.getRejeitadas(),
                    importacao.getIniciadaEm(),
                    importacao.getFinalizadaEm()
            ))
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}

    // Consultar erros de uma importação
    @GetMapping("/{id}/erros")
    public ResponseEntity<List<ImportacaoErroDTO>> consultarErros(
            @PathVariable("id") Long id) {

        if (!importacaoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        List<ImportacaoErroDTO> erros = importacaoErroRepository
                .findByImportacaoIdOrderByNumeroLinhaAsc(id)
                .stream()
                .map(erro -> new ImportacaoErroDTO(
                        erro.getId(),
                        erro.getNumeroLinha(),
                        erro.getMotivo()
                ))
                .toList();

        return ResponseEntity.ok(erros);
    }
}
