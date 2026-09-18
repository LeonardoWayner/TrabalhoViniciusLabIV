package br.com.flastore.model;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class Produto {
    private String codigo;
    @NotBlank(message = "Informe o nome.") @Size(max = 120, message = "O nome deve ter até 120 caracteres.")
    private String nome;
    @NotBlank(message = "Selecione uma categoria.")
    @Pattern(regexp = "Camisas|Acessórios|Casa|Kits", message = "Categoria inválida.")
    private String categoria;
    @NotNull(message = "Informe o preço.") @DecimalMin(value = "1.00", message = "O preço mínimo é R$ 1,00.")
    @Digits(integer = 8, fraction = 2, message = "Informe um preço com até duas casas decimais.")
    private BigDecimal preco;
    @NotNull(message = "Informe o estoque.") @Min(value = 0, message = "O estoque não pode ser negativo.")
    private Integer estoque;
    @NotBlank(message = "Informe a descrição.") @Size(max = 2000, message = "A descrição deve ter até 2000 caracteres.")
    private String descricao;

    public Produto() {}
    public Produto(String codigo, String nome, String categoria, BigDecimal preco, Integer estoque, String descricao) {
        this.codigo = codigo; this.nome = nome; this.categoria = categoria;
        this.preco = preco; this.estoque = estoque; this.descricao = descricao;
    }
    public Produto copia() { return new Produto(codigo, nome, categoria, preco, estoque, descricao); }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome == null ? null : nome.trim(); }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao == null ? null : descricao.trim(); }
    public String getSituacao() { return estoque == null || estoque == 0 ? "Esgotado" : estoque <= 5 ? "Baixo estoque" : "Disponível"; }
    public String getClasseSituacao() { return estoque == null || estoque == 0 ? "indisponivel" : estoque <= 5 ? "atencao" : "ok"; }
    public String getPrecoFormatado() { return preco == null ? "" : NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(preco); }
}
