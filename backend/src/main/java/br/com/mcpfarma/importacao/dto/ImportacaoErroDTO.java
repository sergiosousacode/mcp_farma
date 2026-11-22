package br.com.mcpfarma.importacao.dto;

public record ImportacaoErroDTO(
        Long id,
        int numeroLinha,
        String motivo
) {
}