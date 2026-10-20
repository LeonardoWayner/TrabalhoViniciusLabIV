package br.com.flastore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "itens_saida")
public class ItemSaida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "saida_id", nullable = false)
    private Saida saida;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private Integer quantidade;

    public ItemSaida() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Saida getSaida() { return saida; }
    public void setSaida(Saida saida) { this.saida = saida; }
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}

