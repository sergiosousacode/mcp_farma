
package br.com.mcpfarma.importacao;

import br.com.mcpfarma.importacao.dto.ImportacaoResultadoDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.math.BigDecimal;

@Service
public class ImportacaoService {

    private final ProdutoImportacaoService produtoImportacaoService;
    private final ImportacaoRepository importacaoRepository;
    private final ImportacaoErroRepository importacaoErroRepository;

    public ImportacaoService(
        ProdutoImportacaoService produtoImportacaoService,
        ImportacaoRepository importacaoRepository,
        ImportacaoErroRepository importacaoErroRepository) {

    this.produtoImportacaoService = produtoImportacaoService;
    this.importacaoRepository = importacaoRepository;
    this.importacaoErroRepository = importacaoErroRepository;
}

    public ImportacaoResultadoDTO importarCsv(MultipartFile arquivo) {

        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException(
                "O arquivo CSV está vazio."
            );
        }

        Importacao importacao = new Importacao(
            arquivo.getOriginalFilename() != null
                ? arquivo.getOriginalFilename()
                : "arquivo-sem-nome.csv"
        );

        importacao = importacaoRepository.save(importacao);

        int totalLinhas = 0;
        int cadastrados = 0;
        int atualizados = 0;
        int rejeitados = 0;

        CSVFormat formato = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreSurroundingSpaces(true)
                .get();

        try (
            InputStreamReader reader = new InputStreamReader(
                arquivo.getInputStream(),
                StandardCharsets.UTF_8
            );
            CSVParser parser = formato.parse(reader)
        ) {

            for (CSVRecord registro : parser) {

                totalLinhas++;

                try {
                    String ean = registro.get("ean").trim();
                    String nome = registro.get("nome").trim();
                    String apresentacao =
                            registro.get("apresentacao").trim();

                    BigDecimal preco = new BigDecimal(
                            registro.get("preco").trim()
                                    .replace(",", ".")
                    );

                    int estoque = Integer.parseInt(
                            registro.get("estoque").trim()
                    );

                    if (nome.isBlank() || apresentacao.isBlank()) {
                        throw new IllegalArgumentException(
                            "Nome e apresentação são obrigatórios."
                        );
                    }

                    if (preco.signum() < 0 || estoque < 0) {
                        throw new IllegalArgumentException(
                            "Preço ou estoque negativo."
                        );
                    }

                    boolean novo = produtoImportacaoService.salvar(
                        ean,
                        nome,
                        apresentacao,
                        preco,
                        estoque
                    );

                    if (novo) {
                        cadastrados++;
                    } else {
                        atualizados++;
                    }

                } catch (IllegalArgumentException e) {
                    rejeitados++;

                int numeroLinha = Math.toIntExact(
                    registro.getRecordNumber() + 1
                );

                ImportacaoErro erro = new ImportacaoErro(
                    importacao,
                    numeroLinha,
                    e.getMessage()
                );

                importacaoErroRepository.save(erro);

                System.err.println(
                    "Linha " + numeroLinha
                    + ": " + e.getMessage()
                );
            }
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                "Erro ao processar o arquivo CSV.", e
            );
        }

        importacao.finalizar(
            totalLinhas,
            cadastrados + atualizados,
            rejeitados
        );

        importacaoRepository.save(importacao);

        return new ImportacaoResultadoDTO(
            totalLinhas,
            cadastrados,
            atualizados,
            rejeitados,
            importacao.getStatus()
        );
    }
}

