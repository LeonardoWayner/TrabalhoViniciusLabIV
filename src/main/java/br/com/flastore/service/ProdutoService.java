package br.com.flastore.service;

import br.com.flastore.model.Produto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Dados simulados: os registros voltam ao estado inicial quando o servidor reinicia. */
@Service
public class ProdutoService {
    private final List<Produto> produtos = new ArrayList<>();
    private int proximoCodigo = 9;
    public ProdutoService() {
        produtos.add(new Produto("001", "Camisa Rubro-Negra", "Camisas", new BigDecimal("189.9"), 24, "Modelo listrado em tecido leve, usado como exemplo para demonstrar a tela de visualização do sistema."));
        produtos.add(new Produto("002", "Jaqueta Arquibancada", "Camisas", new BigDecimal("249.9"), 8, "Jaqueta preta com detalhe vermelho para dias de jogo à noite."));
        produtos.add(new Produto("003", "Cachecol Torcida", "Acessórios", new BigDecimal("79.9"), 5, "Cachecol macio com faixas vermelhas e pretas para a arquibancada."));
        produtos.add(new Produto("004", "Caneca Listrada", "Casa", new BigDecimal("49.9"), 18, "Caneca de cerâmica para café, coleção ou presente de torcedor."));
        produtos.add(new Produto("005", "Boné Nação", "Acessórios", new BigDecimal("69.9"), 3, "Boné preto com detalhe vermelho, aba curva e ajuste traseiro."));
        produtos.add(new Produto("006", "Kit Presente", "Kits", new BigDecimal("299.9"), 11, "Caixa especial com camisa, caneca e cachecol para presentear."));
        produtos.add(new Produto("007", "Garrafa de Treino", "Acessórios", new BigDecimal("59.9"), 20, "Garrafa esportiva com pegada confortável e visual rubro-negro."));
        produtos.add(new Produto("008", "Camisa Retrô", "Camisas", new BigDecimal("219.9"), 0, "Peça de algodão com listras clássicas e corte casual."));
    }
    public synchronized List<Produto> listar() { return produtos.stream().map(Produto::copia).toList(); }
    public synchronized Produto buscar(String codigo) {
        return produtos.stream().filter(p -> p.getCodigo().equals(codigo)).findFirst().map(Produto::copia)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
    public synchronized void cadastrar(Produto produto) {
        Produto novo = produto.copia();
        novo.setCodigo(String.format("%03d", proximoCodigo++));
        produtos.add(novo);
    }
    public synchronized void atualizar(String codigo, Produto produto) {
        buscar(codigo);
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getCodigo().equals(codigo)) {
                Produto atualizado = produto.copia();
                atualizado.setCodigo(codigo);
                produtos.set(i, atualizado);
                return;
            }
        }
    }
}
