package br.com.mcpfarma.importacao.dto;

public record ImportacaoResultadoDTO(
    int totalLinhas,
    int cadastrados,
    int atualizados,
    int rejeitados,
    String status
) {}
