package br.com.flastore.form;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class EntradaForm {
    @NotNull(message = "Selecione um produto.")
    private Long produtoId;
    @NotNull(message = "Selecione um fornecedor.")
    private Long fornecedorId;
    @NotNull(message = "Informe a quantidade.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    private Integer quantidade;
    @NotNull(message = "Informe o custo unitário.")
    @DecimalMin(value = "0.00", message = "O custo não pode ser negativo.")
    @Digits(integer = 8, fraction = 2, message = "Informe um custo com até duas casas decimais.")
    private BigDecimal custoUnitario;
    @Size(max = 255, message = "A observação deve ter até 255 caracteres.")
    private String observacao;

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public Long getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Long fornecedorId) { this.fornecedorId = fornecedorId; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public BigDecimal getCustoUnitario() { return custoUnitario; }
    public void setCustoUnitario(BigDecimal custoUnitario) { this.custoUnitario = custoUnitario; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
