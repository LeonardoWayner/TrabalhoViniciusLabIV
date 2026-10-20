package br.com.flastore.form;

import jakarta.validation.constraints.*;

public class SaidaForm {
    @NotNull(message = "Selecione um produto.")
    private Long produtoId;
    @NotNull(message = "Informe a quantidade.")
    @Min(value = 1, message = "A quantidade deve ser maior que zero.")
    private Integer quantidade;
    @NotBlank(message = "Selecione o motivo.")
    @Pattern(regexp = "VENDA|PERDA|USO_INTERNO", message = "Motivo inválido.")
    private String motivo;
    @Size(max = 255, message = "A observação deve ter até 255 caracteres.")
    private String observacao;

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
