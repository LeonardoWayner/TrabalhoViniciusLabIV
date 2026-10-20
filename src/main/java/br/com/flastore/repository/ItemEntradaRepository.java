package br.com.flastore.repository;

import br.com.flastore.model.ItemEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemEntradaRepository extends JpaRepository<ItemEntrada, Long> {
    boolean existsByProdutoId(Long produtoId);
}

