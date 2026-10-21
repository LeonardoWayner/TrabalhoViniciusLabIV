package br.com.flastore.service;

import br.com.flastore.model.Fornecedor;
import br.com.flastore.repository.FornecedorRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FornecedorService {
    private final FornecedorRepository fornecedores;

    public FornecedorService(FornecedorRepository fornecedores) {
        this.fornecedores = fornecedores;
    }

    public List<Fornecedor> listar() {
        return fornecedores.findAll(Sort.by("nome"));
    }

    @Transactional
    public String cadastrar(Fornecedor dados) {
        if (dados.getCnpj() != null && fornecedores.existsByCnpj(dados.getCnpj())) {
            return "Já existe um fornecedor com esse CNPJ.";
        }
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(dados.getNome());
        fornecedor.setCnpj(dados.getCnpj());
        fornecedor.setTelefone(dados.getTelefone());
        fornecedor.setEmail(dados.getEmail());
        fornecedores.save(fornecedor);
        return null;
    }
}
