package br.com.flastore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe o código.")
    @Size(max = 20, message = "O código deve ter até 20 caracteres.")
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @NotBlank(message = "Informe o nome.")
    @Size(max = 120, message = "O nome deve ter até 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String nome;

    @NotNull(message = "Selecione uma categoria.")
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @NotNull(message = "Informe o preço.")
    @DecimalMin(value = "1.00", message = "O preço mínimo é R$ 1,00.")
    @Digits(integer = 8, fraction = 2, message = "Informe um preço com até duas casas decimais.")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Min(value = 0, message = "O estoque não pode ser negativo.")
    @Column(nullable = false)
    private Integer estoque = 0;

    @NotBlank(message = "Informe a descrição.")
    @Size(max = 2000, message = "A descrição deve ter até 2000 caracteres.")
    @Column(nullable = false, length = 2000)
    private String descricao;

    public Produto() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo == null ? null : codigo.trim(); }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome == null ? null : nome.trim(); }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao == null ? null : descricao.trim(); }

    public String getSituacao() {
        if (estoque == null || estoque == 0) return "Esgotado";
        if (estoque <= 5) return "Baixo estoque";
        return "Disponível";
    }

    public String getClasseSituacao() {
        if (estoque == null || estoque == 0) return "indisponivel";
        if (estoque <= 5) return "atencao";
        return "ok";
    }

    public String getPrecoFormatado() {
        return preco == null ? "" : NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(preco);
    }
}
