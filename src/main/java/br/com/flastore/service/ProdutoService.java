package br.com.flastore.service;

import br.com.flastore.model.*;
import br.com.flastore.repository.*;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProdutoService {
    private final ProdutoRepository produtos;
    private final CategoriaRepository categorias;
    private final ItemEntradaRepository itensEntrada;
    private final ItemSaidaRepository itensSaida;

    public ProdutoService(ProdutoRepository produtos, CategoriaRepository categorias,
            ItemEntradaRepository itensEntrada, ItemSaidaRepository itensSaida) {
        this.produtos = produtos;
        this.categorias = categorias;
        this.itensEntrada = itensEntrada;
        this.itensSaida = itensSaida;
    }

    public List<Produto> listar() {
        return produtos.findAll(Sort.by("id"));
    }
    public List<Categoria> listarCategorias() {
        return categorias.findAll(Sort.by("nome"));
    }
    public Produto buscar(Long id) {
        return produtos.findById(id).orElse(null);
    }

    public String validar(Produto produto) {
        boolean codigoDuplicado;
        if (produto.getId() == null) {
            codigoDuplicado = produtos.existsByCodigo(produto.getCodigo());
        } else {
            codigoDuplicado = produtos.existsByCodigoAndIdNot(produto.getCodigo(), produto.getId());
        }
        if (codigoDuplicado) {
            return "Já existe um produto com esse código.";
        }
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null
                || !categorias.existsById(produto.getCategoria().getId())) {
            return "Selecione uma categoria existente.";
        }
        return null;
    }

    @Transactional
    public String cadastrar(Produto dados) {
        String erro = validar(dados);
        if (erro != null) {
            return erro;
        }
        Produto novo = new Produto();
        copiarCadastro(dados, novo);
        novo.setEstoque(0);
        produtos.save(novo);
        return null;
    }

    @Transactional
    public String atualizar(Long id, Produto dados) {
        Produto existente = produtos.buscarParaAtualizar(id).orElse(null);
        if (existente == null) {
            return "Produto não encontrado.";
        }
        dados.setId(id);
        String erro = validar(dados);
        if (erro != null) {
            return erro;
        }
        copiarCadastro(dados, existente);
        produtos.save(existente);
        return null;
    }

    private void copiarCadastro(Produto origem, Produto destino) {
        destino.setCodigo(origem.getCodigo());
        destino.setNome(origem.getNome());
        destino.setDescricao(origem.getDescricao());
        destino.setPreco(origem.getPreco());
        destino.setCategoria(categorias.findById(origem.getCategoria().getId()).orElseThrow());
    }

    @Transactional
    public String excluir(Long id) {
        Produto produto = produtos.buscarParaAtualizar(id).orElse(null);
        if (produto == null) {
            return "Produto não encontrado.";
        }
        if (itensEntrada.existsByProdutoId(id) || itensSaida.existsByProdutoId(id)) {
            return "Produto com movimentações não pode ser excluído, para preservar o histórico.";
        }
        produtos.deleteById(id);
        return null;
    }
}
