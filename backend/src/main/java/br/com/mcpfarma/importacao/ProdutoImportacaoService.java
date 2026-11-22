
package br.com.mcpfarma.importacao;

import br.com.mcpfarma.produto.*;
import br.com.mcpfarma.preco.*;
import br.com.mcpfarma.estoque.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProdutoImportacaoService {

    private final ProdutoRepository produtoRepository;
    private final PrecoRepository precoRepository;
    private final EstoqueRepository estoqueRepository;

    public ProdutoImportacaoService(
            ProdutoRepository produtoRepository,
            PrecoRepository precoRepository,
            EstoqueRepository estoqueRepository) {

        this.produtoRepository = produtoRepository;
        this.precoRepository = precoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    @Transactional
    public boolean salvar(
            String ean,
            String nome,
            String apresentacao,
            BigDecimal valor,
            int quantidade) {

        if (!GtinValidator.isValid(ean)) {
            throw new IllegalArgumentException(
                "EAN/GTIN inválido: formato ou dígito verificador incorreto."
            );
        }

        if (nome == null || nome.isBlank()
                || apresentacao == null || apresentacao.isBlank()) {
            throw new IllegalArgumentException(
                "Nome e apresentação são obrigatórios."
            );
        }

        if (valor == null || valor.signum() < 0
                || valor.scale() > 2 || valor.precision() > 12) {
            throw new IllegalArgumentException("Preço inválido.");
        }

        if (quantidade < 0) {
            throw new IllegalArgumentException("Estoque inválido.");
        }

        Optional<Produto> existente = produtoRepository.findByEan(ean);

        boolean cadastrado = existente.isEmpty();

        Produto produto = existente.orElseGet(
            () -> new Produto(null, ean, nome, apresentacao)
        );

        if (!cadastrado) {
            produto.atualizarDados(nome, apresentacao);
        }

        produto = produtoRepository.save(produto);

        Optional<Preco> ultimoPreco =
                precoRepository
                    .findFirstByProdutoIdOrderByRegistradoEmDescIdDesc(
                        produto.getId()
                    );

        if (ultimoPreco.isEmpty()
                || ultimoPreco.get().getValor().compareTo(valor) != 0) {
            precoRepository.save(new Preco(produto, valor));
        }

        Optional<Estoque> estoqueExistente =
                estoqueRepository.findById(produto.getId());

        if (estoqueExistente.isPresent()) {
            Estoque estoque = estoqueExistente.get();
            estoque.atualizarQuantidade(quantidade);
            estoqueRepository.save(estoque);
        } else {
            estoqueRepository.save(new Estoque(produto, quantidade));
        }

        return cadastrado;
    }
}
