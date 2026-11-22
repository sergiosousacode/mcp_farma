package br.com.mcpfarma.importacao;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "importacao")
public class Importacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_arquivo", nullable = false, length = 255)
    private String nomeArquivo;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "total_linhas", nullable = false)
    private int totalLinhas;

    @Column(name = "importadas", nullable = false)
    private int importadas;

    @Column(name = "rejeitadas", nullable = false)
    private int rejeitadas;

    @Column(name = "iniciada_em", nullable = false)
    private OffsetDateTime iniciadaEm;

    @Column(name = "finalizada_em")
    private OffsetDateTime finalizadaEm;

    protected Importacao() {
    }

    public Importacao(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        this.status = "EM_ANDAMENTO";
        this.iniciadaEm = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void finalizar(
            int totalLinhas,
            int importadas,
            int rejeitadas) {

        this.totalLinhas = totalLinhas;
        this.importadas = importadas;
        this.rejeitadas = rejeitadas;

        this.status = rejeitadas == 0
                ? "IMPORTACAO_CONCLUIDA"
                : "IMPORTACAO_COM_ERROS";

        this.finalizadaEm = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public Long getId() {
        return id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalLinhas() {
        return totalLinhas;
    }

    public int getImportadas() {
        return importadas;
    }

    public int getRejeitadas() {
        return rejeitadas;
    }

    public OffsetDateTime getIniciadaEm() {
        return iniciadaEm;
    }

    public OffsetDateTime getFinalizadaEm() {
        return finalizadaEm;
    }
}