package br.com.flastore.service;

import br.com.flastore.form.*;
import br.com.flastore.model.*;
import br.com.flastore.repository.*;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EstoqueService {
    private final ProdutoRepository produtos;
    private final FornecedorRepository fornecedores;
    private final UsuarioRepository usuarios;
    private final EntradaRepository entradas;
    private final ItemEntradaRepository itensEntrada;
    private final SaidaRepository saidas;
    private final ItemSaidaRepository itensSaida;

    public EstoqueService(ProdutoRepository produtos, FornecedorRepository fornecedores,
            UsuarioRepository usuarios, EntradaRepository entradas, ItemEntradaRepository itensEntrada,
            SaidaRepository saidas, ItemSaidaRepository itensSaida) {
        this.produtos = produtos;
        this.fornecedores = fornecedores;
        this.usuarios = usuarios;
        this.entradas = entradas;
        this.itensEntrada = itensEntrada;
        this.saidas = saidas;
        this.itensSaida = itensSaida;
    }

    public List<Fornecedor> listarFornecedores() {
        return fornecedores.findAll(Sort.by("nome"));
    }
    public List<ItemEntrada> listarEntradas() {
        return itensEntrada.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
    public List<ItemSaida> listarSaidas() {
        return itensSaida.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public String registrarEntrada(EntradaForm dados, Long usuarioId) {
        Produto produto = produtos.buscarParaAtualizar(dados.getProdutoId()).orElse(null);
        Fornecedor fornecedor = fornecedores.findById(dados.getFornecedorId()).orElse(null);
        Usuario usuario = usuarios.findById(usuarioId).orElse(null);
        if (produto == null || produto.isExcluido()) {
            return "Produto não encontrado.";
        }
        if (fornecedor == null) {
            return "Fornecedor não encontrado.";
        }
        if (usuario == null || !usuario.getAtivo()) {
            return "Usuário não autorizado.";
        }
        if (dados.getQuantidade() > Integer.MAX_VALUE - produto.getEstoque()) {
            return "Quantidade excede o limite de estoque.";
        }

        Entrada entrada = new Entrada();
        entrada.setFornecedor(fornecedor);
        entrada.setUsuario(usuario);
        entrada.setObservacao(dados.getObservacao());
        entradas.save(entrada);

        ItemEntrada item = new ItemEntrada();
        item.setEntrada(entrada);
        item.setProduto(produto);
        item.setQuantidade(dados.getQuantidade());
        item.setCustoUnitario(dados.getCustoUnitario());
        itensEntrada.save(item);

        produto.setEstoque(produto.getEstoque() + dados.getQuantidade());
        produtos.save(produto);
        return null;
    }

    @Transactional
    public String registrarSaida(SaidaForm dados, Long usuarioId) {
        Produto produto = produtos.buscarParaAtualizar(dados.getProdutoId()).orElse(null);
        Usuario usuario = usuarios.findById(usuarioId).orElse(null);
        if (produto == null || produto.isExcluido()) {
            return "Produto não encontrado.";
        }
        if (usuario == null || !usuario.getAtivo()) {
            return "Usuário não autorizado.";
        }
        if (dados.getQuantidade() > produto.getEstoque()) {
            return "Estoque insuficiente para essa saída.";
        }

        Saida saida = new Saida();
        saida.setUsuario(usuario);
        saida.setMotivo(dados.getMotivo());
        saida.setObservacao(dados.getObservacao());
        saidas.save(saida);

        ItemSaida item = new ItemSaida();
        item.setSaida(saida);
        item.setProduto(produto);
        item.setQuantidade(dados.getQuantidade());
        itensSaida.save(item);

        produto.setEstoque(produto.getEstoque() - dados.getQuantidade());
        produtos.save(produto);
        return null;
    }
}
