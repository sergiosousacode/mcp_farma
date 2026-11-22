package br.com.mcpfarma.importacao.dto;

import java.time.OffsetDateTime;

public record ImportacaoHistoricoDTO(
        Long id,
        String nomeArquivo,
        String status,
        int totalLinhas,
        int importadas,
        int rejeitadas,
        OffsetDateTime iniciadaEm,
        OffsetDateTime finalizadaEm
) {
}