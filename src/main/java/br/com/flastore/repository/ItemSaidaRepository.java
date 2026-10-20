package br.com.flastore.repository;

import br.com.flastore.model.ItemSaida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemSaidaRepository extends JpaRepository<ItemSaida, Long> {
    boolean existsByProdutoId(Long produtoId);
}

