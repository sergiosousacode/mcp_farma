package br.com.mcpfarma.produto;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
@Entity @Table(name="produto")
public class Produto {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="codigo_interno",length=80,unique=true) private String codigoInterno;
 @Column(length=14,unique=true) private String ean;
 @Column(nullable=false,length=200) private String nome;
 @Column(name="principio_ativo",length=200) private String principioAtivo;
 @Column(length=100) private String concentracao;
 @Column(nullable=false,length=200) private String apresentacao;
 @Column(length=150) private String fabricante;
 @Column(name="registro_ms",length=20) private String registroMs;
 @Column(name="sujeito_prescricao",nullable=false) private boolean sujeitoPrescricao;
 @Column(nullable=false) private boolean publicavel;
 @Column(nullable=false) private boolean ativo=true;
 @Column(name="criado_em",insertable=false,updatable=false) private OffsetDateTime criadoEm;
 protected Produto() {}
 public Produto(String codigoInterno,String ean,String nome,String apresentacao) {
  this.codigoInterno=codigoInterno;this.ean=ean;this.nome=nome;this.apresentacao=apresentacao;
 }

public void atualizarDados(String nome, String apresentacao) {
    if (nome == null || nome.isBlank()) {
        throw new IllegalArgumentException(
            "O nome do medicamento é obrigatório."
        );
    }

    if (apresentacao == null || apresentacao.isBlank()) {
        throw new IllegalArgumentException(
            "A apresentação do medicamento é obrigatória."
        );
    }

    this.nome = nome.trim();
    this.apresentacao = apresentacao.trim();
}

 public Long getId(){return id;}
 public String getCodigoInterno(){return codigoInterno;}
 public String getEan(){return ean;}
 public String getNome(){return nome;}
 public String getApresentacao(){return apresentacao;}
 public boolean isPublicavel(){return publicavel;}
 public boolean isAtivo(){return ativo;}
 public boolean isSujeitoPrescricao() {return sujeitoPrescricao;
}
}


