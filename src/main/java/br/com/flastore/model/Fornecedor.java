package br.com.flastore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "fornecedores")
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe o nome do fornecedor.")
    @Size(max = 120, message = "O nome deve ter até 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String nome;

    @Pattern(regexp = "[0-9]{14}", message = "Informe o CNPJ com 14 números ou deixe vazio.")
    @Column(unique = true, length = 14)
    private String cnpj;

    @Size(max = 20, message = "O telefone deve ter até 20 caracteres.")
    @Column(length = 20)
    private String telefone;

    @Email(message = "Informe um e-mail válido.")
    @Size(max = 120, message = "O e-mail deve ter até 120 caracteres.")
    @Column(length = 120)
    private String email;

    public Fornecedor() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome == null ? null : nome.trim(); }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj == null || cnpj.isBlank() ? null : cnpj.trim(); }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

