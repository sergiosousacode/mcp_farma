package br.com.mcpfarma.importacao;

import jakarta.persistence.*;

@Entity
@Table(name = "importacao_erro")
public class ImportacaoErro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "importacao_id", nullable = false)
    private Importacao importacao;

    @Column(name = "numero_linha", nullable = false)
    private int numeroLinha;

    @Column(name = "motivo", nullable = false, columnDefinition = "text")
    private String motivo;

    protected ImportacaoErro() {
    }

    public ImportacaoErro(
            Importacao importacao,
            int numeroLinha,
            String motivo) {

        if (importacao == null) {
            throw new IllegalArgumentException(
                "A importação é obrigatória."
            );
        }

        if (numeroLinha < 1) {
            throw new IllegalArgumentException(
                "O número da linha deve ser positivo."
            );
        }

        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException(
                "O motivo do erro é obrigatório."
            );
        }

        this.importacao = importacao;
        this.numeroLinha = numeroLinha;
        this.motivo = motivo;
    }

    public Long getId() {
        return id;
    }

    public Importacao getImportacao() {
        return importacao;
    }

    public int getNumeroLinha() {
        return numeroLinha;
    }

    public String getMotivo() {
        return motivo;
    }
}